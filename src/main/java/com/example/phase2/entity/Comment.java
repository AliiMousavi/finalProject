package com.example.phase2.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int score;
    private String Comment;
    @ManyToOne
    private Expert expert;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", score=" + score +
                ", Comment='" + Comment + '\'' +
                ", expert=" + expert.getFirstName() +
                '}';
    }
}
