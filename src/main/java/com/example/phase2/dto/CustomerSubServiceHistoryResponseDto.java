package com.example.phase2.dto;

import com.example.phase2.entity.Customer;
import com.example.phase2.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSubServiceHistoryResponseDto {
    private Customer customer;
    private List<Order> orders = new ArrayList<>();
}
