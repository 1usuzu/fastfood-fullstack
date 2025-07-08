package com.example.fastfood.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastfood.R;
import com.example.fastfood.adapter.FoodAdapter;
import com.example.fastfood.data.api.FoodAPI;
import com.example.fastfood.data.model.FoodModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://10.0.2.2:3000/";

    private FoodAPI foodApi;
    private RecyclerView recyclerViewFoods;
    private FoodAdapter foodAdapter;
    private List<FoodModel> foodModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewFoods.setLayoutManager(new LinearLayoutManager(this));

        foodModelList = new ArrayList<>();
        foodAdapter = new FoodAdapter(this, foodModelList, new FoodAdapter.OnItemAddListener() {
            @Override
            public void onItemAdd(FoodModel food) {
                Toast.makeText(MainActivity.this, "Thêm món: " + food.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(FoodModel food) {
                Toast.makeText(MainActivity.this, "Xem món: " + food.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerViewFoods.setAdapter(foodAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        foodApi = retrofit.create(FoodAPI.class);

        loadFoods();
    }

    private void loadFoods() {
        Call<List<FoodModel>> call = foodApi.getFoods();
        call.enqueue(new Callback<List<FoodModel>>() {
            @Override
            public void onResponse(Call<List<FoodModel>> call, Response<List<FoodModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    foodAdapter.updateData(response.body());
                    Log.d("API_SUCCESS", "\u0110ã tải " + response.body().size() + " món ăn.");
                } else {
                    String errorMessage = "Không tải được dữ liệu món ăn.";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += " Server trả về lỗi: " + response.code() + " - " + response.errorBody().string();
                        } catch (IOException e) {
                            Log.e("API_ERROR", "Lỗi đọc errorBody", e);
                            errorMessage += " Mã lỗi: " + response.code();
                        }
                    }
                    Log.e("API_ERROR", errorMessage);
                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<FoodModel>> call, Throwable t) {
                Log.e("API_FAILURE", "Lỗi kết nối hoặc xử lý", t);
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}