package com.example.phase2.service;


import com.example.phase2.entity.Customer;
import com.example.phase2.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface CustomerService {
    Customer saveOrUpdate(Customer customer);
    Optional<Customer> findById(Long id);
    List<Customer> findAll();
    void deleteById(Long id);
    Customer ChangePasswordByID(String password,Long id);
    Order Ordering(Order order);
}
