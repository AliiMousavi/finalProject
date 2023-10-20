package com.example.phase2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "sub_services")
public class SubService {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    String name;
    @ManyToOne
    ServiceCo service;
    int basePrice;
    String caption;
    @ManyToMany
    List<Expert> Experts;
    @OneToOne(mappedBy = "subServiceCollection")
    Order order;

    public SubService(String name, int basePrice, String caption) {
        this.name = name;
        this.basePrice = basePrice;
        this.caption = caption;
    }

    @Override
    public String toString() {
        return "SubServiceCollection{" +
                "name='" + name + '\'' +
                ", service=" + service.getName() +
                ", basePrice=" + basePrice +
                ", caption='" + caption + '\'' +
                ", Experts=" + Experts +
                ", order=" + order +
                '}';
    }

    public SubService(String name, ServiceCo service, int basePrice, String caption) {
        this.name = name;
        this.service = service;
        this.basePrice = basePrice;
        this.caption = caption;
    }
}
