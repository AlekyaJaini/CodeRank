package org.example.coderank.worker;

import jakarta.transaction.Transactional;
import org.example.coderank.repository.SubmissionRepository;
import org.example.coderank.service.CodeExecutionService;
import org.example.coderank.service.ExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
@Component
public class SubmissionWorker {
    private final SubmissionRepository submissionRepository;
    private final CodeExecutionService executionService;

    @Autowired
    public SubmissionWorker(SubmissionRepository submissionRepository, CodeExecutionService executionService) {
        this.submissionRepository = submissionRepository;
        this.executionService = executionService;
        System.out.println("SubmissionWorker initialized");
    }

    @KafkaListener(topics = "submissions.new", groupId = "submission-workers")
@Transactional
    public void onMessage(String submissionIdStr) {
        long id = Long.parseLong(submissionIdStr);
        //long id1=11;
        submissionRepository.findById(id).ifPresent(sub -> {
            if (!"PENDING".equals(sub.getStatus())) return; // idempotency
            sub.setStatus("RUNNING");
            submissionRepository.save(sub);
            System.out.println("================");
            System.out.println("Processing submission id ===saved: " + id);
            System.out.println("================");
            ExecutionResult result = executionService.executeSubmission(sub);

            sub.setStatus(result.getStatus());
            sub.setOutput(truncate(result.getStdout(), 100000)); // avoid huge blobs
            //check hereeee
            sub.setErr(truncate(result.getStderr(), 100000));
            sub.setExecTimeMs(result.getExecTimeMs());
            sub.setFinishedAt(Instant.now());
            System.out.println("=============saved===========");
            submissionRepository.save(sub);
        });
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max) + "\n...TRUNCATED...";
    }
}
