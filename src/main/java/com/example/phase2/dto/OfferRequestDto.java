package com.example.phase2.dto;

import com.example.phase2.entity.Order;
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
public class OfferRequestDto {
    @NotNull
    private int offeredPrice;

    private LocalDateTime offeredDate;
    @NotNull
    private int Duration;
    private Long orderId;
    private Long expertId;
}
