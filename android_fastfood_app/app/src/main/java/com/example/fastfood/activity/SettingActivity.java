package com.example.fastfood.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import com.example.fastfood.data.model.User;
import com.example.fastfood.data.api.FoodAPI;
import com.example.fastfood.data.api.RetrofitClient;
import com.example.fastfood.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class SettingActivity extends AppCompatActivity {
    private TextView tvName, tvPhone;
    private ActivityResultLauncher<Intent> infoLauncher;
    private FoodAPI FoodAPI;
    private Button btnSupport, btnPayment;

    private String userPhone = "0345543580";
    private String userId = "685c822dd6b7134a4ee4cc50"; // Thay bằng ID thực tế

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        LinearLayout btnPayment = findViewById(R.id.btnPayment);
        LinearLayout btnSupport = findViewById(R.id.btnSupport);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        LinearLayout btnInfo = findViewById(R.id.btnInfo);

        FoodAPI = RetrofitClient.getApi();

        loadUserData();

        infoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String name = data.getStringExtra("name");
                        String phone = data.getStringExtra("phone");
                        if (name != null) tvName.setText(name);
                        if (phone != null) tvPhone.setText(phone);
                        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }
                }
        );

// Đang ở SettingActivity, KHÔNG dùng getActivity()
        btnInfo.setOnClickListener(view -> {
            Intent intent = new Intent(SettingActivity.this, InfoActivity.class);
            intent.putExtra("userId", userId);
            infoLauncher.launch(intent);
        });
        btnSupport.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, SupportActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
        btnPayment.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, PaymentAccountActivity.class);
            intent.putExtra("userPhone", userPhone); // TRUYỀN SDT
            startActivity(intent);
        });


    }
    protected void onResume() {
        super.onResume();
        FoodAPI.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvName.setText(response.body().getName());
                    tvPhone.setText(response.body().getPhone());

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }

    private void loadUserData() {
        FoodAPI.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (user != null) {
                    tvName.setText(user.getName());
                    tvPhone.setText(user.getPhone());
                    // Lưu lại vào SharedPreferences
                    SharedPreferences.Editor editor = getSharedPreferences("USER_PREFS", MODE_PRIVATE).edit();
                    editor.putString("phone", user.getPhone());
                    editor.putString("name", user.getName());
                    editor.apply();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(SettingActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }

        });


    }

}
