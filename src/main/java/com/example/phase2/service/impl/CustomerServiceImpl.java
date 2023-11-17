package com.example.phase2.service.impl;


import com.example.phase2.dto.OrderFilterCriteria;
import com.example.phase2.entity.*;
import com.example.phase2.entity.enumeration.OrderStatus;
import com.example.phase2.entity.enumeration.Role;
import com.example.phase2.exception.*;
import com.example.phase2.repository.CustomerRepository;
import com.example.phase2.repository.OrderRepository;
import com.example.phase2.repository.OrderSpecifications;
import com.example.phase2.service.CustomerService;
import com.example.phase2.validator.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final OrderServiceImpl orderService;
    private final OfferServiceImpl offerService;
    private final CommentServiceImpl commentService;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final OrderRepository orderRepository;



    @Override
    @Transactional
    public Customer saveOrUpdate(Customer customer) {
        try{
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            customer.setRole(Role.ROLE_CUSTOMER);
            customer.setDateOfRegistration(LocalDateTime.now());
            customer.setCredit(0);

            customer.setIsEnabled(false);
            String activationToken = generateActivationToken();
            customer.setActivationToken(activationToken);
            sendActivationEmail(customer);


            return customerRepository.save(customer);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Customer update(Customer customer) {
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
    public Customer ChangePasswordByID(String password) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findCustomerByEmail(email).orElseThrow(
                () -> new NotFoundException("customer not found"));
        if(PasswordValidator.isValidPassword(password))
            customer.setPassword(passwordEncoder.encode(password));
        else
            throw new NotValidPasswordException("is not valid password!");
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Order Ordering(Order order) {
        return orderService.update(order);
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
        order.setAcceptedOffer(offer);
        order.setExpert(offer.getExpert());
        order.setOrderStatus(OrderStatus.WAITING_FOR_THE_EXPERT_TO_COME_TO_YOUR_PLACE);
        orderService.update(order);
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
                .filter(o -> o.getOrder().getId().equals(order.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Order not found"))
                .getOfferedDate()
                .isAfter(LocalDateTime.now())) {
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
            if(expert.getScore() < 0)
                expert.setIsEnabled(false);
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
    public void paymentByBankCard(Long orderId) {
        Order order = orderService.findById(orderId).orElseThrow();
        Customer customer = order.getCustomer();
        Offer offer = order.getAcceptedOffer();

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
            throw new NotFinishedWorkException("Expert work is not finish.");
        }
    }

    @Override
    @Transactional
    public Optional<Customer> findCustomerByEmail(String email) {
        return customerRepository.findCustomerByEmail(email);
    }

    @Override
    @Transactional
    public Optional<Customer> findCustomerByActivationToken(String activationToken) {
        return customerRepository.findCustomerByActivationToken(activationToken);
    }


    private String generateActivationToken() {
        return UUID.randomUUID().toString();
    }

    private void sendActivationEmail(Customer customer) {
        String activationLink = "http://localhost:8080/customer/activate?token=" + customer.getActivationToken();

        String subject = "Activate your account";
        String message = "Dear " + customer.getFirstName() + ",\n\n"
                + "Please click on the following link to activate your account:\n"
                + activationLink;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(customer.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);
    }

    @Transactional
    public String activateUserAccount(String activationToken) {
        Customer customer = customerRepository.findCustomerByActivationToken(activationToken).orElseThrow(
                () -> new NotFoundException("customer not found")
        );

        if(customer.isActivationTokenExpired()){
            throw new ActivationTokenExpiredException("link is expired.");
        }

        if (customer.getIsEnabled().equals(false)) {

            customer.setIsEnabled(true);
            customer.setActivationTokenExpired(true);

            customerRepository.save(customer);
            // Optionally, you can handle additional logic here, such as redirecting the user to a success page.
        }
        return "Account activated successfully!";
    }

    @Transactional
    public List<Order> getOrdersByCustomerAndStatus(OrderStatus orderStatus) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findCustomerByEmail(email).orElseThrow(
                () -> new NotFoundException("customer not found"));
        Specification<Order> spec = Specification.where(OrderSpecifications.byOrderStatus(orderStatus));
        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("customer"), customer));
        return orderRepository.findAll(spec);
    }

    public Double getCredit(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findCustomerByEmail(email).orElseThrow(
                () -> new NotFoundException("customer not found"));

        return customer.getCredit();
    }

}
