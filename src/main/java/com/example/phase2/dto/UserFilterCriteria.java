package com.example.phase2.dto;

import com.example.phase2.entity.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFilterCriteria {
    private Role role;
    private String firstName;
    private String lastName;
    private String email;
    private Long expertSubServiceId;
    private boolean highestScore;
    private boolean lowestScore;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
