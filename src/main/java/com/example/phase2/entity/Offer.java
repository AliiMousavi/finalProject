package com.example.phase2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime dateOfOfferRegister;
    private int offeredPrice;
    private LocalDateTime offeredDate;
    private int duration;
    @ManyToOne
    private Order order;
    @OneToOne
    private Order orderr;
    @ManyToOne
    private Expert expert;

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", DateOfOfferRegister=" + dateOfOfferRegister +
                ", offeredPrice=" + offeredPrice +
                ", offeredDate=" + offeredDate +
                ", duration=" + duration +
                ", order=" + order.getId() +
                ", expert=" + expert.getFirstName() +
                '}';
    }
}
