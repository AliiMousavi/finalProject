package com.example.phase2.service;


import com.example.phase2.entity.Offer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface OfferService {
    Offer saveOrUpdate(Offer offer);
    Optional<Offer> findById(Long id);
    List<Offer> findAll();
    List<Offer> saveAll(List<Offer> offers);
    void deleteById(Long id);
    List<Offer> sortOffersByExpertScore(List<Offer> offers);
    List<Offer> sortOffersByOfferedPrice(List<Offer> offers);
}
