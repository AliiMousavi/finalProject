package com.example.phase2.service.impl;

import com.example.phase2.entity.Expert;
import com.example.phase2.entity.Offer;
import com.example.phase2.repository.OfferRepository;
import com.example.phase2.service.OfferService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {
    private OfferServiceImpl offerService;
    private OfferServiceImpl offerServiceImpl;

    @Mock
    private OfferRepository offerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        offerServiceImpl = new OfferServiceImpl(offerRepository);
        offerService = offerServiceImpl;
    }

    @Test
    public void testSaveOrUpdate() {
        Offer offer = new Offer();
        offer.setId(1L);
        offer.setOfferedPrice(100);

        when(offerRepository.save(offer)).thenReturn(offer);

        Offer result = offerService.saveOrUpdate(offer);

        Assertions.assertEquals(offer, result);
        verify(offerRepository, times(1)).save(offer);
    }

    @Test
    public void testFindById_ExistingId() {
        Long id = 1L;
        Offer offer = new Offer();
        offer.setId(id);
        offer.setOfferedPrice(100);

        when(offerRepository.findById(id)).thenReturn(Optional.of(offer));

        Optional<Offer> result = offerService.findById(id);

        Assertions.assertEquals(Optional.of(offer), result);
        verify(offerRepository, times(1)).findById(id);
    }

    @Test
    public void testFindById_NonExistingId() {
        Long id = 1L;

        when(offerRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Offer> result = offerService.findById(id);

        Assertions.assertEquals(Optional.empty(), result);
        verify(offerRepository, times(1)).findById(id);
    }

    @Test
    public void testFindAll() {
        List<Offer> offers = createOffers();

        when(offerRepository.findAll()).thenReturn(offers);

        List<Offer> result = offerService.findAll();

        Assertions.assertEquals(offers, result);
        verify(offerRepository, times(1)).findAll();
    }

    @Test
    public void testSaveAll() {
        List<Offer> offers = createOffers();

        when(offerRepository.saveAll(offers)).thenReturn(offers);

        List<Offer> result = offerService.saveAll(offers);

        Assertions.assertEquals(offers, result);
        verify(offerRepository, times(1)).saveAll(offers);
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;

        offerService.deleteById(id);

        verify(offerRepository, times(1)).deleteById(id);
    }

    @Test
    public void testSortOffersByExpertScore() {
        List<Offer> offers = createOffers();

        List<Offer> expectedSortedOffers = new ArrayList<>(offers);
        expectedSortedOffers.sort(Comparator.comparingDouble(offer -> offer.getExpert().getScore()));

        List<Offer> result = offerService.sortOffersByExpertScore(offers);

        Assertions.assertEquals(expectedSortedOffers, result);
    }

    @Test
    public void testSortOffersByOfferedPrice() {
        List<Offer> offers = createOffers();

        List<Offer> expectedSortedOffers = new ArrayList<>(offers);
        expectedSortedOffers.sort(Comparator.comparingInt(Offer::getOfferedPrice));

        List<Offer> result = offerService.sortOffersByOfferedPrice(offers);

        Assertions.assertEquals(expectedSortedOffers, result);
    }

    private List<Offer> createOffers() {
        List<Offer> offers = new ArrayList<>();
        Expert expert = new Expert();
        Offer offer1 = new Offer();
        offer1.setId(1L);
        offer1.setOfferedPrice(100);
        offer1.setExpert(expert);
        offer1.getExpert().setScore(100);
        offers.add(offer1);
        Offer offer2 = new Offer();
        offer2.setId(2L);
        offer2.setOfferedPrice(200);
        offer2.setExpert(expert);
        offer2.getExpert().setScore(100);
        offers.add(offer2);
        return offers;
    }

}