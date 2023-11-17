package com.example.phase2.repository;


import com.example.phase2.entity.Customer;
import com.example.phase2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findCustomerByEmail(String email);
    Optional<Customer> findCustomerByActivationToken(String activationToken);

    @Query(value = "SELECT c.* FROM Users c LEFT JOIN Orders o ON c.id = o.customer_id "
            + "GROUP BY c.id HAVING COUNT(o.id) = :numberOfOrders", nativeQuery = true)
    List<Customer> findByNumberOfOrders(int numberOfOrders);

}
