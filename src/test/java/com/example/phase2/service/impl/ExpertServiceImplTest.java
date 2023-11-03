package com.example.phase2.service.impl;

import com.example.phase2.entity.*;
import com.example.phase2.entity.enumeration.OrderStatus;
import com.example.phase2.exception.NotValidPasswordException;
import com.example.phase2.repository.ExpertRepository;
import com.example.phase2.service.ExpertService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mock;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ExpertServiceImplTest {
    private ExpertServiceImpl expertService;
    private ExpertServiceImpl expertServiceImpl;

    @Mock
    private ExpertRepository expertRepository;

    @Mock
    private OfferServiceImpl offerService;
    @Mock
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        expertServiceImpl = new ExpertServiceImpl(expertRepository, offerService);
        expertService = expertServiceImpl;
    }

    @Test
    public void testSaveOrUpdate() {
        Expert expert = new Expert();
        expert.setId(1L);
        expert.setFirstName("John");

        when(expertRepository.save(expert)).thenReturn(expert);

        Expert result = expertService.saveOrUpdate(expert);

        Assertions.assertEquals(expert, result);
        verify(expertRepository, times(1)).save(expert);
    }

    @Test
    public void testFindById_ExistingId() {
        Long id = 1L;
        Expert expert = new Expert();
        expert.setId(id);
        expert.setFirstName("John");

        when(expertRepository.findById(id)).thenReturn(Optional.of(expert));

        Optional<Expert> result = expertService.findById(id);

        Assertions.assertEquals(Optional.of(expert), result);
        verify(expertRepository, times(1)).findById(id);
    }

    @Test
    public void testFindById_NonExistingId() {
        Long id = 1L;

        when(expertRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Expert> result = expertService.findById(id);

        Assertions.assertEquals(Optional.empty(), result);
        verify(expertRepository, times(1)).findById(id);
    }

    @Test
    public void testFindAll() {
        List<Expert> experts = createExperts();

        when(expertRepository.findAll()).thenReturn(experts);

        List<Expert> result = expertService.findAll();

        Assertions.assertEquals(experts, result);
        verify(expertRepository, times(1)).findAll();
    }

    @Test
    public void testSaveAll() {
        List<Expert> experts = createExperts();

        when(expertRepository.saveAll(experts)).thenReturn(experts);

        List<Expert> result = expertService.saveAll(experts);

        Assertions.assertEquals(experts, result);
        verify(expertRepository, times(1)).saveAll(experts);
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;

        expertService.deleteById(id);

        verify(expertRepository, times(1)).deleteById(id);
    }

    @Test
    public void testChangePasswordByID_ValidPassword() {
        Long expertId = 1L;
        String password = "a2345678";
        Expert expert = new Expert();
        when(expertRepository.findById(expertId)).thenReturn(Optional.of(expert));
        when(expertRepository.save(any(Expert.class))).thenReturn(expert);

        Expert result = expertService.ChangePasswordByID(password, expertId);

        Assertions.assertEquals(expert, result);
        Assertions.assertEquals(password, expert.getPassword());
        verify(expertRepository, times(1)).findById(expertId);
        verify(expertRepository, times(1)).save(expert);
    }

    @Test
    public void testChangePasswordByID_InvalidPassword() {
        Long id = 1L;
        String password = "weak";

        Expert expert = new Expert();
        expert.setId(id);
        expert.setFirstName("John");

        when(expertRepository.findById(id)).thenReturn(Optional.of(expert));

        Assertions.assertThrows(NotValidPasswordException.class, () -> {
            expertService.ChangePasswordByID(password, id);
        });

        verify(expertRepository, times(1)).findById(id);
        verify(expertRepository, never()).save(expert);
    }

    @Test
    public void testGetOrdersForOffering() {
        Expert expert = new Expert();
        expert.setId(1L);
        expert.setFirstName("John");

        SubService subService1 = new SubService();
        subService1.setId(1L);

        SubService subService2 = new SubService();
        subService2.setId(2L);

        Order order1 = new Order();
        order1.setId(1L);
        order1.setOrderStatus(OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS);
        order1.setExpert(expert);
        subService1.getOrders().add(order1);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setOrderStatus(OrderStatus.WAITING_FOR_SELECT_EXPERT);
        order2.setExpert(expert);
        subService2.getOrders().add(order2);

        expert.getSubServices().add(subService1);
        expert.getSubServices().add(subService2);

        List<Order> expectedOrders = new ArrayList<>();
        expectedOrders.add(order1);
        expectedOrders.add(order2);

        List<Order> result = expertService.getOrdersForOffering(expert);

        Assertions.assertEquals(expectedOrders.size(), result.size());
        Assertions.assertTrue(result.contains(order1));
        Assertions.assertTrue(result.contains(order2));
    }

    @Test
    public void testOffering() {
        Offer offer = new Offer();
        offer.setId(1L);
        offer.setOrder(new Order());
        offer.getOrder().setId(1L);
        offer.getOrder().setOrderStatus(OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS);
        offer.setOfferedPrice(12);
        offer.getOrder().setSubService(new SubService());
        offer.getOrder().getSubService().setBasePrice(10);
        offer.setOfferedDate(LocalDateTime.now().plusDays(1));
        when(offerService.saveOrUpdate(offer)).thenReturn(offer);

        Offer result = expertService.offering(offer);

        Assertions.assertEquals(OrderStatus.WAITING_FOR_SELECT_EXPERT, result.getOrder().getOrderStatus());
        verify(offerService, times(1)).saveOrUpdate(offer);
    }

//    @Test
//    public void testOffering_ValidOffer() {
//        // Create a mock Offer
//        Offer offer = mock(Offer.class);
//
//        // Create a mock Order
//        Order order = mock(Order.class);
//        SubService subService = new SubService();
//        subService.setBasePrice(100);
//        when(order.getSubService()).thenReturn(subService);
//
//        // Set up the mock Offer and Order for the test case
//        when(offer.getOrder()).thenReturn(order);
//        when(offer.getOfferedPrice()).thenReturn(120);
//        when(offer.getOfferedDate()).thenReturn(Date.valueOf(LocalDate.now().plusDays(1)));
//
//        // Set up the mock behavior for the OrderService
//        doAnswer((Answer<Order>) invocation -> {
//            Order updatedOrder = invocation.getArgument(0);
//            updatedOrder.setOrderStatus(OrderStatus.WAITING_FOR_SELECT_EXPERT);
//            return updatedOrder;
//        }).when(orderService).saveOrUpdate(any(Order.class));
//
//        // Call the offering method
//        Offer result = offerService.offering(offer);
//
//        // Verify that the order status was updated and the offer was saved
//        verify(order, times(1)).setOrderStatus(OrderStatus.WAITING_FOR_SELECT_EXPERT);
//        verify(orderService, times(1)).saveOrUpdate(order);
//        verify(offerService, times(1)).saveOrUpdate(offer);
//
//        // Verify that the returned offer is the same as the input offer
//        Assertions.assertEquals(offer, result);
//    }
//
//    @Test
//    public void testOffering_InvalidOffer() {
//        // Create a mock Offer
//        Offer offer = mock(Offer.class);
//
//        // Create a mock Order
//        Order order = mock(Order.class);
//        SubService subService = new SubService();
//        subService.setBasePrice(100);
//        when(order.getSubService()).thenReturn(subService);
//
//        // Set up the mock Offer and Order for the test case
//        when(offer.getOrder()).thenReturn(order);
//        when(offer.getOfferedPrice()).thenReturn(80);
//        when(offer.getOfferedDate()).thenReturn(Date.valueOf(LocalDate.now()));
//
//        // Call the offering method and assert that it throws an IllegalArgumentException
//        Assertions.assertThrows(IllegalArgumentException.class, () -> offerService.offering(offer));
//
//        // Verify that the order status was not updated and no offer was saved
//        verify(order, never()).setOrderStatus(any(OrderStatus.class));
//        verify(orderService, never()).saveOrUpdate(any(Order.class));
//        verify(offerService, never()).saveOrUpdate(any(Offer.class));
//    }

    private List<Expert> createExperts() {
        List<Expert> experts = new ArrayList<>();
        Expert expert1 = new Expert();
        expert1.setId(1L);
        expert1.setFirstName("John");
        experts.add(expert1);
        Expert expert2 = new Expert();
        expert2.setId(2L);
        expert2.setFirstName("Jane");
        experts.add(expert2);
        return experts;
    }
}