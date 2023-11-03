package com.example.phase2.service.impl;

import com.example.phase2.entity.SubService;
import com.example.phase2.repository.SubServiceRepository;
import com.example.phase2.service.SubServiceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SubServiceServiceImplTest {
    private SubServiceService subServiceService;
    private SubServiceServiceImpl subServiceServiceImpl;

    @Mock
    private SubServiceRepository subServiceRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        subServiceServiceImpl = new SubServiceServiceImpl(subServiceRepository);
        subServiceService = subServiceServiceImpl;
    }

    @Test
    public void testSaveOrUpdate() {
        SubService subService = new SubService();
        subService.setId(1L);

        when(subServiceRepository.save(subService)).thenReturn(subService);

        SubService result = subServiceService.saveOrUpdate(subService);

        Assertions.assertEquals(subService, result);
        verify(subServiceRepository, times(1)).save(subService);
    }

    @Test
    public void testFindById_ExistingId() {
        Long id = 1L;
        SubService subService = new SubService();
        subService.setId(id);

        when(subServiceRepository.findById(id)).thenReturn(Optional.of(subService));

        Optional<SubService> result = subServiceService.findById(id);

        Assertions.assertEquals(Optional.of(subService), result);
        verify(subServiceRepository, times(1)).findById(id);
    }

    @Test
    public void testFindById_NonExistingId() {
        Long id = 1L;

        when(subServiceRepository.findById(id)).thenReturn(Optional.empty());

        Optional<SubService> result = subServiceService.findById(id);

        Assertions.assertEquals(Optional.empty(), result);
        verify(subServiceRepository, times(1)).findById(id);
    }

    @Test
    public void testFindAll() {
        List<SubService> subServices = createSubServices();

        when(subServiceRepository.findAll()).thenReturn(subServices);

        List<SubService> result = subServiceService.findAll();

        Assertions.assertEquals(subServices, result);
        verify(subServiceRepository, times(1)).findAll();
    }

    @Test
    public void testSaveAll() {
        List<SubService> subServices = createSubServices();

        when(subServiceRepository.saveAll(subServices)).thenReturn(subServices);

        List<SubService> result = subServiceService.saveAll(subServices);

        Assertions.assertEquals(subServices, result);
        verify(subServiceRepository, times(1)).saveAll(subServices);
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;

        subServiceService.deleteById(id);

        verify(subServiceRepository, times(1)).deleteById(id);
    }

    @Test
    public void testUpdateBasePrice() {
        Long subServiceId = 1L;
        int newBasePrice = 100;

        SubService subService = new SubService();
        subService.setId(subServiceId);
        subService.setBasePrice(50);

        when(subServiceRepository.findById(subServiceId)).thenReturn(Optional.of(subService));
        when(subServiceRepository.save(subService)).thenReturn(subService);

        SubService result = subServiceService.updateBasePrice(subServiceId, newBasePrice);

        Assertions.assertEquals(newBasePrice, result.getBasePrice());
        verify(subServiceRepository, times(1)).findById(subServiceId);
        verify(subServiceRepository, times(1)).save(subService);
    }

    @Test
    public void testUpdateCaption() {
        Long subServiceId = 1L;
        String newCaption = "New Caption";

        SubService subService = new SubService();
        subService.setId(subServiceId);
        subService.setCaption("Old Caption");

        when(subServiceRepository.findById(subServiceId)).thenReturn(Optional.of(subService));
        when(subServiceRepository.save(subService)).thenReturn(subService);

        SubService result = subServiceService.updateCaption(subServiceId, newCaption);

        Assertions.assertEquals(newCaption, result.getCaption());
        verify(subServiceRepository, times(1)).findById(subServiceId);
        verify(subServiceRepository, times(1)).save(subService);
    }

    private List<SubService> createSubServices() {
        List<SubService> subServices = new ArrayList<>();
        SubService subService1 = new SubService();
        subService1.setId(1L);
        subServices.add(subService1);
        SubService subService2 = new SubService();
        subService2.setId(2L);
        subServices.add(subService2);
        return subServices;
    }
}