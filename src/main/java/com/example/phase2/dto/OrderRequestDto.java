package com.example.phase2.dto;

import jakarta.validation.constraints.NotBlank;
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
public class OrderRequestDto {
    @NotBlank
    private int offerPrice;
    @NotBlank
    private String workToDo;
    @NotBlank
    private LocalDateTime DateOfExecution;
    @NotBlank
    private String address;
}
