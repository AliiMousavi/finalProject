package com.example.phase2.dto;


import com.example.phase2.entity.SubService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    @NotNull
    private int offerPrice;
    @NotBlank
    private String workToDo;

    private LocalDateTime dateOfExecution;
    @NotBlank
    private String address;

    private Long subServiceId;
}
