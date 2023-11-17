package com.example.phase2.mapper;

import com.example.phase2.dto.*;
import com.example.phase2.entity.ServiceCo;
import com.example.phase2.entity.SubService;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubServiceMapper {
    SubServiceMapper INSTANCE = Mappers.getMapper(SubServiceMapper.class);

    SubService dtoToSubService(SubServiceRequestDto subServiceRequestDto);
    SubService justIdDtoToService(SubServiceResponseDtoJustId subServiceResponseDtoJustId);

    SubServiceResponseDto subServiceToDto(SubService subService);
    SubServiceResponseDto_justExpert subServiceTojustExpertDto(SubService subService);

}
