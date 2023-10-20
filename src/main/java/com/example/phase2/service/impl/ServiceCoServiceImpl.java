package com.example.phase2.service.impl;


import com.example.phase2.entity.ServiceCo;
import com.example.phase2.repository.ServiceCoRepository;
import com.example.phase2.service.ServiceCoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ServiceCoServiceImpl implements ServiceCoService {
    private final ServiceCoRepository serviceCoRepository;

    public ServiceCoServiceImpl(ServiceCoRepository serviceCoRepository) {
        this.serviceCoRepository = serviceCoRepository;
    }

    @Override
    public ServiceCo saveOrUpdate(ServiceCo service) {
        return serviceCoRepository.save(service);
    }

    @Override
    public Optional<ServiceCo> findById(Long id) {
        return serviceCoRepository.findById(id);
    }

    @Override
    public List<ServiceCo> findAll() {
        return serviceCoRepository.findAll();
    }

    @Override
    public List<ServiceCo> saveAll(List<ServiceCo> serviceCos) {
        return serviceCoRepository.saveAll(serviceCos);
    }

    @Override
    public void deleteById(Long id) {
        serviceCoRepository.deleteById(id);
    }
}
