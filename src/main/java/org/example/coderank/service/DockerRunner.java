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

        // container name
        String cname = "coderank-run-" + UUID.randomUUID().toString().replace("-", "");

        // build docker run command
        List<String> cmd = new ArrayList<>();
        cmd.add("docker");
        cmd.add("run");
        cmd.add("--rm");
        cmd.add("--name");
        cmd.add(cname);
        cmd.add("--network");
        cmd.add("none");
        cmd.add("--read-only"); // read-only root
        cmd.add("--tmpfs");
        cmd.add("/sandbox:rw,size=64m,uid=10001,gid=10001");
        cmd.add("--cap-drop");
        cmd.add("ALL");
        cmd.add("--security-opt");
        cmd.add("no-new-privileges");
        cmd.add("-u");
        cmd.add("10001:10001");

// pass source and stdin (base64)
        cmd.add("-e");
        cmd.add("SRC_B64=" + srcB64);
        cmd.add("-e");
        cmd.add("STDIN_B64=" + stdinB64);

// resource limits
        cmd.add("--memory");
        cmd.add("256m");
        cmd.add("--cpus");
        cmd.add("0.5");

// the image
        cmd.add(lang.getImage());

        // Entrypoint inside image should decode SRC_B64 -> file (lan.getFileName()) and run compile/run commands
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);

        StringBuilder combined = new StringBuilder();
        int exitCode = -1;
        try {
            Process p = pb.start();

            // read combined stdout/stderr (container logs)
            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    combined.append(line).append("\n");
                }
            }

            boolean finished = p.waitFor(overallTimeoutSeconds, TimeUnit.SECONDS);
            if (!finished) {
                // kill container
                Process kill = new ProcessBuilder("docker", "rm", "-f", cname).start();
                kill.waitFor(5, TimeUnit.SECONDS);
                long elapsed = System.currentTimeMillis() - start;
                return new ExecutionResult("", "Execution timed out", "TIMEOUT", elapsed);
            }
            exitCode = p.exitValue();

            long elapsed = System.currentTimeMillis() - start;

            // The container image should print a small JSON or at minimum print stdout/stderr markers.
            // We'll parse the combined logs heuristically: many runner images print markers like
            // ===STDOUT===\n ... \n===STDERR===\n ...
            // For simplicity here, if exitCode == 0 => treat combined as stdout, else as stderr.

            if (exitCode == 0) {
                return new ExecutionResult(combined.toString(), "", "SUCCESS", elapsed);
            } else {
                // try to identify compile error vs runtime from container log (the image should exit with special code or print label)
                String log = combined.toString();
                String status = "RUNTIME_ERROR";
                if (log.toLowerCase().contains("compile") || log.toLowerCase().contains("javac")) {
                    status = "COMPILE_ERROR";
                }
                return new ExecutionResult("", log, status, elapsed);
            }

        } catch (IOException | InterruptedException e) {
            long elapsed = System.currentTimeMillis() - start;
            return new ExecutionResult("", "Internal executor error: " + e.getMessage(), "INTERNAL_ERROR", elapsed);
        }
    }
}
