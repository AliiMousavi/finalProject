package com.example.phase2.dto;

import com.example.phase2.entity.Expert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubServiceResponseDto_justExpert {
    private List<Expert> experts= new ArrayList<>();
}
