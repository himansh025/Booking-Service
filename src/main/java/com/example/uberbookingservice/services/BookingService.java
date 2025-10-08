package com.example.uberbookingservice.services;

import com.example.uberbookingservice.dtos.*;

public interface BookingService {
    CreateBookingResponseDto createBooking(CreateBookingDto bookingDetails);

    UpdateBookingResponseDto updateBooking(UpdateBookingRequestDto bookingRequestDto, Long bookingId);

     void processNearbyDriversAsync(NearbyDriverRequestDto requestDto,Long passengerId,Long bookingId);

    void raiseRideRequestAsync(RideRequestDto requestDto);
}
