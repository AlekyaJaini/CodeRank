package org.example.coderank.repository;

import org.example.coderank.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    
    List<Problem> findByDifficulty(String difficulty);
    
    List<Problem> findByTitleContainingIgnoreCase(String title);
}
