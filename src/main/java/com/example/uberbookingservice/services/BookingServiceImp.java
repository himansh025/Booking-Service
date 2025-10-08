package com.example.uberbookingservice.services;

import com.example.Uber_Entity.models.Booking;
import com.example.Uber_Entity.models.BookingStatus;
import com.example.Uber_Entity.models.Driver;
import com.example.Uber_Entity.models.Passenger;
import com.example.uberbookingservice.apis.LocationServiceApi;
import com.example.uberbookingservice.apis.UberSocketApi;
import com.example.uberbookingservice.dtos.*;
import com.example.uberbookingservice.repositories.BookingRepository;
import com.example.uberbookingservice.repositories.DriverRepository;
import com.example.uberbookingservice.repositories.PassengerRepository;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImp implements BookingService {
    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final LocationServiceApi locationServiceApi;
    private final DriverRepository driverRepository;
    private final UberSocketApi uberSocketApi;
//    private  final  String LOCATION_SERVICE= "http://localhost:8080";

    public BookingServiceImp(BookingRepository bookingRepository, PassengerRepository passengerRepository, LocationServiceApi locationServiceApi,
                             DriverRepository driverRepository, UberSocketApi uberSocketApi) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.locationServiceApi = locationServiceApi;
        this.driverRepository = driverRepository;
        this.uberSocketApi = uberSocketApi;
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

        processNearbyDriversAsync(nearbyDriverRequestDto, bookingDetails.getPassengerId(), newbooking.getId());
/*
        ResponseEntity<DriverLocationDto[]> result = restTemplate.postForEntity(LOCATION_SERVICE +"/api/location/nearby/drivers",request,DriverLocationDto[].class);
        if (response.isSuccessful() && response.body() != null) {
            List<DriverLocationDto> driversLocations = Arrays.asList(response.body());
            for (DriverLocationDto driver : driversLocations) {
                System.out.println("driver id is " + driver.getDriverId() + "latitude " + driver.getLatitude() + "longitude" + driver.getLongitude());
            }
            }
*/
        return CreateBookingResponseDto.builder()
                .bookingId(newbooking.getId())
                .bookingStatus(newbooking.getBookingStatus().toString())
                .driver(Optional.of(newbooking.getDriver()))
                .build();
    }

    @Override
    public UpdateBookingResponseDto updateBooking(UpdateBookingRequestDto bookingRequestDto, Long bookingId) {
        Optional<Driver> driver = this.driverRepository.findById(bookingRequestDto.getDriverId().get());
        if(driver.isPresent() && driver.get().isAvailable()){
        bookingRepository.updateBookingStatusAndDriverById(bookingId, bookingRequestDto.getStatus(), driver.get());
//        driverRepository.updateStatus("unavailable");
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        return UpdateBookingResponseDto.builder()
                .bookingId(bookingId)
                .status(booking.get().getBookingStatus())
                .driver(Optional.ofNullable(booking.get().getDriver()))
                .build();

    }
        return UpdateBookingResponseDto.builder().build();
}

    @Override
    public void processNearbyDriversAsync(NearbyDriverRequestDto requestDto, Long passengerId, Long bookingId) {
        Call<DriverLocationDto[]> call = locationServiceApi.getNearbyDrivers(requestDto);
        call.enqueue(new Callback<DriverLocationDto[]>() {
            @Override
            public void onResponse(Call<DriverLocationDto[]> call, Response<DriverLocationDto[]> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DriverLocationDto> driversLocations = Arrays.asList(response.body());
                    for (DriverLocationDto driver : driversLocations) {
                        System.out.println("driver id is " + driver.getDriverId() + "latitude " + driver.getLatitude() + "longitude" + driver.getLongitude());
                    }
                    try {
                        raiseRideRequestAsync(RideRequestDto
                                .builder()
                                .passengerId(passengerId)
                                .bookingId(bookingId)
                                .build());
                    } catch (Exception e) {
                        throw new  RuntimeException(e);
                    }
                } else {
                    System.out.println();
                }
            }

            @Override
            public void onFailure(Call<DriverLocationDto[]> call, Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
        });
    }

    @Override
    public void raiseRideRequestAsync(RideRequestDto requestDto) {
        Call<Boolean> call = uberSocketApi.getNewRides(requestDto);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boolean result = response.body();
                    System.out.println(result);
                } else {
                    System.out.println("req in uber socket failed" + response.message());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
        });
    }
}

