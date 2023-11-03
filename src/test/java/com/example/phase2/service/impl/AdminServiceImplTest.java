package com.example.phase2.service.impl;


import com.example.phase2.entity.Expert;
import com.example.phase2.entity.ServiceCo;
import com.example.phase2.entity.SubService;
import com.example.phase2.entity.enumeration.ExpertStatus;
import com.example.phase2.repository.AdminRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {
    private AdminServiceImpl adminService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private ServiceCoServiceImpl serviceCoService;

    @Mock
    private SubServiceServiceImpl subServiceService;

    @Mock
    private ExpertServiceImpl expertService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        adminService = new AdminServiceImpl(adminRepository, serviceCoService, subServiceService, expertService);
    }

    @Test
    void addServiceCo() {
        ServiceCo serviceCo = new ServiceCo();
        when(serviceCoService.saveOrUpdate(any(ServiceCo.class))).thenReturn(serviceCo);

        ServiceCo result = adminService.addServiceCo(serviceCo);

        Assertions.assertEquals(serviceCo, result);
        verify(serviceCoService, times(1)).saveOrUpdate(serviceCo);
    }

    @Test
    void addSubService() {
        SubService subService = new SubService();
        when(subServiceService.saveOrUpdate(any(SubService.class))).thenReturn(subService);

        SubService result = adminService.addSubService(subService);

        Assertions.assertEquals(subService, result);
        verify(subServiceService, times(1)).saveOrUpdate(subService);
    }

    @Test
    void getAllServiceCo() {
        List<ServiceCo> serviceCoList = new ArrayList<>();
        when(serviceCoService.findAll()).thenReturn(serviceCoList);

        Collection<ServiceCo> result = adminService.getAllServiceCo();

        Assertions.assertEquals(serviceCoList, result);
        verify(serviceCoService, times(1)).findAll();
    }

    @Test
    void getAllSubService() {
        List<SubService> subServiceList = new ArrayList<>();
        when(subServiceService.findAll()).thenReturn(subServiceList);

        Collection<SubService> result = adminService.getAllSubService();

        Assertions.assertEquals(subServiceList, result);
        verify(subServiceService, times(1)).findAll();
    }

    @Test
    void updateBasePrice() {
        Long subServiceId = 1L;
        int newBasePrice = 100;
        SubService subService = new SubService();
        when(subServiceService.updateBasePrice(eq(subServiceId), eq(newBasePrice))).thenReturn(subService);

        SubService result = adminService.updateBasePrice(subServiceId, newBasePrice);

        Assertions.assertEquals(subService, result);
        verify(subServiceService, times(1)).updateBasePrice(subServiceId, newBasePrice);
    }

    @Test
    void updateCaption() {
        Long subServiceId = 1L;
        String newCaption = "New Caption";
        SubService subService = new SubService();
        when(subServiceService.updateCaption(eq(subServiceId), eq(newCaption))).thenReturn(subService);

        SubService result = adminService.updateCaption(subServiceId, newCaption);

        Assertions.assertEquals(subService, result);
        verify(subServiceService, times(1)).updateCaption(subServiceId, newCaption);
    }

    @Test
    void addExpertToSubService() {
        Expert expert = new Expert();
        SubService subService = new SubService();
        subService.setId(1L);

        SubService foundSubService = new SubService();
        foundSubService.setId(1L);
        List<Expert> experts = new ArrayList<>();
        foundSubService.setExperts(experts);

        when(subServiceService.findById(anyLong())).thenReturn(Optional.of(foundSubService));
        when(subServiceService.saveOrUpdate(any(SubService.class))).thenReturn(foundSubService);

        SubService result = adminService.addExpertToSubService(expert, subService);

        Assertions.assertEquals(foundSubService, result);
        Assertions.assertTrue(result.getExperts().contains(expert));
        verify(subServiceService, times(1)).findById(anyLong());
        verify(subServiceService, times(1)).saveOrUpdate(any(SubService.class));
    }

    @Test
    void deleteExpertToSubService() {
        Expert expert = new Expert();
        SubService subService = new SubService();
        subService.setId(1L);

        SubService foundSubService = new SubService();
        foundSubService.setId(1L);
        List<Expert> experts = new ArrayList<>();
        experts.add(expert);
        foundSubService.setExperts(experts);

        when(subServiceService.findById(anyLong())).thenReturn(Optional.of(foundSubService));
        when(subServiceService.saveOrUpdate(any(SubService.class))).thenReturn(foundSubService);

        SubService result = adminService.deleteExpertToSubService(expert, subService);

        Assertions.assertEquals(foundSubService, result);
        Assertions.assertFalse(result.getExperts().contains(expert));
        verify(subServiceService, times(1)).findById(anyLong());
        verify(subServiceService, times(1)).saveOrUpdate(any(SubService.class));
    }

    @Test
    void confirmExpertbyId() {
        Long id = 1L;
        Expert expert =new Expert();
        expert.setId(id);
        expert.setExpertStatus(ExpertStatus.NEW);

        when(expertService.findById(eq(id))).thenReturn(Optional.of(expert));
        when(expertService.saveOrUpdate(any(Expert.class))).thenReturn(expert);

        Expert result = adminService.confirmExpertbyId(id);

        Assertions.assertEquals(expert, result);
        Assertions.assertEquals(ExpertStatus.ACCEPTED, result.getExpertStatus());
        verify(expertService, times(1)).findById(eq(id));
        verify(expertService, times(1)).saveOrUpdate(any(Expert.class));
    }
}