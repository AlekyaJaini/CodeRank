package org.example.coderank.service;

import org.example.coderank.model.Problem;
import org.example.coderank.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemService {
    
    private final ProblemRepository problemRepository;
    
    @Autowired
    public ProblemService(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }
    
    public List<Problem> getAllProblems() {
        return problemRepository.findAll();
    }
    
    public Optional<Problem> getProblemById(Long id) {
        return problemRepository.findById(id);
    }
    
    public List<Problem> getProblemsByDifficulty(String difficulty) {
        return problemRepository.findByDifficulty(difficulty);
    }
    
    public List<Problem> searchProblemsByTitle(String title) {
        return problemRepository.findByTitleContainingIgnoreCase(title);
    }
    
    public Problem createProblem(Problem problem) {
        return problemRepository.save(problem);
    }
    
    public Problem updateProblem(Long id, Problem problemDetails) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found with id: " + id));
        
        problem.setTitle(problemDetails.getTitle());
        problem.setDescription(problemDetails.getDescription());
        problem.setDifficulty(problemDetails.getDifficulty());
        problem.setAcceptanceRate(problemDetails.getAcceptanceRate());
        problem.setTestCases(problemDetails.getTestCases());
        problem.setConstraints(problemDetails.getConstraints());
        
        return problemRepository.save(problem);
    }
    
    public void deleteProblem(Long id) {
        problemRepository.deleteById(id);
    }
}
