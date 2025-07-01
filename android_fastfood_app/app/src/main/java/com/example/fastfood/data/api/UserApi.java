package com.example.fastfood.data.api;

import com.example.fastfood.data.model.UserLoginRequest;
import com.example.fastfood.data.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {
    @POST("/login")
    Call<UserResponse> loginUser(@Body UserLoginRequest request);
}
