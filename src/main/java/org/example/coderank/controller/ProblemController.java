package org.example.coderank.controller;

import org.example.coderank.dto.ProblemListDTO;
import org.example.coderank.model.Problem;
import org.example.coderank.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/problems")
public class ProblemController {

    private final ProblemService problemService;

    @Autowired
    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping
    public ResponseEntity<Page<ProblemListDTO>> getAllProblems(
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String search, Pageable pageable) {

        List<Problem> problems;
        Page<ProblemListDTO> page = problemService.getAllProblems(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Problem> getProblemById(@PathVariable Integer id) {
        return problemService.getProblemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}