package com.example.phase2.service.impl;


import com.example.phase2.dto.UserFilterCriteria;
import com.example.phase2.entity.*;
import com.example.phase2.entity.enumeration.ExpertStatus;
import com.example.phase2.entity.enumeration.OrderStatus;
import com.example.phase2.entity.enumeration.Role;
import com.example.phase2.exception.ActivationTokenExpiredException;
import com.example.phase2.exception.NotFoundException;
import com.example.phase2.exception.NotValidPasswordException;
import com.example.phase2.repository.ExpertRepository;
import com.example.phase2.repository.ExpertSpecifications;
import com.example.phase2.repository.OrderRepository;
import com.example.phase2.repository.OrderSpecifications;
import com.example.phase2.service.ExpertService;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.sql.Time.valueOf;

@Service
@RequiredArgsConstructor
public class ExpertServiceImpl implements ExpertService {
    private final ExpertRepository expertRepository;
    private final OfferServiceImpl offerService;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final OrderRepository orderRepository;


    @Override
    @Transactional
    public Expert saveOrUpdate(Expert expert) {
        try{
            expert.setPassword(passwordEncoder.encode(expert.getPassword()));
            expert.setRole(Role.ROLE_EXPERT);
            expert.setDateOfRegistration(LocalDateTime.now());
            expert.setScore(0);
            expert.setCredit(0);

            expert.setIsEnabled(false);
            expert.setExpertStatus(ExpertStatus.NEW);
            String activationToken = generateActivationToken();
            expert.setActivationToken(activationToken);
            sendActivationEmail(expert);

            return expertRepository.save(expert);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Expert update(Expert expert) {
        try{
            return expertRepository.save(expert);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Optional<Expert> findById(Long id) {
        return expertRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Expert> findAll() {
        return expertRepository.findAll();
    }

    @Override
    @Transactional
    public List<Expert> saveAll(List<Expert> experts) {
        return expertRepository.saveAll(experts);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        expertRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Expert ChangePasswordByID(String password) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Expert expert = expertRepository.findExpertByEmail(email).orElseThrow(
                () -> new NotFoundException("expert not found")
        );
        if(PasswordValidator.isValidPassword(password))
            expert.setPassword(passwordEncoder.encode(password));
        else
            throw new NotValidPasswordException("is not valid password!");
        return expertRepository.save(expert);
    }

    @Override
    @Transactional
    public List<Order> getOrdersForOffering(Expert expert) {
        return expert.getSubServices().stream()
                .flatMap(subService -> subService.getOrders().stream())
                .filter(order -> order.getOrderStatus() == OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS || order.getOrderStatus() == OrderStatus.WAITING_FOR_SELECT_EXPERT)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public Offer offering(Offer offer) {
        SubService subService = offer.getOrder().getSubService();

        if (offer.getOfferedPrice() > subService.getBasePrice() && offer.getOfferedDate().isAfter(LocalDateTime.now())){
            offer.getOrder().setOrderStatus(OrderStatus.WAITING_FOR_SELECT_EXPERT);
            return offerService.update(offer);
        } else {
            throw new IllegalArgumentException("Invalid offer.");
        }
    }

    @Override
    @Transactional
    public List<Integer> getScoreOfComments(Long expertId) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("expert not found"));

        return expert.getComments().stream()
                .mapToInt(Comment::getScore)
                .boxed()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<Expert> findExpertByEmail(String email) {
        return expertRepository.findExpertByEmail(email);
    }

    @Override
    @Transactional
    public Optional<Expert> findExpertByActivationToken(String activationToken) {
        return expertRepository.findExpertByActivationToken(activationToken);
    }

    private String generateActivationToken() {
        return UUID.randomUUID().toString();
    }


    private void sendActivationEmail(Expert expert) {
        String activationLink = "http://localhost:8080/customer/activate?token=" + expert.getActivationToken();

        String subject = "Activate your account";
        String message = "Dear " + expert.getFirstName() + ",\n\n"
                + "Please click on the following link to activate your account:\n"
                + activationLink;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(expert.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);
    }

    @Transactional
    public String activateUserAccount(String activationToken) {
        Expert expert = expertRepository.findExpertByActivationToken(activationToken).orElseThrow(
                () -> new NotFoundException("expert not found")
        );

        if(expert.isActivationTokenExpired()){
            throw new ActivationTokenExpiredException("link is expired.");
        }

        if (expert.getIsEnabled().equals(false)) {

            expert.setIsEnabled(true);
            expert.setActivationTokenExpired(true);
            expert.setExpertStatus(ExpertStatus.AWAITING_CONFIRMATION);


            expertRepository.save(expert);
        }
        return "Account activated successfully!\n waiting for admin confirm you!";
    }

    @Transactional
    public Page<Expert> filterExperts(UserFilterCriteria criteria, Pageable pageable) {
        Specification<Expert> specification = Specification.where(null);

        if (criteria.getExpertSubServiceId() != null) {
            specification = specification.and(ExpertSpecifications.withSubService(criteria.getExpertSubServiceId()));
        }

        if (criteria.isHighestScore()) {
            specification = specification.and(ExpertSpecifications.withHighestScore());
        }

        if (criteria.isLowestScore()) {
            specification = specification.and(ExpertSpecifications.withLowestScore());
        }

        return expertRepository.findAll(specification, pageable);
    }

    @Transactional
    public List<Order> getOrdersByExpertAndStatus(OrderStatus orderStatus) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Expert expert = expertRepository.findExpertByEmail(email).orElseThrow(
                () -> new NotFoundException("expert not found")
        );
        Specification<Order> spec = Specification.where(OrderSpecifications.byOrderStatus(orderStatus));
        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("expert"), expert));
        return orderRepository.findAll(spec);
    }

    @Transactional
    public Double getCredit(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Expert expert = expertRepository.findExpertByEmail(email).orElseThrow(
                () -> new NotFoundException("expert not found")
        );

        return expert.getCredit();
    }
}


