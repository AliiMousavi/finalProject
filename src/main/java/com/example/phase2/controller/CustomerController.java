package com.example.phase2.controller;

import com.example.phase2.dto.*;
import com.example.phase2.entity.*;
import com.example.phase2.entity.enumeration.OrderStatus;
import com.example.phase2.exception.*;
import com.example.phase2.mapper.CommentMapper;
import com.example.phase2.mapper.CustomerMapper;
import com.example.phase2.mapper.OfferMapper;
import com.example.phase2.mapper.OrderMapper;
import com.example.phase2.service.impl.CustomerServiceImpl;
import com.example.phase2.service.impl.SubServiceServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerServiceImpl customerService;
    private final SubServiceServiceImpl subServiceService;

    public CustomerController(CustomerServiceImpl customerService, SubServiceServiceImpl subServiceService) {
        this.customerService = customerService;
        this.subServiceService = subServiceService;
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<CustomerResponseDto> save(@RequestBody @Valid CustomerRequestDto customerRequestDto){
        Customer customer = CustomerMapper.INSTANCE.dtoToCustomer(customerRequestDto);
        Customer savedcustomer = customerService.saveOrUpdate(customer);
        CustomerResponseDto customerResponseDto = CustomerMapper.INSTANCE.customerToDto(savedcustomer);
        return new ResponseEntity<>(customerResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/find-by-id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<CustomerResponseDto> findById(@PathVariable Long id){
        Customer customer = customerService.findById(id).orElseThrow();
        CustomerResponseDto customerResponseDto = CustomerMapper.INSTANCE.customerToDto(customer);
        return ResponseEntity.ok(customerResponseDto);
    }

    @GetMapping("/find-all")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<List<CustomerResponseDto>> findAll() {
        List<Customer> customers = customerService.findAll();
        List<CustomerResponseDto> responseDtos = customers.stream()
                .map(CustomerMapper.INSTANCE::customerToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@AuthenticationPrincipal UserDetails userDetails,
                                                              @RequestBody @Valid CustomerRequestDto customerRequestDto) {
        Customer customer = customerService.findCustomerByEmail(userDetails.getUsername()).orElseThrow(
                () -> new NotFoundException("Admin not found")
        );
        Customer updatedCustomer = CustomerMapper.INSTANCE.dtoToCustomer(customerRequestDto);
        updatedCustomer.setId(customer.getId());
        Customer savedCustomer = customerService.update(updatedCustomer);
        CustomerResponseDto responseDto = CustomerMapper.INSTANCE.customerToDto(savedCustomer);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<Long> deleteCustomer(@PathVariable Long id) {
            customerService.deleteById(id);
            return ResponseEntity.ok(id);
    }

    @PutMapping("/change-password/{password}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> changePasswordById(@PathVariable String password) {
            Customer updatedCustomer = customerService.ChangePasswordByID(password);
            if (updatedCustomer != null) {
                return ResponseEntity.ok("password has changed!");
            } else {
                return ResponseEntity.notFound().build();
            }
    }
    @PostMapping("/ordering/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderResponseDto> createOrder(@PathVariable Long customerId, @RequestBody @Valid OrderRequestDto orderRequestDto) {
        Customer customer = customerService.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        SubService subService = subServiceService.findById(orderRequestDto.getSubServiceId()).orElseThrow();
        Order order = OrderMapper.INSTANCE.dtoToOrder(orderRequestDto);
        order.setSubService(subService);

        if(order.getOfferPrice() < order.getSubService().getBasePrice()){
            throw new NotValidOfferPriceException("OfferPrice should not be less than subservice BasePrice");
        }
        if(order.getDateOfExecution().isBefore(LocalDateTime.now())){
            throw new NotValidDateOfExeException("DateOfExecution can not before now!");
        }
        customer.getOrders().add(order);
        order.setCustomer(customer);
        order.setOrderStatus(OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS);
        Order createdOrder = customerService.Ordering(order);
        OrderResponseDto orderResponseDto = OrderMapper.INSTANCE.orderToDto(createdOrder);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/getOffersSortedByOfferedPrice/{customerId}/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
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
    @PreAuthorize("hasRole('CUSTOMER')")
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
    @PreAuthorize("hasRole('CUSTOMER')")
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
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> changeOrderStatusToBeginning(@PathVariable Long customerId, @PathVariable Long orderId) {
        try {
            customerService.changeOrderStatusToBeginning(customerId, orderId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/changeOrderStatusToDone/{customerId}/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> changeOrderStatusToDone(@PathVariable Long customerId, @PathVariable Long orderId) {
        try {
            customerService.changeOrderStatusToDone(customerId, orderId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/changeOrderStatusToPAID/{customerId}/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> changeOrderStatusToPAID(@PathVariable Long customerId, @PathVariable Long orderId) {
        try {
            customerService.changeOrderStatusToPAID(customerId, orderId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/paymentFromCredit/{customerId}/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> paymentFromCredit(@PathVariable Long customerId, @PathVariable Long orderId) {
            customerService.paymentFromCredit(customerId, orderId);
            return ResponseEntity.ok().build();
    }

    @PostMapping("/addComment/{customerId}/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long customerId, @PathVariable Long orderId, @RequestBody CommentRequestDto commentRequestDto) {
        Comment comment = CommentMapper.INSTANCE.dtoToComment(commentRequestDto);
        try {
            customerService.AddComment(customerId, orderId, comment);
            CommentResponseDto commentResponseDto = CommentMapper.INSTANCE.commentToDto(comment);
            return ResponseEntity.ok(commentResponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/addCommentJustScore/{customerId}/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CommentResponseDto> addCommentJustScore(@PathVariable Long customerId, @PathVariable Long orderId, @RequestBody CommentRequestDtoWithoutComment commentRequestDto) {
        Comment comment = CommentMapper.INSTANCE.justScoredtoToComment(commentRequestDto);
        try {
            customerService.AddComment(customerId, orderId, comment);
            CommentResponseDto commentResponseDto = CommentMapper.INSTANCE.commentToDto(comment);
            return ResponseEntity.ok(commentResponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/paymentPage")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> processPaymentPage(@RequestBody @Valid BankCardRequestDto bankCardRequestDto) {
        String captchaText = CaptchaController.captchaMap.remove(bankCardRequestDto.getCaptchaId());
        if(!bankCardRequestDto.getCaptcha().equalsIgnoreCase(captchaText)){
            throw new NotMachCaptchaException("captcha dose not mach!");
        }
        customerService.paymentByBankCard(bankCardRequestDto.getOrderId());
        return ResponseEntity.ok("pardakht anjam shod!");
    }

    @GetMapping("/activate")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String activateAccount(@RequestParam("token") String activationToken){
        return customerService.activateUserAccount(activationToken);
    }
    @GetMapping("/getOrdersByCustomerAndStatus/{orderStatus}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<Order>> getOrdersByCustomerAndStatus(@PathVariable OrderStatus orderStatus){
        List<Order> orders = customerService.getOrdersByCustomerAndStatus(orderStatus);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getCredit")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Double> getCredit(){
        return ResponseEntity.ok(customerService.getCredit());
    }
}
