package com.example.phase2.service.impl;

import com.example.phase2.entity.Order;
import com.example.phase2.repository.OrderRepository;
import com.example.phase2.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    private OrderServiceImpl orderService;
    private OrderServiceImpl orderServiceImpl;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        orderServiceImpl = new OrderServiceImpl(orderRepository);
        orderService = orderServiceImpl;
    }

    @Test
    public void testSaveOrUpdate() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.saveOrUpdate(order);

        Assertions.assertEquals(order, result);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testFindById_ExistingId() {
        Long id = 1L;
        Order order = new Order();
        order.setId(id);

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.findById(id);

        Assertions.assertEquals(Optional.of(order), result);
        verify(orderRepository, times(1)).findById(id);
    }

    @Test
    public void testFindById_NonExistingId() {
        Long id = 1L;

        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Order> result = orderService.findById(id);

        Assertions.assertEquals(Optional.empty(), result);
        verify(orderRepository, times(1)).findById(id);
    }

    @Test
    public void testFindAll() {
        List<Order> orders = createOrders();

        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.findAll();

        Assertions.assertEquals(orders, result);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testSaveAll() {
        List<Order> orders = createOrders();

        when(orderRepository.saveAll(orders)).thenReturn(orders);

        List<Order> result = orderService.saveAll(orders);

        Assertions.assertEquals(orders, result);
        verify(orderRepository, times(1)).saveAll(orders);
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;

        orderService.deleteById(id);

        verify(orderRepository, times(1)).deleteById(id);
    }

    private List<Order> createOrders() {
        List<Order> orders = new ArrayList<>();
        Order order1 = new Order();
        order1.setId(1L);
        orders.add(order1);
        Order order2 = new Order();
        order2.setId(2L);
        orders.add(order2);
        return orders;
    }

}