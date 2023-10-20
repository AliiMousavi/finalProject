package com.example.phase2.service.impl;


import com.example.phase2.entity.Expert;
import com.example.phase2.entity.ServiceCo;
import com.example.phase2.entity.SubService;
import com.example.phase2.entity.enumeration.ExpertStatus;
import com.example.phase2.repository.AdminRepository;
import com.example.phase2.repository.ExpertRepository;
import com.example.phase2.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    private final ServiceCoServiceImpl serviceCoService;

    private final SubServiceServiceImpl subServiceService;
    private final ExpertRepository expertRepository;

    public AdminServiceImpl(AdminRepository adminRepository,
                            ServiceCoServiceImpl serviceCoService,
                            SubServiceServiceImpl subServiceService,
                            ExpertRepository expertRepository) {
        this.adminRepository = adminRepository;
        this.serviceCoService = serviceCoService;
        this.subServiceService = subServiceService;
        this.expertRepository = expertRepository;
    }

    @Override
    public ServiceCo addServiceCo(ServiceCo serviceCo) {
        return serviceCoService.saveOrUpdate(serviceCo);
    }

    @Override
    public SubService addSubService(SubService subService) {
        return subServiceService.saveOrUpdate(subService);
    }

    @Override
    public Collection<ServiceCo> getAllServiceCo() {
        return serviceCoService.findAll();
    }

    @Override
    public Collection<SubService> getAllSubService() {
        return subServiceService.findAll();
    }

    @Override
    public SubService updateBasePrice(Long subServiceId, int newBasePrice) {
        return subServiceService.updateBasePrice(subServiceId,newBasePrice);
    }

    @Override
    public SubService updateCaption(Long subServiceId, String newCaption) {
        return subServiceService.updateCaption(subServiceId,newCaption);
    }

    @Override
    public SubService addExpertToSubService(Expert expert, SubService subService) {
        SubService subService1 = subServiceService.findById(subService.getId()).orElseThrow();
        List<Expert> experts = new ArrayList<>();
        experts.add(expert);
        subService1.setExperts(experts);
        return subServiceService.saveOrUpdate(subService1);
    }

    @Override
    public SubService deleteExpertToSubService(Expert expert, SubService subService) {
        subService.getExperts().remove(expert);
        return subServiceService.saveOrUpdate(subService);
    }

    @Override
    public Expert confirmExpertbyId(Long id) {
        Expert expert = expertRepository.findById(id).orElseThrow();
        expert.setExpertStatus(ExpertStatus.ACCEPTED);
        return expertRepository.save(expert);
    }
}
