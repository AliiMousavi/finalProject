package com.example.phase2.service.impl;


import com.example.phase2.entity.Expert;
import com.example.phase2.entity.ServiceCo;
import com.example.phase2.entity.SubService;
import com.example.phase2.entity.enumeration.ExpertStatus;
import com.example.phase2.repository.AdminRepository;
import com.example.phase2.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    private final ServiceCoServiceImpl serviceCoService;

    private final SubServiceServiceImpl subServiceService;
    private final ExpertServiceImpl expertService;

    public AdminServiceImpl(AdminRepository adminRepository,
                            ServiceCoServiceImpl serviceCoService,
                            SubServiceServiceImpl subServiceService, ExpertServiceImpl expertService) {
        this.adminRepository = adminRepository;
        this.serviceCoService = serviceCoService;
        this.subServiceService = subServiceService;
        this.expertService = expertService;
    }

    @Override
    @Transactional
    public ServiceCo addServiceCo(ServiceCo serviceCo) {
        return serviceCoService.saveOrUpdate(serviceCo);
    }

    @Override
    @Transactional
    public SubService addSubService(SubService subService) {
        return subServiceService.saveOrUpdate(subService);
    }

    @Override
    @Transactional
    public Collection<ServiceCo> getAllServiceCo() {
        return serviceCoService.findAll();
    }

    @Override
    @Transactional
    public Collection<SubService> getAllSubService() {
        return subServiceService.findAll();
    }

    @Override
    @Transactional
    public SubService updateBasePrice(Long subServiceId, int newBasePrice) {
        return subServiceService.updateBasePrice(subServiceId,newBasePrice);
    }

    @Override
    @Transactional
    public SubService updateCaption(Long subServiceId, String newCaption) {
        return subServiceService.updateCaption(subServiceId,newCaption);
    }

    @Override
    @Transactional
    public SubService addExpertToSubService(Expert expert, SubService subService) {
        SubService subService1 = subServiceService.findById(subService.getId()).orElseThrow();
        subService1.getExperts().add(expert);
        return subServiceService.saveOrUpdate(subService1);
    }

    @Override
    @Transactional
    public SubService deleteExpertToSubService(Expert expert, SubService subService) {
        SubService subService1 = subServiceService.findById(subService.getId()).orElseThrow();
        subService1.getExperts().remove(expert);
        return subServiceService.saveOrUpdate(subService1);
    }

    @Override
    @Transactional
    public Expert confirmExpertbyId(Long id) {
        Expert expert = expertService.findById(id).orElseThrow();
        expert.setExpertStatus(ExpertStatus.ACCEPTED);
        return expertService.saveOrUpdate(expert);
    }
}
