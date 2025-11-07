
package org.example.coderank.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.coderank.model.Problem;
import org.example.coderank.repository.ProblemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Configuration
public class ProblemDataLoader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public CommandLineRunner loadProblems(ProblemRepository problemRepository) {
        return args -> {
            ClassPathResource resource = new ClassPathResource("data/problems_with_testcases.json");
            if (!resource.exists()) {
                System.out.println("[ProblemDataLoader] file not found: data/problems_with_testcases.json");
                return;
            }

            try (InputStream is = resource.getInputStream()) {
                List<Map<String, Object>> seed = objectMapper.readValue(is, new TypeReference<>() {});

                for (Map<String, Object> m : seed) {
                    // read id as String (may be null)
//                    String idStr = (m.get("id") == null) ? null : String.valueOf(m.get("id")).trim();
//                    UUID id = null;
//                    if (idStr != null && !idStr.isEmpty()) {
//                        try {
//                            id = UUID.fromString(idStr);
//                        } catch (IllegalArgumentException ex) {
//                            System.out.printf("[ProblemDataLoader] skipping entry with invalid UUID id='%s'%n", idStr);
//                            continue;
//                        }
//                    }

                    // idempotency check: if id present, check existsById(UUID). If id absent, you may check another unique field (title) if desired
                    Integer externalId = (m.get("externalId") == null) ? null : (Integer) m.get("externalId");
                    if (externalId != null && problemRepository.existsByExternalId(externalId)) {
                        System.out.printf("[ProblemDataLoader] skipping existing problem externalId=%d title=\"%s\"%n",
                                externalId, m.get("title"));
                        continue;
                    }
                    Problem problem = new Problem();
                    problem.setExternalId(externalId);
//
//                    // if id present, set it. If not present, let DB/Hibernate generate it.
//                    if (id != null) {
//                        problem.setId(id); // ensure you have setId(UUID) on Problem entity
//                    }

                    // map other fields (adjust keys/types as needed)
                    problem.setTitle((String) m.getOrDefault("title", "untitled"));
                    problem.setDescription((String) m.getOrDefault("description", ""));
                    problem.setLevel((String) m.getOrDefault("level", "UNKNOWN"));
                    problem.setConstraints((String) m.getOrDefault("constraints", ""));
                    problem.setTime_limit_ms( (Integer) m.getOrDefault("time_limit_ms", 1000));
                    problem.setMemory_limit_kb( (Integer) m.getOrDefault("memory_limit_kb", 65536));
                //    problem.setTestCases( (String) m.getOrDefault("testcases", ""));
                    problem.setTestCases(objectMapper.writeValueAsString(m.get("testcases")));
                    problem.setAcceptanceRate( ((Number) m.getOrDefault("acceptanceRate", 0.0)).doubleValue() );

                    // createdAt mapping - change according to your Problem.setCreatedAt type
                    Object createdObj = m.get("createdAt");
                    if (createdObj != null) {
                        String createdStr = String.valueOf(createdObj);
                        try {
                            // If Problem.setCreatedAt accepts Instant:
                            Instant createdInstant = Instant.parse(createdStr);
                            //problem.setCreatedAt(createdInstant);
                        } catch (DateTimeParseException ex1) {
                            try {
                                // fallback: if your entity uses LocalDateTime
                                LocalDateTime ldt = LocalDateTime.parse(createdStr);
                                // convert or use an overload setter for LocalDateTime
                                // problem.setCreatedAt(ldt);
                                // if your entity expects Instant, convert:
                                //problem.setCreatedAt(ldt.atZone(java.time.ZoneId.systemDefault()).toInstant());
                            } catch (DateTimeParseException ex2) {
                                System.out.printf("[ProblemDataLoader] warning: couldn't parse createdAt='%s' for id=%s%n",
                                        createdStr, externalId);
                            }
                        }
                    }

                    // handle testcases, tags, defaults, etc. Example for tags if it's a JSON array:
                    Object tagsObj = m.get("tags");
//                    if (tagsObj instanceof List) {
//                        @SuppressWarnings("unchecked")
//                        List<String> tags = (List<String>) tagsObj;
//                        problem.setTags(tags); // implement setTags(List<String>) or convert as needed
//                    }
                    System.out.println("========================");
                    System.out.println(problem);
                    System.out.println("========================");
                    problemRepository.save(problem);
                    System.out.printf("[ProblemDataLoader] inserted problem id=%s title=\"%s\"%n",
                            problem.getId(), problem.getTitle());
                }
            } catch (Exception e) {
                System.err.println("[ProblemDataLoader] failed to load problems: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
