package com.example.uberbookingservice.apis;

import com.example.uberbookingservice.dtos.RideRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UberSocketApi {
    @POST("/api/socket/newride")
    Call<Boolean> getNewRides(@Body RideRequestDto requestDto);
}
