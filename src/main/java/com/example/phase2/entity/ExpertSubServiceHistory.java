package com.example.phase2.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ExpertSubServiceHistory{
    private Expert expert;
    private List<Order> orders = new ArrayList<>();
}
