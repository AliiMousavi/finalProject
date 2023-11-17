package com.example.phase2.repository;

import com.example.phase2.entity.SubService;
import org.springframework.data.jpa.domain.Specification;

public class SubServiceSpecifications {

    public static Specification<SubService> forExpert(Long expertId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("experts").get("id") , expertId);
    }

    public static Specification<SubService> forCustomer(Long customerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("orders").get("customer").get("id") , customerId);
    }
}
