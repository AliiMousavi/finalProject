package com.example.phase2.mapper;

import com.example.phase2.dto.OrderRequestDto;
import com.example.phase2.dto.OrderResponseDto;
import com.example.phase2.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order dtoToOrder(OrderRequestDto orderRequestDto);

    OrderResponseDto orderToDto(Order order);
}
