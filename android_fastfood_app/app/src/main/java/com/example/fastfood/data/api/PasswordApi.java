package com.example.fastfood.data.api;

import com.example.fastfood.data.model.ApiResponse;
import com.example.fastfood.data.model.PhoneRequest;
import com.example.fastfood.data.model.OtpRequest;
import com.example.fastfood.data.model.ResetPasswordRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PasswordApi {

    @POST("/send-otp")
    Call<ApiResponse> sendOtp(@Body PhoneRequest phoneRequest);

    @POST("/verify-otp")
    Call<ApiResponse> verifyOtp(@Body OtpRequest otpRequest);

    @POST("/reset-password")
    Call<ApiResponse> resetPassword(@Body ResetPasswordRequest resetRequest);
}
