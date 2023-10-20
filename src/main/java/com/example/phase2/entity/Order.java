package com.example.phase2.entity;


import com.example.phase2.entity.enumeration.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    int offerPrice;
    String workToDo;
    Date DateOfExecution;
    String address;
    OrderStatus orderStatus;
    @OneToOne
    Customer customer;
    @OneToOne
    SubService subServiceCollection;
    @OneToOne
    Offer offer;

    public Order(int offerPrice, String workToDo, Date dateOfExecution, String address, SubService subService) {
        if (offerPrice > subService.getBasePrice())
            this.offerPrice = offerPrice;
        else
            throw new RuntimeException("offer PRice should bigger than basePrice");

        this.workToDo = workToDo;
        DateOfExecution = dateOfExecution;
        this.address = address;
        this.orderStatus = OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS;
        this.subServiceCollection = subService;
    }
}
