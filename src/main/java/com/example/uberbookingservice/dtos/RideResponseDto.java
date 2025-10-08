package com.example.uberbookingservice.dtos;

import com.example.Uber_Entity.models.ExactLocation;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideResponseDto {    private  Long passengerId;
    private  Long bookingId;
    private Long driverId;
    private ExactLocation startLocation;
    private  ExactLocation endLocation;
    private List<Long> driverIds;
}
