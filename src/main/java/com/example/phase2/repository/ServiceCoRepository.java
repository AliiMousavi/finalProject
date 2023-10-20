package com.example.phase2.repository;


import com.example.phase2.entity.ServiceCo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCoRepository extends JpaRepository<ServiceCo,Long> {
}
