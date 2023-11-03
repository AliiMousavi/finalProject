package com.example.phase2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
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
