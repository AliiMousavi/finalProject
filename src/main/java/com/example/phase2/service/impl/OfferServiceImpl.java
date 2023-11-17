package com.example.phase2.service.impl;

import com.example.phase2.entity.Offer;
import com.example.phase2.repository.OfferRepository;
import com.example.phase2.service.OfferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;

    public OfferServiceImpl(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Override
    @Transactional
    public Offer saveOrUpdate(Offer offer) {
        try {
            offer.setDateOfOfferRegister(LocalDateTime.now());
            return offerRepository.save(offer);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Offer update(Offer offer) {
        try{
            return offerRepository.save(offer);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Optional<Offer> findById(Long id) {
        return offerRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Offer> findAll() {
        return offerRepository.findAll();
    }

    @Override
    @Transactional
    public List<Offer> saveAll(List<Offer> offers) {
        return offerRepository.saveAll(offers);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        offerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<Offer> sortOffersByExpertScore(List<Offer> offers) {
        return offers.stream()
                .sorted(Comparator.comparingDouble(offer -> offer.getExpert().getScore()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Offer> sortOffersByOfferedPrice(List<Offer> offers) {
        return offers.stream()
                .sorted(Comparator.comparingInt(Offer::getOfferedPrice))
                .collect(Collectors.toList());
    }
}
