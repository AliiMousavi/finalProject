package com.example.phase2.service.impl;

import com.example.phase2.entity.SubService;
import com.example.phase2.repository.SubServiceRepository;
import com.example.phase2.service.SubServiceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class SubServiceServiceImpl implements SubServiceService {
    private final SubServiceRepository subServiceRepository;

    public SubServiceServiceImpl(SubServiceRepository subServiceRepository) {
        this.subServiceRepository = subServiceRepository;
    }

    @Override
    public SubService saveOrUpdate(SubService subService) {
        return subServiceRepository.save(subService);
    }

    @Override
    public Optional<SubService> findById(Long id) {
        return subServiceRepository.findById(id);
    }

    @Override
    public List<SubService> findAll() {
        return subServiceRepository.findAll();
    }

    @Override
    public List<SubService> saveAll(List<SubService> subServices) {
        return subServiceRepository.saveAll(subServices);
    }

    @Override
    public void deleteById(Long id) {
        subServiceRepository.deleteById(id);
    }

    @Override
    public SubService updateBasePrice(Long subServiceId, int newBasePrice) {
        SubService byId = subServiceRepository.findById(subServiceId).orElseThrow();
        byId.setBasePrice(newBasePrice);
        return subServiceRepository.save(byId);
    }

    @Override
    public SubService updateCaption(Long subServiceId, String newCaption) {
        SubService byId = subServiceRepository.findById(subServiceId).orElseThrow();
        byId.setCaption(newCaption);
        return subServiceRepository.save(byId);
    }
}
