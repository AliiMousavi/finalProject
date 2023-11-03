package com.example.phase2.entity;



import com.example.phase2.entity.enumeration.ExpertStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@SuperBuilder
public class Expert extends User {
    private ExpertStatus expertStatus;
    private double credit;
    private double Score;

    @ManyToMany(mappedBy = "experts", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<SubService> subServices= new ArrayList<>();

    @OneToMany(mappedBy = "expert", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<Comment> comments= new ArrayList<>();

    @OneToMany(mappedBy = "expert" , cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<Offer> offers= new ArrayList<>();

    @OneToMany(mappedBy = "expert", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<Order> orders = new ArrayList<>();

    @Override
    public String toString() {
        return "Expert{" +
                "expertStatus=" + expertStatus +
                ", credit=" + credit +
                ", Score=" + Score +
                ", subServices=" + subServices.stream()
                .map(SubService::getName)
                .toList() +
                ", comments=" + comments.stream()
                .map(Comment::getId)
                .toList() +
                ", offers=" + offers.stream()
                .map(Offer::getId)
                .toList() +
                ", orders=" + orders.stream()
                .map(Order::getId)
                .toList() +
                '}';
    }
}
