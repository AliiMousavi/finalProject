package com.example.phase2.service.impl;

import com.example.phase2.entity.SubService;
import com.example.phase2.repository.SubServiceRepository;
import com.example.phase2.service.SubServiceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class SubServiceServiceImpl implements SubServiceService {
    private final SubServiceRepository subServiceRepository;

    public SubServiceServiceImpl(SubServiceRepository subServiceRepository) {
        this.subServiceRepository = subServiceRepository;
    }

    @Override
    @Transactional
    public SubService saveOrUpdate(SubService subService) {
        return subServiceRepository.save(subService);
    }

    @Override
    @Transactional
    public SubService update(SubService subService) {
        try{
            return subServiceRepository.save(subService);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Optional<SubService> findById(Long id) {
        return subServiceRepository.findById(id);
    }

    @Override
    @Transactional
    public List<SubService> findAll() {
        return subServiceRepository.findAll();
    }

    @Override
    @Transactional
    public List<SubService> saveAll(List<SubService> subServices) {
        return subServiceRepository.saveAll(subServices);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        subServiceRepository.deleteById(id);
    }

    @Override
    @Transactional
    public SubService updateBasePrice(Long subServiceId, int newBasePrice) {
        SubService byId = subServiceRepository.findById(subServiceId).orElseThrow();
        byId.setBasePrice(newBasePrice);
        return subServiceRepository.save(byId);
    }

    @Override
    @Transactional
    public SubService updateCaption(Long subServiceId, String newCaption) {
        SubService byId = subServiceRepository.findById(subServiceId).orElseThrow();
        byId.setCaption(newCaption);
        return subServiceRepository.save(byId);
    }
}
