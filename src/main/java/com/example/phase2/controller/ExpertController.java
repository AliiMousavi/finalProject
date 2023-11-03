package com.example.phase2.controller;


import com.example.phase2.dto.ExpertRequestDto;
import com.example.phase2.dto.ExpertResponseDto;
import com.example.phase2.dto.OrderResponseDto;
import com.example.phase2.entity.Expert;
import com.example.phase2.entity.Offer;
import com.example.phase2.entity.Order;
import com.example.phase2.exception.NotValidPasswordException;
import com.example.phase2.mapper.ExpertMapper;
import com.example.phase2.mapper.OrderMapper;
import com.example.phase2.service.impl.ExpertServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/expert")
public class ExpertController {
    private final ExpertServiceImpl expertService;

    public ExpertController(ExpertServiceImpl expertService) {
        this.expertService = expertService;
    }

    @PostMapping("/save")
    public ResponseEntity<ExpertResponseDto> save(@RequestBody @Valid ExpertRequestDto expertRequestDto){
        Expert expert = ExpertMapper.INSTANCE.dtoToExpert(expertRequestDto);
        Expert savedexpert = expertService.saveOrUpdate(expert);
        ExpertResponseDto expertResponseDto = ExpertMapper.INSTANCE.expertToDto(savedexpert);
        return new ResponseEntity<>(expertResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<ExpertResponseDto> findById(@PathVariable Long id){
        Expert expert = expertService.findById(id).orElseThrow();
        ExpertResponseDto expertResponseDto = ExpertMapper.INSTANCE.expertToDto(expert);
        return new ResponseEntity<>(expertResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<ExpertResponseDto>> findAll() {
        List<Expert> experts = expertService.findAll();
        List<ExpertResponseDto> expertResponseDtos = experts.stream()
                .map(ExpertMapper.INSTANCE::expertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(expertResponseDtos);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ExpertResponseDto> updateExpert(@PathVariable Long id,
                                                              @RequestBody @Valid ExpertRequestDto expertRequestDto) {
        Expert expert = expertService.findById(id).orElseThrow();
        Expert updatedExpert = ExpertMapper.INSTANCE.dtoToExpert(expertRequestDto);
        updatedExpert.setId(expert.getId());
        Expert savedExpert = expertService.saveOrUpdate(updatedExpert);
        ExpertResponseDto expertResponseDto = ExpertMapper.INSTANCE.expertToDto(savedExpert);
        return ResponseEntity.ok(expertResponseDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> deleteExpert(@PathVariable Long id) {
        expertService.deleteById(id);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/change-password/{id}/{password}")
    public ResponseEntity<String> changePasswordById(@PathVariable Long id, @PathVariable String password) {
        try {
            Expert updatedExpert = expertService.ChangePasswordByID(password, id);
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
    public ResponseEntity<List<OrderResponseDto>> getOrdersForOffering(@PathVariable Long expertId) {
        Expert expert = expertService.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Expert not found"));
        List<OrderResponseDto> ordersForOffering = expertService.getOrdersForOffering(expert).stream()
                .map(OrderMapper.INSTANCE::orderToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ordersForOffering);
    }

    @PostMapping("/offering")
    public ResponseEntity<Void> offering(@RequestBody Offer offer) {
        try {
            expertService.offering(offer);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getScoreOfComments/{id}")
    public ResponseEntity<List<Integer>> getScoreOfComments(@PathVariable Long id) {
        List<Integer> scoreOfComments = expertService.getScoreOfComments(id);
        return ResponseEntity.ok(scoreOfComments);
    }

}
