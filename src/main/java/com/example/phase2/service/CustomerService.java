package com.example.phase2.service;


import com.example.phase2.entity.Comment;
import com.example.phase2.entity.Customer;
import com.example.phase2.entity.Offer;
import com.example.phase2.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface CustomerService {
    Customer saveOrUpdate(Customer customer);
    Customer update(Customer customer);
    Optional<Customer> findById(Long id);
    List<Customer> findAll();
    void deleteById(Long id);
    Customer ChangePasswordByID(String password);
    Order Ordering(Order order);
    List<Offer> getOffersSortedByOfferedPrice(Order order);
    List<Offer> getOffersSortedByExpertScore(Order order);
    void selectOfferForOrder(Order order, Offer offer);
    void changeOrderStatusToBeginning(Long customerId, Long orderId);
    void changeOrderStatusToDone(Long customerId, Long orderId);
    void changeOrderStatusToPAID(Long customerId, Long orderId);
    void paymentFromCredit(Long customerId, Long orderId);
    void paymentByBankCard(Long orderId);
    void AddComment(Long customerId, Long orderId, Comment comment);
    Optional<Customer> findCustomerByEmail(String email);
    Optional<Customer> findCustomerByActivationToken(String activationToken);
}
