package com.example.fastfood.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastfood.R;
import com.example.fastfood.adapter.FoodAdapter;
// THÊM IMPORT
import com.example.fastfood.data.api.SessionManager;
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

public class OrderHistoryActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://10.0.2.2:3000/";
    private RecyclerView recyclerViewFoods;
    private FoodAdapter foodAdapter;
    private List<FoodModel> foodModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ BỔ SUNG ĐOẠN KIỂM TRA ĐĂNG NHẬP VÀO ĐÂY
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();

        // --- Cấu trúc code cũ của bạn được giữ nguyên từ đây trở xuống ---
        setContentView(R.layout.activity_order_history);

        recyclerViewFoods = findViewById(R.id.rv_order_history);
        recyclerViewFoods.setLayoutManager(new LinearLayoutManager(this));

        foodModelList = new ArrayList<>();
        foodAdapter = new FoodAdapter(this, foodModelList, null);
        recyclerViewFoods.setAdapter(foodAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FoodAPI foodApi = retrofit.create(FoodAPI.class);
        loadFoods(foodApi);
    }

    private void loadFoods(FoodAPI foodApi) {
        Call<List<FoodModel>> call = foodApi.getFoods();
        call.enqueue(new Callback<List<FoodModel>>() {
            @Override
            public void onResponse(Call<List<FoodModel>> call, Response<List<FoodModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    foodAdapter.updateData(response.body());
                    Log.d("API_SUCCESS", "Đã tải " + response.body().size() + " món ăn.");
                } else {
                    String errorMessage = "Không tải được dữ liệu.";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += " Lỗi: " + response.code() + " - " + response.errorBody().string();
                        } catch (IOException e) {
                            errorMessage += " [Lỗi đọc errorBody]";
                        }
                    }
                    Toast.makeText(OrderHistoryActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<FoodModel>> call, Throwable t) {
                Toast.makeText(OrderHistoryActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}