package com.example.phase2.controller;

import com.example.phase2.dto.*;
import com.example.phase2.entity.Customer;
import com.example.phase2.entity.Offer;
import com.example.phase2.entity.Order;
import com.example.phase2.exception.InsufficientCreditException;
import com.example.phase2.exception.NotValidPasswordException;
import com.example.phase2.mapper.CustomerMapper;
import com.example.phase2.mapper.OfferMapper;
import com.example.phase2.mapper.OrderMapper;
import com.example.phase2.service.impl.CustomerServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerServiceImpl customerService;

    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/save")
    public ResponseEntity<CustomerResponseDto> save(@RequestBody @Valid CustomerRequestDto customerRequestDto){
        Customer customer = CustomerMapper.INSTANCE.dtoToCustomer(customerRequestDto);
        Customer savedcustomer = customerService.saveOrUpdate(customer);
        CustomerResponseDto customerResponseDto = CustomerMapper.INSTANCE.customerToDto(savedcustomer);
        return new ResponseEntity<>(customerResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<CustomerResponseDto> findById(@PathVariable Long id){
        Customer customer = customerService.findById(id).orElseThrow();
        CustomerResponseDto customerResponseDto = CustomerMapper.INSTANCE.customerToDto(customer);
        return new ResponseEntity<>(customerResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<CustomerResponseDto>> findAll() {
        List<Customer> customers = customerService.findAll();
        List<CustomerResponseDto> responseDtos = customers.stream()
                .map(CustomerMapper.INSTANCE::customerToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@PathVariable Long id,
                                                              @RequestBody @Valid CustomerRequestDto customerRequestDto) {
        Optional<Customer> optionalCustomer = customerService.findById(id);
        if (optionalCustomer.isPresent()) {
            Customer existingCustomer = optionalCustomer.get();
            Customer updatedCustomer = CustomerMapper.INSTANCE.dtoToCustomer(customerRequestDto);
            updatedCustomer.setId(existingCustomer.getId());
            Customer savedCustomer = customerService.saveOrUpdate(updatedCustomer);
            CustomerResponseDto responseDto = CustomerMapper.INSTANCE.customerToDto(savedCustomer);
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> deleteCustomer(@PathVariable Long id) {
            customerService.deleteById(id);
            return ResponseEntity.ok(id);
    }

    @PutMapping("/change-password/{id}/{password}")
    public ResponseEntity<String> changePasswordById(@PathVariable Long id, @PathVariable String password) {
        try {
            Customer updatedCustomer = customerService.ChangePasswordByID(password, id);
            if (updatedCustomer != null) {
                return ResponseEntity.ok("password has changed!");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NotValidPasswordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/ordering/{customerId}")
    public ResponseEntity<OrderResponseDto> createOrder(@PathVariable Long customerId, @RequestBody @Valid OrderRequestDto orderRequestDto) {
        Customer customer = customerService.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        Order order = OrderMapper.INSTANCE.dtoToOrder(orderRequestDto);
        order.setCustomer(customer);
        Order createdOrder = customerService.Ordering(order);
        OrderResponseDto orderResponseDto = OrderMapper.INSTANCE.orderToDto(createdOrder);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/getOffersSortedByOfferedPrice/{customerId}/{orderId}")
    public ResponseEntity<List<OfferResponseDto>> getOffersSortedByOfferedPrice(@PathVariable Long customerId, @PathVariable Long orderId) {
        Customer customer = customerService.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        Order order = customer.getOrders().stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        List<OfferResponseDto> sortedOffers = customerService.getOffersSortedByOfferedPrice(order).stream()
                .map(OfferMapper.INSTANCE::offerToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sortedOffers);
    }

    @GetMapping("/getOffersSortedByExpertScore/{customerId}/{orderId}")
    public ResponseEntity<List<OfferResponseDto>> getOffersSortedByExpertScore(@PathVariable Long customerId, @PathVariable Long orderId)  {
        Customer customer = customerService.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        Order order = customer.getOrders().stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        List<OfferResponseDto> sortedOffers = customerService.getOffersSortedByExpertScore(order).stream()
                .map(OfferMapper.INSTANCE::offerToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sortedOffers);
    }

    @PostMapping("/selectOffer/{customerId}/{orderId}/{offerId}")
    public ResponseEntity<OfferResponseDto> selectOfferForOrder(@PathVariable Long customerId, @PathVariable Long orderId, @PathVariable Long offerId) {
        Customer customer = customerService.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        Order order = customer.getOrders().stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        Offer offer = order.getOffers().stream()
                .filter(o -> o.getId().equals(offerId))
                .findFirst()
                .orElseThrow();
        customerService.selectOfferForOrder(order, offer);
        OfferResponseDto offerResponseDto = OfferMapper.INSTANCE.offerToDto(offer);
        return new ResponseEntity<>(offerResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/changeOrderStatusToBeginning/{customerId}/{orderId}")
    public ResponseEntity<Void> changeOrderStatusToBeginning(@PathVariable Long customerId, @PathVariable Long orderId) {
        try {
            customerService.changeOrderStatusToBeginning(customerId, orderId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/changeOrderStatusToDone/{customerId}/{orderId}")
    public ResponseEntity<Void> changeOrderStatusToDone(@PathVariable Long customerId, @PathVariable Long orderId) {
        try {
            customerService.changeOrderStatusToDone(customerId, orderId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/changeOrderStatusToPAID/{customerId}/{orderId}")
    public ResponseEntity<Void> changeOrderStatusToPAID(@PathVariable Long customerId, @PathVariable Long orderId) {
        try {
            customerService.changeOrderStatusToPAID(customerId, orderId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/paymentFromCredit/{customerId}/{orderId}")
    public ResponseEntity<Void> paymentFromCredit(@PathVariable Long customerId, @PathVariable Long orderId) {
        try {
            customerService.paymentFromCredit(customerId, orderId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (InsufficientCreditException e) {
            return ResponseEntity.badRequest().build();
        }
    }

//    @PostMapping("/paymentPage")
//    public void processPaymentPage(@RequestBody @Valid BankCardRequestDto bankCardRequestDto) {
////        customerService.paymentByBankCard();
////        System.out.println(bankCardRequestDto);
////        System.out.println(bankCardRequestDto);
////        System.out.println(bankCardRequestDto);
////        System.out.println(bankCardRequestDto);
////        System.out.println(bankCardRequestDto);
////        System.out.println(bankCardRequestDto);
////        System.out.println(bankCardRequestDto);
//    }
//
//    @PostMapping("/oldPaymentPage")
//    public void processPaymentPageHtmlAfshar(@RequestBody @Valid BankCardRequestDto bankCardRequestDto) {
//        System.out.println(bankCardRequestDto);
//        System.out.println(bankCardRequestDto);
//        System.out.println(bankCardRequestDto);
//        System.out.println(bankCardRequestDto);
//        System.out.println(bankCardRequestDto);
//        System.out.println(bankCardRequestDto);
//        System.out.println(bankCardRequestDto);
//    }

}
