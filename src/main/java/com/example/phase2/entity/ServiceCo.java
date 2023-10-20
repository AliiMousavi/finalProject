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
@Table(name = "services")
public class ServiceCo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    String name;
    @OneToMany(mappedBy = "service")
    List<SubService> subServices;

    public ServiceCo(String name) {
        this.name = name;
    }
}
