package com.example.phase2.repository;



import com.example.phase2.entity.Customer;
import com.example.phase2.entity.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpertRepository extends JpaRepository<Expert,Long>, JpaSpecificationExecutor<Expert> {
    Optional<Expert> findExpertByEmail(String email);

    Optional<Expert> findExpertByActivationToken(String activationToken);

    @Query(value = "SELECT c.* FROM Users c LEFT JOIN Orders o ON c.id = o.expert_id "
            + "GROUP BY c.id HAVING COUNT(o.id) = :numberOfOrders", nativeQuery = true)
    List<Expert> findByNumberOfOrders(int numberOfOrders);
}
