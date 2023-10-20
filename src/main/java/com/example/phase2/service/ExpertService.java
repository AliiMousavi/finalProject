package com.example.phase2.service;


import com.example.phase2.entity.Customer;
import com.example.phase2.entity.Expert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ExpertService {
    Expert saveOrUpdate(Expert expert);
    Optional<Expert> findById(Long id);
    List<Expert> findAll();
    List<Expert> saveAll(List<Expert> experts);
    void deleteById(Long id);
    Expert ChangePasswordByID(String password, Long id);
}
