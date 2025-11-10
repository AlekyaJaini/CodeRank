package org.example.coderank.service;

import org.example.coderank.model.enums.ExecLanguage;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class DockerRunner {

    // Tunables for the runner - change if needed
    private final int overallTimeoutSeconds = 10;   // kill whole container after this
    private final String tmpfsSize = "64m";
    private final String runnerUser = "10001:10001"; // non-root uid:gid inside image
    private final long memoryLimit = 256; // MB
    private final String cpuQuota = "0.5"; // cpus

    public ExecutionResult run(ExecLanguage lang, String source, String stdin) {
        long start = System.currentTimeMillis();

        // Base64 encode to safely pass as env variable
        String srcB64 = Base64.getEncoder().encodeToString(source.getBytes(StandardCharsets.UTF_8));
        String stdinB64 = stdin == null ? "" : Base64.getEncoder().encodeToString(stdin.getBytes(StandardCharsets.UTF_8));

        // unique container name
        String cname = "coderank-run-" + UUID.randomUUID().toString().replace("-", "");

        try {
            // 1️⃣ docker create (prepare container)
            List<String> createCmd = new ArrayList<>();
            createCmd.add("docker");
            createCmd.add("create");
            createCmd.add("--name");
            createCmd.add(cname);
            createCmd.add("--network"); createCmd.add("none");
            createCmd.add("--read-only");
            createCmd.add("--tmpfs"); createCmd.add("/sandbox:rw,size=" + tmpfsSize + ",uid=10001,gid=10001");
            createCmd.add("--cap-drop"); createCmd.add("ALL");
            createCmd.add("--security-opt"); createCmd.add("no-new-privileges");
            createCmd.add("-u"); createCmd.add(runnerUser);
            createCmd.add("-e"); createCmd.add("SRC_B64=" + srcB64);
            createCmd.add("-e"); createCmd.add("STDIN_B64=" + stdinB64);
            createCmd.add("-e"); createCmd.add("EXEC_LANG=" + lang.name());
            createCmd.add("-e");createCmd.add("EXEC_LANG_CODE=" + lang.getCode());
            createCmd.add("--memory"); createCmd.add(memoryLimit + "m");
            createCmd.add("--cpus"); createCmd.add(cpuQuota);
            createCmd.add(lang.getImage());
            if (lang.getRunCmd() != null && lang.getRunCmd().length > 0) {
                String runCmdJoined = String.join(" ", lang.getRunCmd());
                createCmd.add("-e");
                createCmd.add("RUN_CMD=" + runCmdJoined);
            }
            System.out.println(String.join(" ", createCmd));
            Process create = new ProcessBuilder(createCmd).start();
            create.waitFor(5, TimeUnit.SECONDS);
            if (create.exitValue() != 0) {
                String err = new String(create.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
                return new ExecutionResult("", "docker create failed: " + err, "INTERNAL_ERROR",
                        System.currentTimeMillis() - start);
            }

            // 2️⃣ docker start
            Process startProc = new ProcessBuilder("docker", "start", cname).start();
            startProc.waitFor(5, TimeUnit.SECONDS);
            if (startProc.exitValue() != 0) {
                String err = new String(startProc.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
                return new ExecutionResult("", "docker start failed: " + err, "INTERNAL_ERROR",
                        System.currentTimeMillis() - start);
            }

            // 3️⃣ docker wait (block until container stops, or timeout)
            Process waitProc = new ProcessBuilder("docker", "wait", cname).start();
            boolean finished = waitProc.waitFor(overallTimeoutSeconds, TimeUnit.SECONDS);

            if (!finished) {
                // timed out → kill container
                new ProcessBuilder("docker", "rm", "-f", cname).start().waitFor(5, TimeUnit.SECONDS);
                long elapsed = System.currentTimeMillis() - start;
                return new ExecutionResult("", "Execution timed out after " + overallTimeoutSeconds + "s",
                        "TIMEOUT", elapsed);
            }

            // container finished → get exit code (docker wait prints it)
            String exitCodeStr = new String(waitProc.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
            int exitCode = 0;
            try { exitCode = Integer.parseInt(exitCodeStr); } catch (Exception ignore) {}

            // 4️⃣ docker logs (collect stdout/stderr)x
            Process logsProc = new ProcessBuilder("docker", "logs", cname).redirectErrorStream(true).start();
            logsProc.waitFor(5, TimeUnit.SECONDS);
            String logs = new String(logsProc.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            // 5️⃣ cleanup
            new ProcessBuilder("docker", "rm", "-f", cname).start().waitFor(5, TimeUnit.SECONDS);

            long elapsed = System.currentTimeMillis() - start;

            if (exitCode == 0) {
                return new ExecutionResult(logs, "", "SUCCESS", elapsed);
            } else {
                String status = logs.toLowerCase().contains("javac") ? "COMPILE_ERROR" : "RUNTIME_ERROR";
                return new ExecutionResult("", logs, status, elapsed);
            }

        } catch (Exception e) {
            long elapsed = System.currentTimeMillis() - start;
            return new ExecutionResult("", "Internal executor error: " + e.getMessage(), "INTERNAL_ERROR", elapsed);
        }
    }

}
