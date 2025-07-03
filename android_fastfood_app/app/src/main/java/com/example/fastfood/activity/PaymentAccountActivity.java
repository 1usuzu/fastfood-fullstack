package com.example.fastfood.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastfood.adapter.PaymentAccountAdapter;
import com.example.fastfood.data.model.PaymentAccount;
import com.example.fastfood.data.api.FoodAPI;
import com.example.fastfood.data.api.RetrofitClient;
import com.example.fastfood.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentAccountActivity extends AppCompatActivity {
    private RecyclerView rvCards;
    private Button btnAddCard;
    private FoodAPI FoodAPI;
    private String userId;
    private ImageView btnBack;
    private PaymentAccountAdapter adapter;
    private List<PaymentAccount> list = new ArrayList<>();
    private String userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_account);

        rvCards = findViewById(R.id.rvCards);
        btnAddCard = findViewById(R.id.btnAddCard);
        btnBack = findViewById(R.id.btnBack);
        adapter = new PaymentAccountAdapter(list);
        rvCards.setAdapter(adapter);
        rvCards.setLayoutManager(new LinearLayoutManager(this));

        FoodAPI = RetrofitClient.getApi();
        // Lấy userId từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        userId = prefs.getString("phone", ""); // Hoặc "userId" nếu lưu theo _id

        userPhone = getIntent().getStringExtra("userPhone");
        if (userPhone == null || userPhone.isEmpty()) {
            // fallback: lấy từ SharedPreferences nếu không truyền qua intent
            userPhone = getSharedPreferences("USER_PREFS", MODE_PRIVATE).getString("phone", "");
        }

        btnAddCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, VisaDetailActivity.class);
            startActivityForResult(intent, 123);
        });

        loadCards();
        btnBack.setOnClickListener(v -> finish());
        // Gán listener xoá thẻ
        adapter.setOnDeleteClickListener((account, position) -> {
            // Gọi API xoá thẻ bằng id hoặc cardNumber (tuỳ bạn lưu thế nào ở backend)
            FoodAPI apiService = RetrofitClient.getApi();
            // Ví dụ backend nhận id hoặc cardNumber:
            apiService.deletePaymentAccount(account.get_id()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        list.remove(position);
                        adapter.notifyItemRemoved(position);
                        Toast.makeText(PaymentAccountActivity.this, "Đã xoá thẻ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PaymentAccountActivity.this, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(PaymentAccountActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadCards() {
        list.clear();
        FoodAPI.getPaymentAccounts(userPhone).enqueue(new Callback<List<PaymentAccount>>() {
            @Override
            public void onResponse(Call<List<PaymentAccount>> call, Response<List<PaymentAccount>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    list.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<PaymentAccount>> call, Throwable t) {}
        });
    }
}
