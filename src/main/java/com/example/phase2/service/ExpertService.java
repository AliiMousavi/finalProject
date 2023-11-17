package com.example.phase2.service;


import com.example.phase2.entity.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ExpertService {
    Expert saveOrUpdate(Expert expert);
    Expert update(Expert expert);
    Optional<Expert> findById(Long id);
    List<Expert> findAll();
    List<Expert> saveAll(List<Expert> experts);
    void deleteById(Long id);
    Expert ChangePasswordByID(String password);
    List<Order> getOrdersForOffering(Expert expert);
    Offer offering(Offer offer);
    List<Integer> getScoreOfComments(Long ExpertId);
    Optional<Expert> findExpertByEmail(String email);
    Optional<Expert> findExpertByActivationToken(String activationToken);
}
