//package com.example.phase2.service.impl;
//
//import com.example.phase2.entity.ServiceCo;
//import com.example.phase2.repository.ServiceCoRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//class ServiceCoServiceImplTest {
//    private ServiceCoServiceImpl serviceCoService;
//    private ServiceCoServiceImpl serviceCoServiceImpl;
//
//    @Mock
//    private ServiceCoRepository serviceCoRepository;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        serviceCoServiceImpl = new ServiceCoServiceImpl(serviceCoRepository);
//        serviceCoService = serviceCoServiceImpl;
//    }
//
//    @Test
//    public void testSaveOrUpdate() {
//        ServiceCo serviceCo = new ServiceCo();
//        serviceCo.setId(1L);
//
//        when(serviceCoRepository.save(serviceCo)).thenReturn(serviceCo);
//
//        ServiceCo result = serviceCoService.saveOrUpdate(serviceCo);
//
//        Assertions.assertEquals(serviceCo, result);
//        verify(serviceCoRepository, times(1)).save(serviceCo);
//    }
//
//    @Test
//    public void testFindById_ExistingId() {
//        Long id = 1L;
//        ServiceCo serviceCo = new ServiceCo();
//        serviceCo.setId(id);
//
//        when(serviceCoRepository.findById(id)).thenReturn(Optional.of(serviceCo));
//
//        Optional<ServiceCo> result = serviceCoService.findById(id);
//
//        Assertions.assertEquals(Optional.of(serviceCo), result);
//        verify(serviceCoRepository, times(1)).findById(id);
//    }
//
//    @Test
//    public void testFindById_NonExistingId() {
//        Long id = 1L;
//
//        when(serviceCoRepository.findById(id)).thenReturn(Optional.empty());
//
//        Optional<ServiceCo> result = serviceCoService.findById(id);
//
//        Assertions.assertEquals(Optional.empty(), result);
//        verify(serviceCoRepository, times(1)).findById(id);
//    }
//
//    @Test
//    public void testFindAll() {
//        List<ServiceCo> serviceCos = createServiceCos();
//
//        when(serviceCoRepository.findAll()).thenReturn(serviceCos);
//
//        List<ServiceCo> result = serviceCoService.findAll();
//
//        Assertions.assertEquals(serviceCos, result);
//        verify(serviceCoRepository, times(1)).findAll();
//    }
//
//    @Test
//    public void testSaveAll() {
//        List<ServiceCo> serviceCos = createServiceCos();
//
//        when(serviceCoRepository.saveAll(serviceCos)).thenReturn(serviceCos);
//
//        List<ServiceCo> result = serviceCoService.saveAll(serviceCos);
//
//        Assertions.assertEquals(serviceCos, result);
//        verify(serviceCoRepository, times(1)).saveAll(serviceCos);
//    }
//
//    @Test
//    public void testDeleteById() {
//        Long id = 1L;
//
//        serviceCoService.deleteById(id);
//
//        verify(serviceCoRepository, times(1)).deleteById(id);
//    }
//
//    private List<ServiceCo> createServiceCos() {
//        List<ServiceCo> serviceCos = new ArrayList<>();
//        ServiceCo serviceCo1 = new ServiceCo();
//        serviceCo1.setId(1L);
//        serviceCos.add(serviceCo1);
//        ServiceCo serviceCo2 = new ServiceCo();
//        serviceCo2.setId(2L);
//        serviceCos.add(serviceCo2);
//        return serviceCos;
//    }
//}