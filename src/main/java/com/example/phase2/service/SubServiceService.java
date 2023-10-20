package com.example.phase2.service;

import com.example.phase2.entity.SubService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SubServiceService{
    SubService saveOrUpdate(SubService subService);
    Optional<SubService> findById(Long id);
    List<SubService> findAll();
    List<SubService> saveAll(List<SubService> subServices);
    void deleteById(Long id);
    SubService updateBasePrice(Long subServiceId, int newBasePrice);
    SubService updateCaption(Long subServiceId, String newCaption);
}
