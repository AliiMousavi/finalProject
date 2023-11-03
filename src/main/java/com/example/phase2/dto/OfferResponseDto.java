package com.example.phase2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfferResponseDto {
    @NotBlank
    private Long id;
    @NotBlank
    private LocalDateTime DateOfOfferRegister;
    @NotBlank
    private int offeredPrice;
    @NotBlank
    private LocalDateTime offeredDate;
    @NotNull
    private int Duration;
}
