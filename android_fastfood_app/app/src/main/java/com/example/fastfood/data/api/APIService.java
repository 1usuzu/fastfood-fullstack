package com.example.fastfood.data.api;

import com.example.fastfood.data.model.User;
import com.example.fastfood.data.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers("Content-Type: application/json")
    @POST("/register")
    Call<ApiResponse> registerUser(@Body User user);

    @Headers("Content-Type: application/json")
    @POST("/login")
    Call<ApiResponse> loginUser(@Body User user);
}

