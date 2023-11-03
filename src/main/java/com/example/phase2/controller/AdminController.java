package com.example.phase2.controller;

import com.example.phase2.dto.*;
import com.example.phase2.entity.Expert;
import com.example.phase2.entity.ServiceCo;
import com.example.phase2.entity.SubService;
import com.example.phase2.mapper.ExpertMapper;
import com.example.phase2.mapper.ServiceCoMapper;
import com.example.phase2.mapper.SubServiceMapper;
import com.example.phase2.service.impl.AdminServiceImpl;
import com.example.phase2.service.impl.ExpertServiceImpl;
import com.example.phase2.service.impl.SubServiceServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminServiceImpl adminService;
    private final ExpertServiceImpl expertService;
    private final SubServiceServiceImpl subServiceService;

    public AdminController(AdminServiceImpl adminService, ExpertServiceImpl expertService, SubServiceServiceImpl subServiceService) {
        this.adminService = adminService;
        this.expertService = expertService;
        this.subServiceService = subServiceService;
    }

    @PostMapping("/saveServiceCo")
    public ResponseEntity<ServiceCoResponseDto> addServiceCo(@RequestBody @Valid ServiceCoRequestDto serviceCoRequestDto){
        ServiceCo serviceCo = ServiceCoMapper.INSTANCE.dtoToServiceCo(serviceCoRequestDto);
        ServiceCo savedServiceCo = adminService.addServiceCo(serviceCo);
        ServiceCoResponseDto serviceCoResponseDto = ServiceCoMapper.INSTANCE.serviceCoToDto(savedServiceCo);
        return new ResponseEntity<>(serviceCoResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/savesubservice")
    public ResponseEntity<SubServiceResponseDto> addSubService(@RequestBody @Valid SubServiceRequestDto subServiceRequestDto){
        SubService subService = SubServiceMapper.INSTANCE.dtoToSubService(subServiceRequestDto);
        SubService savedSubService = adminService.addSubService(subService);
        SubServiceResponseDto subServiceResponseDto = SubServiceMapper.INSTANCE.subServiceToDto(savedSubService);
        return new ResponseEntity<>(subServiceResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/servicecos")
    public ResponseEntity<List<ServiceCoResponseDto>> getAllServiceCos() {
        Collection<ServiceCo> serviceCos = adminService.getAllServiceCo();
        List<ServiceCoResponseDto> serviceCoResponseDtos = serviceCos.stream()
                .map(ServiceCoMapper.INSTANCE::serviceCoToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(serviceCoResponseDtos);
    }

    @GetMapping("/getAllSubServices")
    public ResponseEntity<List<SubServiceResponseDto>> getAllSubServices() {
        Collection<SubService> subServices = adminService.getAllSubService();
        List<SubServiceResponseDto> subServiceResponseDtos = subServices.stream()
                .map(SubServiceMapper.INSTANCE::subServiceToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(subServiceResponseDtos);
    }


    @PutMapping("/updateBasePrice/{id}/{newBasePrice}")
    public ResponseEntity<SubServiceResponseDto> updateBasePrice(@PathVariable Long id, @PathVariable int newBasePrice) {
        SubService updatedSubService = adminService.updateBasePrice(id, newBasePrice);
        SubServiceResponseDto subServiceResponseDto = SubServiceMapper.INSTANCE.subServiceToDto(updatedSubService);
        return ResponseEntity.ok(subServiceResponseDto);
    }

    @PutMapping("/subservices/{id}/{newCaption}")
    public ResponseEntity<SubServiceResponseDto> updateCaption(@PathVariable Long id, @PathVariable String newCaption) {
        SubService updatedSubService = adminService.updateCaption(id, newCaption);
        SubServiceResponseDto subServiceResponseDto = SubServiceMapper.INSTANCE.subServiceToDto(updatedSubService);
        return ResponseEntity.ok(subServiceResponseDto);
    }

    @PostMapping("/addExpertToSubService/{subServiceId}/{expertId}")
    public ResponseEntity<SubServiceResponseDto_justExpert> addExpertToSubService(@PathVariable Long subServiceId, @PathVariable Long expertId) {
        Expert expert = expertService.findById(expertId).orElseThrow(() -> new IllegalArgumentException("Expert not found"));
        SubService subService = subServiceService.findById(subServiceId).orElseThrow(() -> new IllegalArgumentException("SubService not found"));
        SubService updatedSubService = adminService.addExpertToSubService(expert, subService);
        SubServiceResponseDto_justExpert responseDto = SubServiceMapper.INSTANCE.subServiceTojustExpertDto(updatedSubService);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/deleteExpertFromSubService/{subServiceId}/{expertId}")
    public ResponseEntity<SubServiceResponseDto_justExpert> deleteExpertFromSubService(@PathVariable Long subServiceId, @PathVariable Long expertId) {
        Expert expert = expertService.findById(expertId).orElseThrow(() -> new IllegalArgumentException("Expert not found"));
        SubService subService = subServiceService.findById(subServiceId).orElseThrow(() -> new IllegalArgumentException("SubService not found"));
        SubService updatedSubService = adminService.deleteExpertToSubService(expert, subService);
        SubServiceResponseDto_justExpert responseDto = SubServiceMapper.INSTANCE.subServiceTojustExpertDto(updatedSubService);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/experts/{id}/confirm")
    public ResponseEntity<ExpertResponseDto> confirmExpertById(@PathVariable Long id) {
        Expert confirmedExpert = adminService.confirmExpertbyId(id);
        ExpertResponseDto responseDto = ExpertMapper.INSTANCE.expertToDto(confirmedExpert);
        return ResponseEntity.ok(responseDto);
    }

}
