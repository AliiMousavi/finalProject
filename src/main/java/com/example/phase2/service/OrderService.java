package com.example.phase2.service;



import com.example.phase2.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderService {
    Order saveOrUpdate(Order order);
    Optional<Order> findById(Long id);
    List<Order> findAll();
    List<Order> saveAll(List<Order> orders);
    void deleteById(Long id);
}
