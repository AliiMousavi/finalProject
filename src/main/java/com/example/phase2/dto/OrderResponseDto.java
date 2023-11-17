package com.example.phase2.dto;

import com.example.phase2.entity.enumeration.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class OrderResponseDto {
    @Positive
    private Long id;
    @NotNull
    private int offerPrice;
    @NotBlank
    private String workToDo;

    private LocalDateTime dateOfExecution;
    @NotBlank
    private String address;
    @NotBlank
    private OrderStatus orderStatus;
}
