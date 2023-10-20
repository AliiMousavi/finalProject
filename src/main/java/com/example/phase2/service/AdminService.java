package com.example.phase2.service;

import com.example.phase2.entity.Expert;
import com.example.phase2.entity.ServiceCo;
import com.example.phase2.entity.SubService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface AdminService{
    ServiceCo addServiceCo(ServiceCo serviceCo);
    SubService addSubService(SubService subService);
    Collection<ServiceCo> getAllServiceCo();
    Collection<SubService> getAllSubService();
    SubService updateBasePrice(Long subServiceId, int newBasePrice);
    SubService updateCaption(Long subServiceId, String newCaption);
    SubService addExpertToSubService(Expert expert, SubService subService);
    SubService deleteExpertToSubService(Expert expert,SubService subService);
    Expert confirmExpertbyId(Long id);
}
