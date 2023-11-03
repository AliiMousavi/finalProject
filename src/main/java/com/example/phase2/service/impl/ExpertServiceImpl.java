package com.example.phase2.service.impl;


import com.example.phase2.entity.*;
import com.example.phase2.entity.enumeration.OrderStatus;
import com.example.phase2.exception.NotValidPasswordException;
import com.example.phase2.repository.ExpertRepository;
import com.example.phase2.service.ExpertService;
import com.example.phase2.validator.PasswordValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.sql.Time.valueOf;

@Service
public class ExpertServiceImpl implements ExpertService {

    private final ExpertRepository expertRepository;
    private final OfferServiceImpl offerService;

    public ExpertServiceImpl(ExpertRepository expertRepository, OfferServiceImpl offerService) {
        this.expertRepository = expertRepository;
        this.offerService = offerService;
    }

    @Override
    @Transactional
    public Expert saveOrUpdate(Expert expert) {
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
    public Expert ChangePasswordByID(String password, Long id) {
        Expert expert = expertRepository.findById(id).orElseThrow();
        if(PasswordValidator.isValidPassword(password))
            expert.setPassword(password);
        else
            throw new NotValidPasswordException("is not valid password!");
        return expertRepository.save(expert);
    }

    @Override
    @Transactional
    public List<Order> getOrdersForOffering(Expert expert) {
        return expert.getSubServices().stream()
                .flatMap(subService -> subService.getOrders().stream())
                .filter(order -> {
                    OrderStatus orderStatus = order.getOrderStatus();
                    return orderStatus == OrderStatus.WAITING_FOR_THE_SUGGESTION_OF_EXPERTS || orderStatus == OrderStatus.WAITING_FOR_SELECT_EXPERT;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Offer offering(Offer offer) {
        SubService subService = offer.getOrder().getSubService();

        if (offer.getOfferedPrice() > subService.getBasePrice() && offer.getOfferedDate().isAfter(LocalDateTime.now())){
            offer.getOrder().setOrderStatus(OrderStatus.WAITING_FOR_SELECT_EXPERT);
            return offerService.saveOrUpdate(offer);
        } else {
            throw new IllegalArgumentException("Invalid offer.");
        }
    }

    @Override
    @Transactional
    public List<Integer> getScoreOfComments(Long expertId) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("expert not found"));

        List<Integer> commentScores = expert.getComments().stream()
                .mapToInt(Comment::getScore)
                .boxed()
                .collect(Collectors.toList());
        return commentScores;
    }
}

