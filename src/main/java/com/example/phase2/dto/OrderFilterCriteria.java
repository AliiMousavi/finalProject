package com.example.phase2.dto;

import com.example.phase2.entity.SubService;
import com.example.phase2.entity.enumeration.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderFilterCriteria {
    LocalDateTime startDate;
    LocalDateTime endDate;
    OrderStatus orderStatus;
    Long subServiceId;
    Long ServiceCoId;
}
