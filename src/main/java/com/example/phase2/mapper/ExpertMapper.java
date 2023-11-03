package com.example.phase2.mapper;

import com.example.phase2.dto.CustomerRequestDto;
import com.example.phase2.dto.CustomerResponseDto;
import com.example.phase2.dto.ExpertRequestDto;
import com.example.phase2.dto.ExpertResponseDto;
import com.example.phase2.entity.Customer;
import com.example.phase2.entity.Expert;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface ExpertMapper {
    ExpertMapper INSTANCE = Mappers.getMapper(ExpertMapper.class);

    Expert dtoToExpert(ExpertRequestDto expertRequestDto);

    ExpertResponseDto expertToDto(Expert expert);
}
