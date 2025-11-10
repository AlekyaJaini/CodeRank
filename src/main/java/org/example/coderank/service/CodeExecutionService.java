package org.example.coderank.service;

import org.example.coderank.model.Submission;
import org.example.coderank.model.enums.ExecLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class CodeExecutionService {
    private final DockerRunner dockerRunner;

    @Autowired
    public CodeExecutionService(DockerRunner dockerRunner) {
        this.dockerRunner = dockerRunner;
        System.out.println("CodeExecutionService initialized");
    }

    public ExecutionResult executeSubmission(Submission submission) {
        ExecLanguage lang = ExecLanguage.fromCode(submission.getLanguage());
        String source = submission.getCode();
        String stdin = submission.getStdin();
        if (stdin == null) stdin = "";
        stdin = stdin.trim();
        if (!stdin.endsWith("\n")) stdin = stdin + "\n";
        String stdinB64 = Base64.getEncoder().encodeToString(stdin.getBytes(StandardCharsets.UTF_8));
        return dockerRunner.run(lang, source, stdin);
    }
}
