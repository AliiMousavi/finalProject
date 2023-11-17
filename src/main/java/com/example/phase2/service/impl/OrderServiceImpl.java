package com.example.phase2.service.impl;


import com.example.phase2.entity.Order;
import com.example.phase2.repository.OrderRepository;
import com.example.phase2.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public Order saveOrUpdate(Order order) {
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order update(Order order) {
        try{
            return orderRepository.save(order);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public List<Order> saveAll(List<Order> orders) {
        return orderRepository.saveAll(orders);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Order> findOrderByAcceptedOffer_OfferedPrice(int offeredPrice) {
        return orderRepository.findOrderByAcceptedOffer_OfferedPrice(offeredPrice);
    }

    @Override
    @Transactional
    public int getPaymentAmountFromOrderID(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        return order.getAcceptedOffer().getOfferedPrice();
    }
}
