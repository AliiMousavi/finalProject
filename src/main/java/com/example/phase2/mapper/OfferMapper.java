package com.example.phase2.mapper;

import com.example.phase2.dto.OfferRequestDto;
import com.example.phase2.dto.OfferResponseDto;
import com.example.phase2.entity.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OfferMapper {
    OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class);
    Offer dtoToOffer(OfferRequestDto offerRequestDto);

    OfferResponseDto offerToDto(Offer offer);

}
