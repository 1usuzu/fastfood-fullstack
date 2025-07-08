package com.example.fastfood.data.api;

import com.example.fastfood.data.model.ResetPasswordRequest;
import com.example.fastfood.data.model.User;
import com.example.fastfood.data.model.ApiResponse;
import com.example.fastfood.data.model.OtpRequest;
import com.example.fastfood.data.model.OrderHistoryResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {

    // Đăng ký
    @Headers("Content-Type: application/json")
    @POST("/register")
    Call<ApiResponse> registerUser(@Body User user);

    // Đăng nhập
    @Headers("Content-Type: application/json")
    @POST("/login")
    Call<ApiResponse> loginUser(@Body User user);

    // Gửi OTP quên mật khẩu
    @Headers("Content-Type: application/json")
    @POST("/forgot-password")
    Call<ApiResponse> sendOtp(@Body OtpRequest request);

    // Xác minh OTP
    @Headers("Content-Type: application/json")
    @POST("/verify-otp")
    Call<ApiResponse> verifyOtp(@Body OtpRequest request);

    // Đặt lại mật khẩu
    @Headers("Content-Type: application/json")
    @POST("/reset-password")
    Call<ApiResponse> resetPassword(@Body ResetPasswordRequest request);

    // Lấy lịch sử đơn hàng theo userId
    @GET("/orders/user/{userId}")
    Call<OrderHistoryResponse> getOrderHistory(@Path("userId") int userId);
}
