package com.example.uberbookingservice.dtos;

import com.example.Uber_Entity.models.ExactLocation;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideRequestDto {
    private  Long passengerId;
    private ExactLocation startLocation;
    private  ExactLocation endLocation;
    private List<Long> driverIds;
}
