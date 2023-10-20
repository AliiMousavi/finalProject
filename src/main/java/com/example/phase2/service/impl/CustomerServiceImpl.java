package com.example.phase2.service.impl;


import com.example.phase2.entity.Customer;
import com.example.phase2.entity.Order;
import com.example.phase2.repository.CustomerRepository;
import com.example.phase2.repository.OrderRepository;
import com.example.phase2.service.CustomerService;
import com.example.phase2.validator.PasswordValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final OrderServiceImpl orderService;

    public CustomerServiceImpl(CustomerRepository customerRepository, OrderServiceImpl orderService) {
        this.customerRepository = customerRepository;
        this.orderService = orderService;
    }

    @Override
    public Customer saveOrUpdate(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
    customerRepository.deleteById(id);
    }

    @Override
    public Customer ChangePasswordByID(String password, Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        if(PasswordValidator.isValidPassword(password))
            customer.setPassword(password);
        else
            throw new RuntimeException("is not valid password!");
        return customerRepository.save(customer);
    }

    @Override
    public Order Ordering(Order order) {
        return orderService.saveOrUpdate(order);
    }
}
