package com.example.phase2.entity;


import jakarta.persistence.*;
import lombok.*;

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
@Table(name = "services")
public class ServiceCo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "serviceCo", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<SubService> subServices= new ArrayList<>();

    @Override
    public String toString() {
        return "ServiceCo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subServices=" + subServices.stream()
                .map(SubService::getId)
                .toList() +
                '}';
    }
}
