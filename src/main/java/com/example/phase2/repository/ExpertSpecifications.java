package com.example.phase2.repository;

import com.example.phase2.entity.Expert;
import com.example.phase2.entity.Order;
import com.example.phase2.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ExpertSpecifications {

    public static Specification<Expert> withSubService(Long subServiceId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("subServices").get("id") , subServiceId);
    }

    public static Specification<Expert> withHighestScore() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("score")));
            return criteriaBuilder.conjunction();
        };
    }


    public static Specification<Expert> withLowestScore() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("score")));
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Expert> byNumOrdersDone(Long NumberOfOrders ){
        return (root, query, criteriaBuilder) -> {
            Join<Expert, Order> ordersJoin = root.join("orders", JoinType.LEFT);
            query.groupBy(root.get("id"));
            return criteriaBuilder.equal(criteriaBuilder.count(ordersJoin), NumberOfOrders);
        };
    }
}
