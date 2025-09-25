package com.example.uberbookingservice.services;

import com.example.Uber_Entity.models.Booking;
import com.example.Uber_Entity.models.BookingStatus;
import com.example.Uber_Entity.models.Passenger;
import com.example.uberbookingservice.dtos.*;
import com.example.uberbookingservice.repositories.BookingRepository;
import com.example.uberbookingservice.repositories.PassengerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BookingServiceImp implements BookingService {
    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private  final  RestTemplate restTemplate;
    private  final  String LOCATION_SERVICE= "http://localhost:8080";

    public BookingServiceImp( BookingRepository bookingRepository, PassengerRepository passengerRepository, RestTemplate restTemplate) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public CreateBookingResponseDto createBooking(CreateBookingDto bookingDetails) {
        Optional<Passenger> passenger = passengerRepository.findById(bookingDetails.getPassengerId());
        Booking booking = Booking.builder()
                .passenger(passenger.get())
                .startLocation(bookingDetails.getStartLocation())
                .endLocation(bookingDetails.getEndLocation())
                .bookingStatus(BookingStatus.ASSIGNING_DRIVER)
                .build();
        Booking newbooking = bookingRepository.save(booking);

        NearbyDriverRequestDto request= NearbyDriverRequestDto.builder()
                .longitude(bookingDetails.getStartLocation().getLatitude())
                .longitude(bookingDetails.getStartLocation().getLongitude())
                .build();
        ResponseEntity<DriverLocationDto[]> result = restTemplate.postForEntity(LOCATION_SERVICE +"/api/location/nearby/drivers",request,DriverLocationDto[].class);
        if(result.getStatusCode().is2xxSuccessful() &&  result.getBody()!=null) {
            List<DriverLocationDto> driversLocations = Arrays.asList(result.getBody());
            for(DriverLocationDto driver: driversLocations){
                System.out.println("driver id is "+ driver.getDriverId()+"latitude " + driver.getLatitude()+"longitude"+ driver.getLongitude());
            }
        }
        return CreateBookingResponseDto.builder()
                .bookingId(newbooking.getId())
                .bookingStatus(newbooking.getBookingStatus().toString())
//                .driver(Optional.of(newbooking.getDriver()))
                .build();

    }

    @Override
    public UpdateBookingResponseDto updateBooking(UpdateBookingRequestDto bookingRequestDto, Long bookingId) {
        return null;
    }
}
