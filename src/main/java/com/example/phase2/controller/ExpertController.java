package com.example.phase2.controller;


import com.example.phase2.dto.ExpertRequestDto;
import com.example.phase2.dto.ExpertResponseDto;
import com.example.phase2.dto.OfferRequestDto;
import com.example.phase2.dto.OrderResponseDto;
import com.example.phase2.entity.Expert;
import com.example.phase2.entity.Offer;
import com.example.phase2.entity.Order;
import com.example.phase2.entity.SubService;
import com.example.phase2.entity.enumeration.ExpertStatus;
import com.example.phase2.entity.enumeration.OrderStatus;
import com.example.phase2.exception.NotValidPasswordException;
import com.example.phase2.mapper.ExpertMapper;
import com.example.phase2.mapper.OfferMapper;
import com.example.phase2.mapper.OrderMapper;
import com.example.phase2.service.impl.ExpertServiceImpl;
import com.example.phase2.service.impl.OrderServiceImpl;
import com.example.phase2.service.impl.SubServiceServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/expert")
public class ExpertController {
    private final ExpertServiceImpl expertService;
    private final OrderServiceImpl orderService;
    private final SubServiceServiceImpl subServiceService;

    public ExpertController(ExpertServiceImpl expertService, OrderServiceImpl orderService, SubServiceServiceImpl subServiceService) {
        this.expertService = expertService;
        this.orderService = orderService;
        this.subServiceService = subServiceService;
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN','EXPERT')")
    public ResponseEntity<ExpertResponseDto> save(@RequestBody @Valid ExpertRequestDto expertRequestDto){
        SubService subService = subServiceService.findById(expertRequestDto.getSubServiceId()).orElseThrow();
        Expert expert = ExpertMapper.INSTANCE.dtoToExpert(expertRequestDto);
        expert.getSubServices().add(subService);
        expert.setExpertStatus(ExpertStatus.NEW);
        expert.setDateOfRegistration(LocalDateTime.now());
        Expert savedexpert = expertService.saveOrUpdate(expert);
        ExpertResponseDto expertResponseDto = ExpertMapper.INSTANCE.expertToDto(savedexpert);
        return new ResponseEntity<>(expertResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/find-by-id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EXPERT')")
    public ResponseEntity<ExpertResponseDto> findById(@PathVariable Long id){
        Expert expert = expertService.findById(id).orElseThrow();
        ExpertResponseDto expertResponseDto = ExpertMapper.INSTANCE.expertToDto(expert);
        return new ResponseEntity<>(expertResponseDto, HttpStatus.OK);
    }

    @GetMapping("/find-all")
    @PreAuthorize("hasAnyRole('ADMIN','EXPERT')")
    public ResponseEntity<List<ExpertResponseDto>> findAll() {
        List<Expert> experts = expertService.findAll();
        List<ExpertResponseDto> expertResponseDtos = experts.stream()
                .map(ExpertMapper.INSTANCE::expertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(expertResponseDtos);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EXPERT')")
    public ResponseEntity<ExpertResponseDto> updateExpert(@PathVariable Long id,
                                                              @RequestBody @Valid ExpertRequestDto expertRequestDto) {
        Expert expert = expertService.findById(id).orElseThrow();
        Expert updatedExpert = ExpertMapper.INSTANCE.dtoToExpert(expertRequestDto);
        updatedExpert.setId(expert.getId());
        Expert savedExpert = expertService.update(updatedExpert);
        ExpertResponseDto expertResponseDto = ExpertMapper.INSTANCE.expertToDto(savedExpert);
        return ResponseEntity.ok(expertResponseDto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EXPERT')")
    public ResponseEntity<Long> deleteExpert(@PathVariable Long id) {
        expertService.deleteById(id);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/change-password/{password}")
    @PreAuthorize("hasRole('EXPERT')")
    public ResponseEntity<String> changePasswordById(@PathVariable String password) {
        try {
            Expert updatedExpert = expertService.ChangePasswordByID(password);
            if (updatedExpert != null) {
                return ResponseEntity.ok("password has changed!");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NotValidPasswordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getOrdersForOffering/{expertId}")
    @PreAuthorize("hasRole('EXPERT')")
    public ResponseEntity<List<OrderResponseDto>> getOrdersForOffering(@PathVariable Long expertId) {
        Expert expert = expertService.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Expert not found"));
        List<OrderResponseDto> ordersForOffering = expertService.getOrdersForOffering(expert).stream()
                .map(OrderMapper.INSTANCE::orderToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ordersForOffering);
    }

    @PostMapping("/offering")
    @PreAuthorize("hasAnyRole('ADMIN','EXPERT')")
    public ResponseEntity<Void> offering(@RequestBody OfferRequestDto offerRequestDto) {
        Order order = orderService.findById(offerRequestDto.getOrderId()).orElseThrow();
        Expert expert = expertService.findById(offerRequestDto.getExpertId()).orElseThrow();
        Offer offer = OfferMapper.INSTANCE.dtoToOffer(offerRequestDto);
        offer.setOrder(order);
        offer.setExpert(expert);
        offer.setDateOfOfferRegister(LocalDateTime.now());
            expertService.offering(offer);
            return ResponseEntity.ok().build();

    }

    @GetMapping("/getScoreOfComments/{id}")
    @PreAuthorize("hasRole('EXPERT')")
    public ResponseEntity<List<Integer>> getScoreOfComments(@PathVariable Long id) {
        List<Integer> scoreOfComments = expertService.getScoreOfComments(id);
        return ResponseEntity.ok(scoreOfComments);
    }

    @GetMapping("/activate")
    @PreAuthorize("hasRole('EXPERT')")
    public String activateAccount(@RequestParam("token") String activationToken) {
        return expertService.activateUserAccount(activationToken);
    }

    @GetMapping("/getOrdersByExpertAndStatus/{orderStatus}")
    @PreAuthorize("hasRole('EXPERT')")
    public ResponseEntity<List<Order>> getOrdersByExpertAndStatus(@PathVariable OrderStatus orderStatus){
        List<Order> orders = expertService.getOrdersByExpertAndStatus(orderStatus);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getCredit")
    @PreAuthorize("hasRole('EXPERT')")
    public ResponseEntity<Double> getCredit(){
        return ResponseEntity.ok(expertService.getCredit());
    }

}
