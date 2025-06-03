package com.example.fastfood;

import com.example.fastfood.FoodModel;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface FoodAPI {
    @GET("/foods")
    Call<List<FoodModel>> getFoods();
}
