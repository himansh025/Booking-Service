package com.example.uberbookingservice.services;

import com.example.uberbookingservice.dtos.CreateBookingDto;
import com.example.uberbookingservice.dtos.CreateBookingResponseDto;
import com.example.uberbookingservice.dtos.UpdateBookingRequestDto;
import com.example.uberbookingservice.dtos.UpdateBookingResponseDto;
import org.springframework.stereotype.Service;

public interface BookingService {
    CreateBookingResponseDto createBooking(CreateBookingDto bookingDetails);

    UpdateBookingResponseDto updateBooking(UpdateBookingRequestDto bookingRequestDto, Long bookingId);

}
