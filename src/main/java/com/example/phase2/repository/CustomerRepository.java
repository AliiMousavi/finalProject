package com.example.phase2.repository;


import com.example.phase2.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Customer saveOrUpdate(Customer customer);
    Optional<Customer> findById(Long id);
    void deleteById(Long id);

}
