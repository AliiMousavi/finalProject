package com.example.phase2.controller;

import com.example.phase2.dto.ExpertRequestDto;
import com.example.phase2.dto.ExpertResponseDto;
import com.example.phase2.dto.OrderRequestDto;
import com.example.phase2.dto.OrderResponseDto;
import com.example.phase2.entity.Expert;
import com.example.phase2.entity.Order;
import com.example.phase2.mapper.ExpertMapper;
import com.example.phase2.mapper.OrderMapper;
import com.example.phase2.service.impl.OrderServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    @PostMapping("/save")
    public ResponseEntity<OrderResponseDto> save(@RequestBody @Valid OrderRequestDto orderRequestDto){
        Order order = OrderMapper.INSTANCE.dtoToOrder(orderRequestDto);
        Order savedOrder = orderService.saveOrUpdate(order);
        OrderResponseDto orderResponseDto = OrderMapper.INSTANCE.orderToDto(savedOrder);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<OrderResponseDto> findById(@PathVariable Long id){
        Order order = orderService.findById(id).orElseThrow();
        OrderResponseDto orderResponseDto = OrderMapper.INSTANCE.orderToDto(order);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<OrderResponseDto>> findAll() {
        List<Order> orders = orderService.findAll();
        List<OrderResponseDto> orderResponseDtos = orders.stream()
                .map(OrderMapper.INSTANCE::orderToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderResponseDtos);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable Long id,
                                                          @RequestBody @Valid OrderRequestDto orderRequestDto) {
        Order order = orderService.findById(id).orElseThrow();
        Order updatedOrder = OrderMapper.INSTANCE.dtoToOrder(orderRequestDto);
        updatedOrder.setId(order.getId());
        Order savedOrder = orderService.saveOrUpdate(updatedOrder);
        OrderResponseDto orderResponseDto = OrderMapper.INSTANCE.orderToDto(savedOrder);
        return ResponseEntity.ok(orderResponseDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> deleteOrder(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.ok(id);
    }
}
