package com.example.phase2.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CustomerSubServiceHistory {
    private Customer customer;
    private List<Order> orders = new ArrayList<>();

    @Override
    public String toString() {
        return orders.toString();
    }
}
