package com.example.phase2.entity;


import com.example.phase2.entity.enumeration.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int offerPrice;
    private String workToDo;
    private LocalDateTime DateOfExecution;
    private String address;
    private OrderStatus orderStatus;
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private SubService subService;
    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<Offer> offers= new ArrayList<>();
    @ManyToOne
    private Expert expert;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", offerPrice=" + offerPrice +
                ", workToDo='" + workToDo + '\'' +
                ", DateOfExecution=" + DateOfExecution +
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
