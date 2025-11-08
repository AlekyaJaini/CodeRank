package org.example.coderank.service;

import jakarta.websocket.SendResult;
import org.example.coderank.dto.SubmsissionRequestDTO;
import org.example.coderank.exception.ResourceNotFoundException;
import org.example.coderank.model.Problem;
import org.example.coderank.model.Submission;
import org.example.coderank.model.User;
import org.example.coderank.repository.ProblemRepository;
import org.example.coderank.repository.SubmissionRepository;
import org.example.coderank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

@Autowired
    // Inject both repositories via constructor
    public SubmissionService(SubmissionRepository submissionRepository,
                             ProblemRepository problemRepository ,
                             UserRepository userRepository,
                             KafkaTemplate<String, String> kafkaTemplate) {
        this.submissionRepository = submissionRepository;
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
    this.kafkaTemplate = kafkaTemplate;
    }



    public Submission createSubmission(SubmsissionRequestDTO submissiondto) {
        // Here you would typically add code execution logic
        // For now, we'll just save the submission with a default status
//        if (submission.getStatus() == null) {
//            submission.setStatus("PENDING");
//        }


        Optional user = userRepository.findById(Long.valueOf(submissiondto.getUserId()));
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "User not found with id: " + submissiondto.getUserId()));

        Problem problem = problemRepository.findById(submissiondto.getProblemId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Problem not found with id: " + submissiondto.getProblemId()));

        Submission submission = Submission.builder()
                .problem(problem)
                .language(submissiondto.getLanguage())
                .code(submissiondto.getCode())
                .stdin(submissiondto.getStdin())
                .status("PENDING")
                .user((User) user.get())
                .build();

        Submission saved= submissionRepository.save(submission);


        kafkaTemplate.send("submissions.new", String.valueOf(saved.getId()));
        return saved;
    }
    
    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }
    
    public Optional<Submission> getSubmissionById(Long id) {
        return submissionRepository.findById(id);
    }
    
    public List<Submission> getSubmissionsByUserId(Long userId) {
        return submissionRepository.findByUserId(userId);
    }
    
    public List<Submission> getSubmissionsByProblemId(UUID problemId) {
        return submissionRepository.findByProblemId(problemId);
    }
    
    public List<Submission> getSubmissionsByStatus(String status) {
        return submissionRepository.findByStatus(status);
    }

    public Optional<Submission> findById(Long id) {
        System.out.println("=========Finding submission by id=========: " + id);
        return submissionRepository.findById(id);
    }
}
