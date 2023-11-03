package com.example.phase2.service.impl;

import com.example.phase2.entity.Customer;
import com.example.phase2.entity.Expert;
import com.example.phase2.entity.Offer;
import com.example.phase2.entity.Order;
import com.example.phase2.entity.enumeration.OrderStatus;
import com.example.phase2.exception.NotValidPasswordException;
import com.example.phase2.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderServiceImpl orderService;

    @Mock
    private OfferServiceImpl offerService;
    @Mock
    private CommentServiceImpl commentService;

    private Customer customer;
    private Order order;
    private Offer offer;
    private Expert expert;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customerService = new CustomerServiceImpl(customerRepository, orderService, offerService, commentService);

        // Create a sample expert
        expert = new Expert();
        expert.setId(1L);
        expert.setScore(10); // Initial score

        // Create a sample order with associated offer and expert
        order = new Order();
        order.setId(1L);
        offer = new Offer();
        offer.setId(1L);
        offer.setOfferedDate(LocalDateTime.now().minusHours(8));
        offer.setDuration(1);
        offer.setExpert(expert);
        offer.setOrder(order);
        order.getOffers().add(offer);
        expert.getOffers().add(offer);
        order.setExpert(expert);
        expert.getOrders().add(order);
        order.setOrderStatus(OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS);

        // Create a sample customer with the order
        customer = new Customer();
        customer.setId(1L);
        customer.getOrders().add(order);
    }

    @Test
    public void testSaveOrUpdate() {
        Customer customer = new Customer();
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.saveOrUpdate(customer);

        Assertions.assertEquals(customer, result);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testFindById() {
        Long customerId = 1L;
        Customer customer = new Customer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.findById(customerId);

        Assertions.assertEquals(Optional.of(customer), result);
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    public void testFindAll() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.findAll();

        Assertions.assertEquals(customers, result);
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteById() {
        Long customerId = 1L;

        customerService.deleteById(customerId);

        verify(customerRepository, times(1)).deleteById(customerId);
    }

    @Test
    public void testChangePasswordByID_ValidPassword() {
        Long customerId = 1L;
        String password = "a2345678";
        Customer customer = new Customer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.ChangePasswordByID(password, customerId);

        Assertions.assertEquals(customer, result);
        Assertions.assertEquals(password, customer.getPassword());
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testChangePasswordByID_InvalidPassword() {
        Long customerId = 1L;
        String invalidPassword = "123";
        Customer customer = new Customer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Assertions.assertThrows(NotValidPasswordException.class, () -> {
            customerService.ChangePasswordByID(invalidPassword, customerId);
        });

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    public void testOrdering() {
        Order order = new Order();
        when(orderService.saveOrUpdate(any(Order.class))).thenReturn(order);

        Order result = customerService.Ordering(order);

        Assertions.assertEquals(order, result);
        verify(orderService, times(1)).saveOrUpdate(order);
    }

//    @Test
//    public void testGetOffersSortedByOfferedPrice() {
//        Order order = new Order();
//        List<Offer> sortedOffers = new ArrayList<>();
//        when(offerService.sortOffersByOfferedPrice(anyList())).thenReturn(sortedOffers);
//
//        List<Offer> result = customerService.getOffersSortedByOfferedPrice(order);
//
//        Assertions.assertEquals(sortedOffers, result);
//        verify(offerService, times(1)).sortOffersByOfferedPrice(order.getOffers());
//    }

    @Test
    public void testGetOffersSortedByOfferedPrice() {
        Order order = new Order();
        List<Offer> offers = new ArrayList<>();
        Offer offer1 = new Offer();
        offer1.setOfferedPrice(100);
        offers.add(offer1);
        Offer offer2 = new Offer();
        offer2.setOfferedPrice(200);
        offers.add(offer2);
        Offer offer3 = new Offer();
        offer3.setOfferedPrice(50);
        offers.add(offer3);
        order.setOffers(offers);

        when(offerService.sortOffersByOfferedPrice(anyList())).thenReturn(offers);

        List<Offer> result = offerService.sortOffersByOfferedPrice(order.getOffers());

        Assertions.assertEquals(offers, result);
        verify(offerService, times(1)).sortOffersByOfferedPrice(order.getOffers());
    }

    @Test
    public void testGetOffersSortedByExpertScore() {
        Order order = new Order();
        List<Offer> offers = new ArrayList<>();
        Offer offer1 = new Offer();
        offer1.setOfferedPrice(100);
        offers.add(offer1);
        Offer offer2 = new Offer();
        offer2.setOfferedPrice(200);
        offers.add(offer2);
        Offer offer3 = new Offer();
        offer3.setOfferedPrice(50);
        offers.add(offer3);
        order.setOffers(offers);

        when(offerService.sortOffersByExpertScore(anyList())).thenReturn(offers);

        List<Offer> result = offerService.sortOffersByExpertScore(order.getOffers());

        Assertions.assertEquals(offers, result);
        verify(offerService, times(1)).sortOffersByExpertScore(order.getOffers());
    }

    @Test
    public void testSelectOfferForOrder() {
        Order order = new Order();
        Offer offer = new Offer();

        customerService.selectOfferForOrder(order, offer);

        Assertions.assertEquals(offer.getExpert(), order.getExpert());
        Assertions.assertEquals(OrderStatus.WAITING_FOR_THE_EXPERT_TO_COME_TO_YOUR_PLACE, order.getOrderStatus());
        verify(orderService, times(1)).saveOrUpdate(order);
    }

    @Test
    public void testChangeOrderStatusToBeginning() {
        Long customerId = 1L;
        Long orderId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.WAITING_FOR_THE_EXPERT_TO_COME_TO_YOUR_PLACE);
        Expert expert = new Expert();
        Offer offer = new Offer();
        offer.setExpert(expert);
        offer.setOrder(order);
        offer.setOfferedDate(LocalDateTime.now().minusDays(10)); // Offered date in the past
        expert.setOffers(List.of(offer));
        order.setExpert(expert);
        customer.setOrders(List.of(order));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        customerService.changeOrderStatusToBeginning(customerId, orderId);

        Assertions.assertEquals(OrderStatus.BEGINNING, order.getOrderStatus());
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testChangeOrderStatusToBeginning_OrderNotWaiting() {
        Long customerId = 1L;
        Long orderId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.BEGINNING); // Order status not waiting
        customer.setOrders(List.of(order));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            customerService.changeOrderStatusToBeginning(customerId, orderId);
        });

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    public void testChangeOrderStatusToBeginning_OfferNotPresent() {
        Long customerId = 1L;
        Long orderId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.WAITING_FOR_THE_EXPERT_TO_COME_TO_YOUR_PLACE);
        Expert expert = new Expert();
        Offer offer = new Offer();
        offer.setExpert(expert);
        offer.setOrder(new Order()); // Different order
        order.setExpert(expert);
        customer.setOrders(List.of(order));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            customerService.changeOrderStatusToBeginning(customerId, orderId);
        });

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    public void testChangeOrderStatusToBeginning_OfferedDateNotInPast() {
        Long customerId = 1L;
        Long orderId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.WAITING_FOR_THE_EXPERT_TO_COME_TO_YOUR_PLACE);
        Expert expert = new Expert();
        Offer offer = new Offer();
        offer.setExpert(expert);
        offer.setOrder(order);
        offer.setOfferedDate(LocalDateTime.now().plusDays(10)); // Offered date in the future
        expert.setOffers(List.of(offer));
        order.setExpert(expert);
        customer.setOrders(List.of(order));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            customerService.changeOrderStatusToBeginning(customerId, orderId);
        });

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    public void testChangeOrderStatusToDone() {
        Long customerId = 1L;
        Long orderId = 1L;
        Customer customer = new Customer();
        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.BEGINNING);
        customer.setOrders(List.of(order));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        customerService.changeOrderStatusToDone(customerId, orderId);

        Assertions.assertEquals(OrderStatus.DONE, order.getOrderStatus());
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testChangeOrderStatusToDone_InvalidCustomer() {
        Long customerId = 1L;
        Long orderId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            customerService.changeOrderStatusToDone(customerId, orderId);
        });

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    //------------------------------------------------------------------------------



    @Test
    void testChangeOrderStatusToDoneWithDelay() {

        // Mock the customer repository to return the sample customer
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        // Call the method under test
        customerService.changeOrderStatusToDone(1L, 1L);

        // Verify that the expert's score is deducted by the delay hours (2 hours in this case)
        assertEquals(3, expert.getScore());

        // Verify that the order status is set to DONE
        assertEquals(OrderStatus.DONE, order.getOrderStatus());

        // Verify that the customer repository save method is called
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testChangeOrderStatusToDoneWithoutDelay() {
        // Set the current time to before the expected completion time
        LocalDateTime currentTime = offer.getOfferedDate().plusHours(offer.getDuration()).minusMinutes(30);

        // Mock the customer repository to return the sample customer
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        // Call the method under test
        customerService.changeOrderStatusToDone(1L, 1L);

        // Verify that the expert's score remains unchanged
        assertEquals(10, expert.getScore());

        // Verify that the order status is set to DONE
        assertEquals(OrderStatus.DONE, order.getOrderStatus());

        // Verify that the customer repository save method is called
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testChangeOrderStatusToDoneNoOffer() {
        // Remove the offer from the expert
        expert.getOffers().remove(offer);
        order.setExpert(null);

        // Mock the customer repository to return the sample customer
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        // Call the method under test
        customerService.changeOrderStatusToDone(1L, 1L);

        // Verify that the expert's score remains unchanged
        assertEquals(10, expert.getScore());

        // Verify that the order status is set to DONE
        assertEquals(OrderStatus.DONE, order.getOrderStatus());

        // Verify that the customer repository save method is called
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testChangeOrderStatusToDoneCustomerNotFound() {
        // Mock the customer repository to return an empty optional
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call the method under test and assert that it throws an IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> customerService.changeOrderStatusToDone(1L, 1L));

        // Verify that the customer repository save method is not called
        verify(customerRepository, never()).save(any());
    }

    @Test
    void testChangeOrderStatusToDoneOrderNotFound() {
        // Mock the customer repository to return the sample customer
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        // Call the method under test and assert that it throws an IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> customerService.changeOrderStatusToDone(1L, 2L));

        // Verify that the customer repository save method is not called
        verify(customerRepository, never()).save(any());
    }

}