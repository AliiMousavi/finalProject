package com.example.phase2.mapper;

import com.example.phase2.dto.CustomerRequestDto;
import com.example.phase2.dto.CustomerResponseDto;
import com.example.phase2.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer dtoToCustomer(CustomerRequestDto customerRequestDto);

    CustomerResponseDto customerToDto(Customer customer);
}
