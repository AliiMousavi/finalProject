package com.example.phase2.mapper;

import com.example.phase2.dto.OrderRequestDto;
import com.example.phase2.dto.OrderResponseDto;
import com.example.phase2.dto.ServiceCoRequestDto;
import com.example.phase2.dto.ServiceCoResponseDto;
import com.example.phase2.entity.Order;
import com.example.phase2.entity.ServiceCo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ServiceCoMapper {
    ServiceCoMapper INSTANCE = Mappers.getMapper(ServiceCoMapper.class);

    ServiceCo dtoToServiceCo(ServiceCoRequestDto serviceCoRequestDto);

    ServiceCoResponseDto serviceCoToDto(ServiceCo serviceCo);
}
