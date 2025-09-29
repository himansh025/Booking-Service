package com.example.uberbookingservice.services;

import com.example.Uber_Entity.models.Booking;
import com.example.Uber_Entity.models.BookingStatus;
import com.example.Uber_Entity.models.Driver;
import com.example.Uber_Entity.models.Passenger;
import com.example.uberbookingservice.apis.LocationServiceApi;
import com.example.uberbookingservice.dtos.*;
import com.example.uberbookingservice.repositories.BookingRepository;
import com.example.uberbookingservice.repositories.DriverRepository;
import com.example.uberbookingservice.repositories.PassengerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.awt.print.Book;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImp implements BookingService {
    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final RestTemplate restTemplate;
    private final LocationServiceApi locationServiceApi;
    private final DriverRepository driverRepository;
//    private  final  String LOCATION_SERVICE= "http://localhost:8080";

    public BookingServiceImp(BookingRepository bookingRepository, PassengerRepository passengerRepository, RestTemplate restTemplate, LocationServiceApi locationServiceApi,
                             DriverRepository driverRepository) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.restTemplate = restTemplate;
        this.locationServiceApi = locationServiceApi;
        this.driverRepository = driverRepository;
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
        NearbyDriverRequestDto nearbyDriverRequestDto = NearbyDriverRequestDto.builder()
                .latitude(bookingDetails.getStartLocation().getLatitude())
                .longitude(bookingDetails.getStartLocation().getLongitude())
                .build();

        processNearbyDriverAsync(nearbyDriverRequestDto);
//        ResponseEntity<DriverLocationDto[]> result = restTemplate.postForEntity(LOCATION_SERVICE +"/api/location/nearby/drivers",request,DriverLocationDto[].class);
//        if (response.isSuccessful() && response.body() != null) {
//            List<DriverLocationDto> driversLocations = Arrays.asList(response.body());
//            for (DriverLocationDto driver : driversLocations) {
//                System.out.println("driver id is " + driver.getDriverId() + "latitude " + driver.getLatitude() + "longitude" + driver.getLongitude());
//            }
//            }

        return CreateBookingResponseDto.builder()
                .bookingId(newbooking.getId())
                .bookingStatus(newbooking.getBookingStatus().toString())
                .driver(Optional.of(newbooking.getDriver()))
                .build();


    }
    public void processNearbyDriverAsync(NearbyDriverRequestDto reqDto) {
        Call<DriverLocationDto[]> call = locationServiceApi.getNearbyDrivers(reqDto);
        call.enqueue(new Callback<DriverLocationDto[]>() {
            @Override
            public void onResponse(Call<DriverLocationDto[]> call, Response<DriverLocationDto[]> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DriverLocationDto> driversLocations = Arrays.asList(response.body());
                    for (DriverLocationDto driver : driversLocations) {
                        System.out.println("driver id is " + driver.getDriverId() + "latitude " + driver.getLatitude() + "longitude" + driver.getLongitude());
                    }
                    ;
                } else {
                    System.out.println("resposne failed" + response.message());
                }
            }
                @Override
                public void onFailure (Call < DriverLocationDto[]>call, Throwable t){
                    t.printStackTrace();
                }
            });
        }

        @Override
        public UpdateBookingResponseDto updateBooking (UpdateBookingRequestDto bookingRequestDto, Long bookingId){
        Optional<Driver> driver=this.driverRepository.findById(bookingRequestDto.getDriverId().get());
             bookingRepository.updateBookingStatusAndDriverById(bookingId, bookingRequestDto.getStatus(),driver.get());
            Optional<Booking> booking = bookingRepository.findById(bookingId);
          return  UpdateBookingResponseDto.builder()
                    .bookingId(bookingId)
                    .status(booking.get().getBookingStatus())
                    .driver(Optional.ofNullable(booking.get().getDriver()))
                    .build();

    }

    }
