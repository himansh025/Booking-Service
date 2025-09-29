package com.example.uberbookingservice.repositories;

import com.example.Uber_Entity.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

    public interface DriverRepository extends JpaRepository<Driver, Long> {
    }

