package com.example.phase2.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime DateOfOfferRegister;
    private int offeredPrice;
    private LocalDateTime offeredDate;
    private int duration;
    @ManyToOne
    private Order order;
    @ManyToOne
    Expert expert;

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", DateOfOfferRegister=" + DateOfOfferRegister +
                ", offeredPrice=" + offeredPrice +
                ", offeredDate=" + offeredDate +
                ", duration=" + duration +
                ", order=" + order.getId() +
                ", expert=" + expert.getFirstName() +
                '}';
    }
}
