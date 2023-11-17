package com.example.phase2.service.impl;


import com.example.phase2.dto.OrderFilterCriteria;
import com.example.phase2.entity.*;
import com.example.phase2.entity.enumeration.ExpertStatus;
import com.example.phase2.entity.enumeration.OrderStatus;
import com.example.phase2.exception.NotFoundException;
import com.example.phase2.repository.*;
import com.example.phase2.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final ServiceCoServiceImpl serviceCoService;
    private final SubServiceServiceImpl subServiceService;
    private final ExpertServiceImpl expertService;
    private final CustomerServiceImpl customerService;
    private final PasswordEncoder passwordEncoder;
    private final CustomerSubServiceHistory customerSubServiceHistory;
    private final ExpertSubServiceHistory expertSubServiceHistory;
    private final OrderRepository orderRepository;
    private final ExpertRepository expertRepository;
    private final CustomerRepository customerRepository;



    @Override
    public Admin saveOrUpdate(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
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
        subService.getExperts().add(expert);
        expert.getSubServices().add(subService);
        expertService.update(expert);
        return subServiceService.update(subService);
    }

    @Override
    @Transactional
    public SubService deleteExpertToSubService(Expert expert, SubService subService) {
        SubService subService1 = subServiceService.findById(subService.getId()).orElseThrow();
        subService1.getExperts().remove(expert);
        return subServiceService.update(subService1);
    }

    @Override
    @Transactional
    public Expert confirmExpertbyId(Long id) {
        Expert expert = expertService.findById(id).orElseThrow();
        expert.setExpertStatus(ExpertStatus.ACCEPTED);
        return expertService.update(expert);
    }

    @Transactional
    public CustomerSubServiceHistory getCustomerSubServiceHistory(Long customerId){
        Customer customer = customerService.findById(customerId).orElseThrow(
                () -> new NotFoundException("customer not found")
        );
        List<Order> orders = customer.getOrders();

        customerSubServiceHistory.setCustomer(customer);
        customerSubServiceHistory.getOrders().addAll(orders);

        return customerSubServiceHistory;

    }

    @Transactional
    public ExpertSubServiceHistory getExpertSubServiceHistory(Long expertId){
        Expert expert = expertService.findById(expertId).orElseThrow(
                () -> new NotFoundException("expert not found")
        );
        List<Order> orders = expert.getOrders();

        expertSubServiceHistory.setExpert(expert);
        expertSubServiceHistory.getOrders().addAll(orders);

        return expertSubServiceHistory;

    }

    @Transactional
    public Page<Order> getOrdersByCriteria(OrderFilterCriteria orderFilterCriteria , Pageable pageable) {
        Specification<Order> specification = Specification.where(null);

        if (orderFilterCriteria.getStartDate() != null && orderFilterCriteria.getEndDate() != null) {
            specification = specification.and(OrderSpecifications.withinTimeRange(orderFilterCriteria.getStartDate(), orderFilterCriteria.getEndDate()));
        }

        if (orderFilterCriteria.getOrderStatus() != null) {
            specification = specification.and(OrderSpecifications.byOrderStatus(orderFilterCriteria.getOrderStatus()));
        }

        if (orderFilterCriteria.getSubServiceId() != null) {
            specification = specification.and(OrderSpecifications.bySubService(orderFilterCriteria.getSubServiceId()));
        }

        if (orderFilterCriteria.getServiceCoId() != null) {
            specification = specification.and(OrderSpecifications.byServiceCo(orderFilterCriteria.getServiceCoId()));
        }

        return orderRepository.findAll(specification , pageable);
    }

    @Transactional
    public List<Expert> getExpertsByNumberOfOrders(int numberOfOrders) {
        return expertRepository.findByNumberOfOrders(numberOfOrders);
    }

    @Transactional
    public List<Customer> getCustomersByNumberOfOrders(int numberOfOrders) {
        return customerRepository.findByNumberOfOrders(numberOfOrders);
    }
}
