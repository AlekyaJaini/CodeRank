package org.example.coderank.repository;

import org.example.coderank.model.Submission;
import org.example.coderank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {

    List<User> findByUsernameContainingIgnoreCase(String username);

    List<User> findByEmailContainingIgnoreCase(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
  // User findById(Long userId);
}
