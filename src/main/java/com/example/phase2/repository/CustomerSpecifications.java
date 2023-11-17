package com.example.phase2.repository;

import com.example.phase2.entity.Customer;
import com.example.phase2.entity.Order;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecifications {

    public static Specification<Customer> byNumOrdersRegistered(Long NumberOfOrders) {
        return (root, query, criteriaBuilder) -> {
            Join<Customer, Order> ordersJoin = root.join("orders", JoinType.LEFT);
            query.groupBy(root.get("id"));
            return criteriaBuilder.equal(criteriaBuilder.count(ordersJoin), NumberOfOrders);
        };
    }
}
