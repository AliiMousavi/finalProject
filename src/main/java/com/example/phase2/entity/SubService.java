package com.example.phase2.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
@Table(name = "sub_services")
public class SubService {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    private int basePrice;
    private String caption;
    @ManyToOne
    private ServiceCo serviceCo;
    @ManyToMany
    private List<Expert> experts= new ArrayList<>();
    @OneToMany(mappedBy = "subService", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<Order> orders= new ArrayList<>();

    @Override
    public String toString() {
        return "SubService{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", basePrice=" + basePrice +
                ", caption='" + caption + '\'' +
                ", serviceCo=" + serviceCo.getName() +
                ", experts=" + experts.stream()
                .map(Expert::getFirstName)
                .toList() +
                ", orders=" + orders.stream()
                .map(Order::getId)
                .toList() +
                '}';
    }
}
