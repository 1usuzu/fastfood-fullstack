package com.example.fastfood.data.api;

import com.example.fastfood.data.model.FoodModel;
import java.util.List;
import com.example.fastfood.data.model.User;
import com.example.fastfood.data.model.ChangePasswordRequest;
import com.example.fastfood.data.model.ShopInfo;
import com.example.fastfood.data.model.SupportRequest;
import com.example.fastfood.data.model.PaymentAccount;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.*;


public interface FoodAPI {
    @GET("/foods")
    Call<List<FoodModel>> getFoods();
    @GET("api/user/{id}")
    Call<User> getUser(@Path("id") String userId);

    @PUT("api/user/{id}")
    Call<User> updateUser(@Path("id") String userId, @Body User user);

    @POST("api/user/{id}/change-password")
    Call<ResponseBody> changePassword(@Path("id") String userId, @Body ChangePasswordRequest request);

    @GET("/api/shops")
    Call<ShopInfo> getShopInfo();

    @POST("/api/support-request")
    Call<ResponseBody> sendSupportRequest(@Body SupportRequest request);

    @GET("/api/payment-account/{userPhone}")
    Call<List<PaymentAccount>> getPaymentAccounts(@Path("userPhone") String userPhone);

    @POST("/api/payment-accounts")
    Call<ResponseBody> addPaymentAccount(@Body PaymentAccount paymentAccount);

    @DELETE("/api/payment-account/delete/{id}")
    Call<Void> deletePaymentAccount(@Path("id") String id);
}
