package org.example.coderank.service;

public class ExecutionResult {
    private final String stdout;
    private final String stderr;
    private final String status; // ACCEPTED / RUNTIME_ERROR / COMPILE_ERROR / TIMEOUT / INTERNAL_ERROR
    private final long execTimeMs;

    public ExecutionResult(String stdout, String stderr, String status, long execTimeMs) {
        this.stdout = stdout;
        this.stderr = stderr;
        this.status = status;
        this.execTimeMs = execTimeMs;
    }
    public String getStdout() { return stdout; }
    public String getStderr() { return stderr; }
    public String getStatus() { return status; }
    public long getExecTimeMs() { return execTimeMs; }
}
