package com.example.phase2.repository;

import com.example.phase2.entity.Order;
import com.example.phase2.entity.ServiceCo;
import com.example.phase2.entity.SubService;
import com.example.phase2.entity.enumeration.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderSpecifications {

    public static Specification<Order> withinTimeRange(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("dateOfExecution"), startDate, endDate);
    }

    public static Specification<Order> byOrderStatus(OrderStatus orderStatus) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orderStatus"), orderStatus);
    }

    public static Specification<Order> bySubService(Long subServiceId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("subService").get("id"), subServiceId);
    }

    public static Specification<Order> byServiceCo(Long serviceCoId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("subService").get("serviceCo").get("id"), serviceCoId);
    }

}
