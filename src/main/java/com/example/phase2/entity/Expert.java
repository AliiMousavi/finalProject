package com.example.phase2.entity;



import com.example.phase2.entity.enumeration.ExpertStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
public class Expert extends User {
    ExpertStatus expertStatus;

    @ManyToMany(mappedBy = "Experts" , cascade = CascadeType.PERSIST)
    List<SubService> subServiceCollections;

    @OneToMany(mappedBy = "expert")
    List<Comment> comments;

//    public Expert(String firstName, String lastName, String email) {
//        super(firstName, lastName, email);
//        this.expertStatus = ExpertStatus.NEW;
//    }
//
//    public Expert(String firstName, String lastName, String email, byte[] image) throws IOException {
//        super(firstName, lastName, email, image);
//    }

    @Override
    public String toString() {
        return id + "." +
                firstName + " " + lastName + "\t" +expertStatus +
                '}';
    }
}
