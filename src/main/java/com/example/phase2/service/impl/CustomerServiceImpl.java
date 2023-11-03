package com.example.phase2.service.impl;


import com.example.phase2.entity.*;
import com.example.phase2.entity.enumeration.OrderStatus;
import com.example.phase2.exception.InsufficientCreditException;
import com.example.phase2.exception.NotValidPasswordException;
import com.example.phase2.repository.CustomerRepository;
import com.example.phase2.service.CustomerService;
import com.example.phase2.validator.PasswordValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final OrderServiceImpl orderService;
    private final OfferServiceImpl offerService;
    private final CommentServiceImpl commentService;

    public CustomerServiceImpl(CustomerRepository customerRepository, OrderServiceImpl orderService, OfferServiceImpl offerService, CommentServiceImpl commentService) {
        this.customerRepository = customerRepository;
        this.orderService = orderService;
        this.offerService = offerService;
        this.commentService = commentService;
    }

    @Override
    @Transactional
    public Customer saveOrUpdate(Customer customer) {
        try{
        return customerRepository.save(customer);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
    customerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Customer ChangePasswordByID(String password, Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        if(PasswordValidator.isValidPassword(password))
            customer.setPassword(password);
        else
            throw new NotValidPasswordException("is not valid password!");
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Order Ordering(Order order) {
        return orderService.saveOrUpdate(order);
    }

    @Override
    @Transactional
    public List<Offer> getOffersSortedByOfferedPrice(Order order) {
        return offerService.sortOffersByOfferedPrice(order.getOffers());
    }

    @Override
    @Transactional
    public List<Offer> getOffersSortedByExpertScore(Order order) {
        return offerService.sortOffersByExpertScore(order.getOffers());
    }

    @Override
    @Transactional
    public void selectOfferForOrder(Order order, Offer offer) {
        order.setExpert(offer.getExpert());
        order.setOrderStatus(OrderStatus.WAITING_FOR_THE_EXPERT_TO_COME_TO_YOUR_PLACE);
        orderService.saveOrUpdate(order);
    }

    @Override
    @Transactional
    public void changeOrderStatusToBeginning(Long customerId, Long orderId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Order order = customer.getOrders().stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.getOrderStatus() == OrderStatus.WAITING_FOR_THE_EXPERT_TO_COME_TO_YOUR_PLACE
                && order.getExpert().getOffers().stream()
                .filter(o -> o.getOrder().equals(order))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Order not found"))
                .getOfferedDate()
                .isBefore(LocalDateTime.now())) {
            order.setOrderStatus(OrderStatus.BEGINNING);
            customerRepository.save(customer);
        }else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    @Transactional
    public void changeOrderStatusToDone(Long customerId, Long orderId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Order order = customer.getOrders().stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        Offer offer = order.getExpert().getOffers().stream()
                .filter(o -> o.getOrder().equals(order))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("offer not found"));

        LocalDateTime offerCompletionTime = offer.getOfferedDate().plusHours(offer.getDuration());

        if (LocalDateTime.now().isAfter(offerCompletionTime)) {
            long delayHours = ChronoUnit.HOURS.between(offerCompletionTime, LocalDateTime.now());

            Expert expert = order.getExpert();
            int pointsToDeduct = (int) delayHours;
            expert.setScore(expert.getScore() - pointsToDeduct);
        }

        order.setOrderStatus(OrderStatus.DONE);
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void changeOrderStatusToPAID(Long customerId, Long orderId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Order order = customer.getOrders().stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.setOrderStatus(OrderStatus.DONE);
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void paymentFromCredit(Long customerId, Long orderId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Order order = customer.getOrders().stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        Offer offer = order.getExpert().getOffers().stream()
                .filter(o -> o.getOrder().equals(order))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("offer not found"));

        if(customer.getCredit() < offer.getOfferedPrice()){
            throw new InsufficientCreditException("credit is not enough.");
        }
        customer.setCredit(customer.getCredit() - offer.getOfferedPrice());
        offer.getExpert().setCredit(offer.getOfferedPrice() * 0.7);
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void paymentByBankCard(Long customerId, Long orderId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Order order = customer.getOrders().stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        Offer offer = order.getExpert().getOffers().stream()
                .filter(o -> o.getOrder().equals(order))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("offer not found"));

        offer.getExpert().setCredit(offer.getOfferedPrice() * 0.7);
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void AddComment(Long customerId, Long orderId, Comment comment) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Order order = customer.getOrders().stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if(order.getOrderStatus().equals(OrderStatus.DONE) || order.getOrderStatus().equals(OrderStatus.PAID)){
            order.getExpert().getComments().add(comment);
            customerRepository.save(customer);
        }else{
            throw new RuntimeException("Expert work is not finish.");
        }
    }
}
