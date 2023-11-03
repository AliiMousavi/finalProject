package com.example.phase2.controller;

import com.example.phase2.dto.OrderRequestDto;
import com.example.phase2.dto.OrderResponseDto;
import com.example.phase2.dto.ServiceCoRequestDto;
import com.example.phase2.dto.ServiceCoResponseDto;
import com.example.phase2.entity.Order;
import com.example.phase2.entity.ServiceCo;
import com.example.phase2.mapper.OrderMapper;
import com.example.phase2.mapper.ServiceCoMapper;
import com.example.phase2.service.impl.ServiceCoServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/serviceCo")
@RequiredArgsConstructor
public class ServiceCoController {
    private final ServiceCoServiceImpl serviceCoService;

    @PostMapping("/save")
    public ResponseEntity<ServiceCoResponseDto> save(@RequestBody @Valid ServiceCoRequestDto serviceCoRequestDto){
        ServiceCo serviceCo = ServiceCoMapper.INSTANCE.dtoToServiceCo(serviceCoRequestDto);
        ServiceCo savedServiceCo = serviceCoService.saveOrUpdate(serviceCo);
        ServiceCoResponseDto serviceCoResponseDto = ServiceCoMapper.INSTANCE.serviceCoToDto(savedServiceCo);
        return new ResponseEntity<>(serviceCoResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<ServiceCoResponseDto> findById(@PathVariable Long id){
        ServiceCo serviceCo = serviceCoService.findById(id).orElseThrow();
        ServiceCoResponseDto serviceCoResponseDto = ServiceCoMapper.INSTANCE.serviceCoToDto(serviceCo);
        return new ResponseEntity<>(serviceCoResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<ServiceCoResponseDto>> findAll() {
        List<ServiceCo> serviceCos = serviceCoService.findAll();
        List<ServiceCoResponseDto> serviceCoResponseDtos = serviceCos.stream()
                .map(ServiceCoMapper.INSTANCE::serviceCoToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(serviceCoResponseDtos);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ServiceCoResponseDto> updateServiceCo(@PathVariable Long id,
                                                        @RequestBody @Valid ServiceCoRequestDto serviceCoRequestDto) {
        ServiceCo serviceCo = serviceCoService.findById(id).orElseThrow();
        ServiceCo updatedServiceCo = ServiceCoMapper.INSTANCE.dtoToServiceCo(serviceCoRequestDto);
        updatedServiceCo.setId(serviceCo.getId());
        ServiceCo savedServiceCo = serviceCoService.saveOrUpdate(updatedServiceCo);
        ServiceCoResponseDto serviceCoResponseDto = ServiceCoMapper.INSTANCE.serviceCoToDto(savedServiceCo);
        return ResponseEntity.ok(serviceCoResponseDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> deleteServiceCo(@PathVariable Long id) {
        serviceCoService.deleteById(id);
        return ResponseEntity.ok(id);
    }

}
