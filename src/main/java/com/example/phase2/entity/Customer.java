package com.example.phase2.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Customer extends User{
    private double credit;
    @OneToMany(mappedBy = "customer", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<Order> orders= new ArrayList<>();

    @Override
    public String toString() {
        return "Customer{" +
                "credit=" + credit +
                ", orders=" + orders.stream()
                .map(Order::getId)
                .toList() +
                '}';
    }
}
