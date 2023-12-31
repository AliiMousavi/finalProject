package com.example.phase2.service.impl;


import com.example.phase2.entity.ServiceCo;
import com.example.phase2.repository.ServiceCoRepository;
import com.example.phase2.service.ServiceCoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class ServiceCoServiceImpl implements ServiceCoService {
    private final ServiceCoRepository serviceCoRepository;

    public ServiceCoServiceImpl(ServiceCoRepository serviceCoRepository) {
        this.serviceCoRepository = serviceCoRepository;
    }

    @Override
    @Transactional
    public ServiceCo saveOrUpdate(ServiceCo service) {
        return serviceCoRepository.save(service);
    }

    @Override
    @Transactional
    public ServiceCo update(ServiceCo service) {
        try{
            return serviceCoRepository.save(service);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Optional<ServiceCo> findById(Long id) {
        return serviceCoRepository.findById(id);
    }

    @Override
    @Transactional
    public List<ServiceCo> findAll() {
        return serviceCoRepository.findAll();
    }

    @Override
    @Transactional
    public List<ServiceCo> saveAll(List<ServiceCo> serviceCos) {
        return serviceCoRepository.saveAll(serviceCos);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        serviceCoRepository.deleteById(id);
    }
}
