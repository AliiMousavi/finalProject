package com.example.phase2.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubServiceResponseDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private int basePrice;
    @NotBlank
    private String caption;
}
