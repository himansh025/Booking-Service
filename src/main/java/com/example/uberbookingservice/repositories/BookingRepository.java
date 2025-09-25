package com.example.uberbookingservice.repositories;

import com.example.Uber_Entity.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {
}
