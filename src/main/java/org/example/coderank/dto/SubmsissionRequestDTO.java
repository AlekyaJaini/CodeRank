package org.example.coderank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;
@Data
public class SubmsissionRequestDTO {

    @NotNull
    private UUID problemId;

    @NotNull
    private Integer userId;

    @NotBlank
    private String language;

    @NotBlank
    private String code;

    private String stdin;
}
