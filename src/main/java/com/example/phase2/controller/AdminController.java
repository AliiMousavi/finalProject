package com.example.phase2.controller;

import com.example.phase2.dto.*;
import com.example.phase2.entity.*;
import com.example.phase2.mapper.ExpertMapper;
import com.example.phase2.mapper.ServiceCoMapper;
import com.example.phase2.mapper.SubServiceMapper;
import com.example.phase2.service.impl.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminServiceImpl adminService;
    private final ExpertServiceImpl expertService;
    private final SubServiceServiceImpl subServiceService;
    private final ServiceCoServiceImpl serviceCoService;
    private final OrderServiceImpl orderService;
    private final UserServiceImpl userService;


    @PostMapping("/saveServiceCo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceCoResponseDto> addServiceCo(@RequestBody @Valid ServiceCoRequestDto serviceCoRequestDto){
        ServiceCo serviceCo = ServiceCoMapper.INSTANCE.dtoToServiceCo(serviceCoRequestDto);
        ServiceCo savedServiceCo = adminService.addServiceCo(serviceCo);
        ServiceCoResponseDto serviceCoResponseDto = ServiceCoMapper.INSTANCE.serviceCoToDto(savedServiceCo);
        return new ResponseEntity<>(serviceCoResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/savesubservice")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubServiceResponseDto> addSubService(@RequestBody @Valid SubServiceRequestDto subServiceRequestDto){
        ServiceCo serviceCo = serviceCoService.findById(subServiceRequestDto.getServiceCoId()).orElseThrow();
        SubService subService = SubServiceMapper.INSTANCE.dtoToSubService(subServiceRequestDto);
        subService.setServiceCo(serviceCo);
        SubService savedSubService = adminService.addSubService(subService);
        SubServiceResponseDto subServiceResponseDto = SubServiceMapper.INSTANCE.subServiceToDto(savedSubService);
        return new ResponseEntity<>(subServiceResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/servicecos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ServiceCoResponseDto>> getAllServiceCos() {
        Collection<ServiceCo> serviceCos = adminService.getAllServiceCo();
        List<ServiceCoResponseDto> serviceCoResponseDtos = serviceCos.stream()
                .map(ServiceCoMapper.INSTANCE::serviceCoToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(serviceCoResponseDtos);
    }

    @GetMapping("/getAllSubServices")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SubServiceResponseDto>> getAllSubServices() {
        Collection<SubService> subServices = adminService.getAllSubService();
        List<SubServiceResponseDto> subServiceResponseDtos = subServices.stream()
                .map(SubServiceMapper.INSTANCE::subServiceToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(subServiceResponseDtos);
    }


    @PutMapping("/updateBasePrice/{id}/{newBasePrice}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubServiceResponseDto> updateBasePrice(@PathVariable Long id, @PathVariable int newBasePrice) {
        SubService updatedSubService = adminService.updateBasePrice(id, newBasePrice);
        SubServiceResponseDto subServiceResponseDto = SubServiceMapper.INSTANCE.subServiceToDto(updatedSubService);
        return ResponseEntity.ok(subServiceResponseDto);
    }

    @PutMapping("/subservices/{id}/{newCaption}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubServiceResponseDto> updateCaption(@PathVariable Long id, @PathVariable String newCaption) {
        SubService updatedSubService = adminService.updateCaption(id, newCaption);
        SubServiceResponseDto subServiceResponseDto = SubServiceMapper.INSTANCE.subServiceToDto(updatedSubService);
        return ResponseEntity.ok(subServiceResponseDto);
    }

    @PostMapping("/addExpertToSubService/{subServiceId}/{expertId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubServiceResponseDto_justExpert> addExpertToSubService(@PathVariable Long subServiceId, @PathVariable Long expertId) {
        Expert expert = expertService.findById(expertId).orElseThrow(() -> new IllegalArgumentException("Expert not found"));
        SubService subService = subServiceService.findById(subServiceId).orElseThrow(() -> new IllegalArgumentException("SubService not found"));
        SubService updatedSubService = adminService.addExpertToSubService(expert, subService);
        SubServiceResponseDto_justExpert responseDto = SubServiceMapper.INSTANCE.subServiceTojustExpertDto(updatedSubService);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/deleteExpertFromSubService/{subServiceId}/{expertId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubServiceResponseDto_justExpert> deleteExpertFromSubService(@PathVariable Long subServiceId, @PathVariable Long expertId) {
        Expert expert = expertService.findById(expertId).orElseThrow(() -> new IllegalArgumentException("Expert not found"));
        SubService subService = subServiceService.findById(subServiceId).orElseThrow(() -> new IllegalArgumentException("SubService not found"));
        SubService updatedSubService = adminService.deleteExpertToSubService(expert, subService);
        SubServiceResponseDto_justExpert responseDto = SubServiceMapper.INSTANCE.subServiceTojustExpertDto(updatedSubService);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/confirmExpertById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExpertResponseDto> confirmExpertById(@PathVariable Long id) {
        Expert confirmedExpert = adminService.confirmExpertbyId(id);
        ExpertResponseDto responseDto = ExpertMapper.INSTANCE.expertToDto(confirmedExpert);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/filterUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<User>> filterUsers(@RequestBody UserFilterCriteria criteria, Pageable pageable) {
        Page<User> filteredUsers = userService.filterUsers(criteria, pageable);
        return ResponseEntity.ok(filteredUsers);
    }

    @PostMapping("/filterExpert")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Expert>> filterExpert(@RequestBody UserFilterCriteria criteria, Pageable pageable) {
        Page<Expert> filteredUsers = expertService.filterExperts(criteria, pageable);
        return ResponseEntity.ok(filteredUsers);
    }
    @GetMapping("/getCustomerSubServiceHistory/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerSubServiceHistory> getCustomerSubServiceHistory(@PathVariable Long id) {
        CustomerSubServiceHistory customerSubServiceHistory = adminService.getCustomerSubServiceHistory(id);
        return ResponseEntity.ok(customerSubServiceHistory);
    }

    @GetMapping("/getExpertSubServiceHistory/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExpertSubServiceHistory> getExpertSubServiceHistory(@PathVariable Long id) {
        ExpertSubServiceHistory expertSubServiceHistory = adminService.getExpertSubServiceHistory(id);
        return ResponseEntity.ok(expertSubServiceHistory);
    }

    @GetMapping("/filterExpert")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Order>> filterOrder(@RequestBody OrderFilterCriteria criteria, Pageable pageable) {
        Page<Order> filteredOrders = adminService.getOrdersByCriteria(criteria, pageable);
        return ResponseEntity.ok(filteredOrders);
    }

    @GetMapping("/getExpertsByNumberOfOrders/{numberOfOrders}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Expert>> getExpertsByNumberOfOrders(@PathVariable int numberOfOrders) {
        List<Expert> filteredOrders = adminService.getExpertsByNumberOfOrders(numberOfOrders);
        return ResponseEntity.ok(filteredOrders);
    }

    @GetMapping("/getCustomersByNumberOfOrders/{numberOfOrders}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Customer>> getCustomersByNumberOfOrders(@PathVariable int numberOfOrders) {
        List<Customer> filteredOrders = adminService.getCustomersByNumberOfOrders(numberOfOrders);
        return ResponseEntity.ok(filteredOrders);
    }

}
