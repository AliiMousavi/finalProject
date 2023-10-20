package com.example.phase2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    Date DateOfTheOffer;
    int offeredprice;
    Date offeredDate;
    int Duration;
    @OneToOne(mappedBy = "offer")
    Order order;


}
