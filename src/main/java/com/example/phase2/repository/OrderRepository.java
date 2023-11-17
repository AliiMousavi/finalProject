package com.example.phase2.repository;


import com.example.phase2.entity.Expert;
import com.example.phase2.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> , JpaSpecificationExecutor<Order> {

    Optional<Order> findOrderByAcceptedOffer_OfferedPrice(int offeredPrice);
}
