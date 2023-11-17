package com.example.phase2.entity;


import com.example.phase2.entity.enumeration.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int offerPrice;
    private String workToDo;
    private LocalDateTime dateOfExecution;
    private String address;
    private OrderStatus orderStatus;
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private SubService subService;
    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<Offer> offers= new ArrayList<>();
    @OneToOne(mappedBy = "orderr", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Offer acceptedOffer;
    @ManyToOne
    private Expert expert;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", offerPrice=" + offerPrice +
                ", workToDo='" + workToDo + '\'' +
                ", DateOfExecution=" + dateOfExecution +
                ", address='" + address + '\'' +
                ", orderStatus=" + orderStatus +
                ", customer=" + customer.getFirstName() +
                ", subService=" + subService.getName() +
                ", offers=" + offers.stream()
                .map(Offer::getId)
                .toList() +
                ", expert=" + expert.getFirstName() +
                '}';
    }
}
