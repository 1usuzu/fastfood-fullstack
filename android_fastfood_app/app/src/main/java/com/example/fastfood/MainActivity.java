package com.example.fastfood;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private static final String BASE_URL = "http://10.0.2.2:3000/"; // HOẶC IP CỤ THỂ CỦA BẠN
    private FoodAPI foodApi;
    private RecyclerView recyclerViewFoods; // Thêm biến cho RecyclerView
    private FoodAdapter foodAdapter; // Thêm biến cho Adapter
    private List<FoodModel> foodModelList; // Danh sách để giữ dữ liệu món ăn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo RecyclerView
        recyclerViewFoods = findViewById(R.id.rv_foods);
        recyclerViewFoods.setLayoutManager(new LinearLayoutManager(this)); // Thiết lập LayoutManager

        // Khởi tạo danh sách và adapter
        foodModelList = new ArrayList<>();
        foodAdapter = new FoodAdapter(foodModelList);
        recyclerViewFoods.setAdapter(foodAdapter); // Gán adapter cho RecyclerView

        // Khởi tạo Retrofit
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
                    // Cập nhật dữ liệu trong Adapter
                    foodAdapter.updateData(response.body());
                    Log.d("API_SUCCESS", "Đã tải thành công " + response.body().size() + " món ăn.");
                } else {
                    String errorMessage = "Không tải được dữ liệu món ăn.";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += " Lỗi từ server: " + response.code() + " - " + response.errorBody().string();
                        } catch (IOException e) {
                            Log.e("API_ERROR", "Lỗi khi đọc errorBody", e);
                            errorMessage += " Lỗi server: " + response.code();
                        }
                    } else {
                        errorMessage += " Mã lỗi: " + response.code();
                    }
                    Log.e("API_ERROR", errorMessage);
                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<FoodModel>> call, Throwable t) {
                Log.e("API_FAILURE", "Lỗi kết nối API hoặc xử lý request/response", t);
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}