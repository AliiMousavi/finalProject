package com.example.phase2.controller;

import com.example.phase2.dto.OfferRequestDto;
import com.example.phase2.dto.OfferResponseDto;
import com.example.phase2.dto.OrderRequestDto;
import com.example.phase2.dto.OrderResponseDto;
import com.example.phase2.entity.Offer;
import com.example.phase2.entity.Order;
import com.example.phase2.mapper.OfferMapper;
import com.example.phase2.mapper.OrderMapper;
import com.example.phase2.service.impl.OfferServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/offer")
@RequiredArgsConstructor
public class OfferController {

    private final OfferServiceImpl offerService;

    @PostMapping("/save")
    public ResponseEntity<OfferResponseDto> save(@RequestBody @Valid OfferRequestDto offerRequestDto){
        Offer offer = OfferMapper.INSTANCE.dtoToOffer(offerRequestDto);
        Offer savedOffer = offerService.saveOrUpdate(offer);
        OfferResponseDto offerResponseDto = OfferMapper.INSTANCE.offerToDto(savedOffer);
        return new ResponseEntity<>(offerResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<OfferResponseDto> findById(@PathVariable Long id){
        Offer offer = offerService.findById(id).orElseThrow();
        OfferResponseDto offerResponseDto = OfferMapper.INSTANCE.offerToDto(offer);
        return new ResponseEntity<>(offerResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<OfferResponseDto>> findAll() {
        List<Offer> offers = offerService.findAll();
        List<OfferResponseDto> offerResponseDtos = offers.stream()
                .map(OfferMapper.INSTANCE::offerToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(offerResponseDtos);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<OfferResponseDto> updateOffer(@PathVariable Long id,
                                                        @RequestBody @Valid OfferRequestDto offerRequestDto) {
        Offer offer = offerService.findById(id).orElseThrow();
        Offer updatedOffer = OfferMapper.INSTANCE.dtoToOffer(offerRequestDto);
        updatedOffer.setId(offer.getId());
        Offer savedOffer = offerService.saveOrUpdate(updatedOffer);
        OfferResponseDto offerResponseDto = OfferMapper.INSTANCE.offerToDto(savedOffer);
        return ResponseEntity.ok(offerResponseDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> deleteOffer(@PathVariable Long id) {
        offerService.deleteById(id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/sortOffersByExpertScore")
    public ResponseEntity<List<OfferResponseDto>> sortOffersByExpertScore(@RequestBody List<OfferRequestDto> offerRequestDtos) {

        List<Offer> offers = offerRequestDtos.stream()
                .map(OfferMapper.INSTANCE::dtoToOffer)
                .collect(Collectors.toList());
        offerService.sortOffersByExpertScore(offers);

        List<OfferResponseDto> offerResponseDtos = offers.stream()
                .map(OfferMapper.INSTANCE::offerToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(offerResponseDtos);
    }

    @GetMapping("/sortOffersByOfferedPrice")
    public ResponseEntity<List<OfferResponseDto>> sortOffersByOfferedPrice(@RequestBody List<OfferRequestDto> offerRequestDtos) {

        List<Offer> offers = offerRequestDtos.stream()
                .map(OfferMapper.INSTANCE::dtoToOffer)
                .collect(Collectors.toList());
        offerService.sortOffersByOfferedPrice(offers);

        List<OfferResponseDto> offerResponseDtos = offers.stream()
                .map(OfferMapper.INSTANCE::offerToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(offerResponseDtos);
    }


}
