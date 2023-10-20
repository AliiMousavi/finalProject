package com.example.phase2.service;





import com.example.phase2.entity.ServiceCo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ServiceCoService {
    ServiceCo saveOrUpdate(ServiceCo service);
    Optional<ServiceCo> findById(Long id);
    List<ServiceCo> findAll();
    List<ServiceCo> saveAll(List<ServiceCo> serviceCos);
    void deleteById(Long id);
}
