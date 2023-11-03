package com.example.phase2.controller;

import com.example.phase2.dto.ServiceCoRequestDto;
import com.example.phase2.dto.ServiceCoResponseDto;
import com.example.phase2.dto.SubServiceRequestDto;
import com.example.phase2.dto.SubServiceResponseDto;
import com.example.phase2.entity.ServiceCo;
import com.example.phase2.entity.SubService;
import com.example.phase2.mapper.ServiceCoMapper;
import com.example.phase2.mapper.SubServiceMapper;
import com.example.phase2.service.impl.SubServiceServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subservice")
@RequiredArgsConstructor
public class SubServiceController {
    private final SubServiceServiceImpl subServiceService;

    @PostMapping("/save")
    public ResponseEntity<SubServiceResponseDto> save(@RequestBody @Valid SubServiceRequestDto subServiceRequestDto){
        SubService subService = SubServiceMapper.INSTANCE.dtoToSubService(subServiceRequestDto);
        SubService savedSubService = subServiceService.saveOrUpdate(subService);
        SubServiceResponseDto subServiceResponseDto = SubServiceMapper.INSTANCE.subServiceToDto(savedSubService);
        return new ResponseEntity<>(subServiceResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<SubServiceResponseDto> findById(@PathVariable Long id){
        SubService subService = subServiceService.findById(id).orElseThrow();
        SubServiceResponseDto subServiceResponseDto = SubServiceMapper.INSTANCE.subServiceToDto(subService);
        return new ResponseEntity<>(subServiceResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<SubServiceResponseDto>> findAll() {
        List<SubService> subServices = subServiceService.findAll();
        List<SubServiceResponseDto> subServiceResponseDtos = subServices.stream()
                .map(SubServiceMapper.INSTANCE::subServiceToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(subServiceResponseDtos);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SubServiceResponseDto> updateSubService(@PathVariable Long id,
                                                                @RequestBody @Valid SubServiceRequestDto subServiceRequestDto) {
        SubService subService = subServiceService.findById(id).orElseThrow();
        SubService updatedSubService = SubServiceMapper.INSTANCE.dtoToSubService(subServiceRequestDto);
        updatedSubService.setId(subService.getId());
        SubService savedSubService = subServiceService.saveOrUpdate(updatedSubService);
        SubServiceResponseDto subServiceResponseDto = SubServiceMapper.INSTANCE.subServiceToDto(savedSubService);
        return ResponseEntity.ok(subServiceResponseDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> deleteSubService(@PathVariable Long id) {
        subServiceService.deleteById(id);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/updateSubServiceBasePrice/{subServiceId}/{newBasePrice}")
    public ResponseEntity<SubServiceResponseDto> updateSubServiceBasePrice(@PathVariable Long subServiceId, @PathVariable int newBasePrice) {
        try {
            SubService updatedSubService = subServiceService.updateBasePrice(subServiceId, newBasePrice);
            SubServiceResponseDto subServiceResponseDto = SubServiceMapper.INSTANCE.subServiceToDto(updatedSubService);
            return ResponseEntity.ok(subServiceResponseDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateSubServiceCaption/{subServiceId}/{newCaption}")
    public ResponseEntity<SubServiceResponseDto> updateSubServiceCaption(@PathVariable Long subServiceId, @PathVariable String newCaption) {
        try {
            SubService updatedSubService = subServiceService.updateCaption(subServiceId, newCaption);
            SubServiceResponseDto subServiceResponseDto = SubServiceMapper.INSTANCE.subServiceToDto(updatedSubService);
            return ResponseEntity.ok(subServiceResponseDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
