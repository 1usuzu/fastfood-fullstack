// UserApi.java
package com.example.fastfood.data.api;

import com.example.fastfood.data.model.ApiResponse;
import com.example.fastfood.data.model.UserLoginRequest;
import com.example.fastfood.data.model.UserResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {
    
    @POST("register")
    Call<ApiResponse> registerUser(@Body Map<String, String> body);
    // Ví dụ body: { "name":"Bảo", "phone":"0123456789", "password":"123456" }

    @POST("login")
    Call<UserResponse> loginUser(@Body UserLoginRequest request);
    // UserLoginRequest chứa phone + password

    @POST("forgot-password/send-otp")
    Call<ApiResponse> sendOtp(@Body Map<String, String> body);
    // body: { "phone":"0123456789" }

    @POST("forgot-password/verify-otp")
    Call<ApiResponse> verifyOtp(@Body Map<String, String> body);
    // body: { "phone":"0123456789", "otp":"123456" }

    @POST("forgot-password/reset")
    Call<ApiResponse> resetPassword(@Body Map<String, String> body);
    // body: { "phone":"0123456789", "newPassword":"abcdef" }
}
