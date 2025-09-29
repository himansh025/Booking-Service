package com.example.uberbookingservice.repositories;

import com.example.Uber_Entity.models.Booking;
import com.example.Uber_Entity.models.BookingStatus;
import com.example.Uber_Entity.models.Driver;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.awt.print.Book;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    @Modifying
    @Transactional
    @Query("UPDATE BOOKING b SET b.bookingStatus=:status ,b.driver=:driver where b.id=:id")
    Booking updateBookingStatusAndDriverById(@Param( "id" ) Long id, @Param("status") BookingStatus status, @Param("driver")Driver driver);
}
