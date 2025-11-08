package org.example.coderank.repository;

import org.example.coderank.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, UUID> {

    boolean existsByExternalId(Integer externalId);
    Optional<Problem> findByExternalId(Integer externalId);
}
