package com.example.fastfood.data.api;

import com.example.fastfood.data.model.FoodModel;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface FoodAPI {
    @GET("/foods")
    Call<List<FoodModel>> getFoods();
}
