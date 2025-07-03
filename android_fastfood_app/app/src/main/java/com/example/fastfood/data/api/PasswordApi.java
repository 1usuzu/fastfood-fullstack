package com.example.fastfood.data.api;

import com.example.fastfood.data.model.ApiResponse;
import com.example.fastfood.data.model.OtpRequest;
import com.example.fastfood.data.model.ResetPasswordRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PasswordApi {

    // Gửi OTP đến số điện thoại
    @POST("/forgot-password/send-otp")
    Call<ApiResponse> sendOtp(@Query("phone") String phone);

    // Xác minh OTP
    @POST("/forgot-password/verify-otp")
    Call<ApiResponse> verifyOtp(@Body OtpRequest request);

    // Đặt lại mật khẩu
    @POST("/forgot-password/reset")
    Call<ApiResponse> resetPassword(@Body ResetPasswordRequest request);
}
