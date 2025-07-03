package com.example.fastfood.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fastfood.data.model.PaymentAccount;
import com.example.fastfood.data.api.FoodAPI;
import com.example.fastfood.data.api.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;
import com.example.fastfood.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// VisaDetailActivity.java
public class VisaDetailActivity extends AppCompatActivity {
    private TextInputEditText edtCardHolder, edtCardNumber, edtExpiry, edtCVV;
    private Button btnAddCard;
    private String userId;
    private ImageView btnBack;
    private FoodAPI FoodAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa_detail);

        // Ánh xạ view
        btnBack = findViewById(R.id.btnBack);
        edtCardHolder = findViewById(R.id.edtCardHolder);
        edtCardNumber = findViewById(R.id.edtCardNumber);
        edtExpiry = findViewById(R.id.edtExpiry);
        edtCVV = findViewById(R.id.edtCVV);
        btnAddCard = findViewById(R.id.btnAddCard);

        FoodAPI = RetrofitClient.getApi();

        // Lấy userId từ SharedPreferences hoặc intent
        SharedPreferences prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        userId = prefs.getString("phone", ""); // hoặc "userId" nếu bạn lưu _id

        btnAddCard.setOnClickListener(v -> {
            String cardHolder = edtCardHolder.getText().toString().trim();
            String cardNumber = edtCardNumber.getText().toString().trim();
            String expiry = edtExpiry.getText().toString().trim();
            String cvv = edtCVV.getText().toString().trim();

            if(cardHolder.isEmpty() || cardNumber.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
                Toast.makeText(this, "Nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            PaymentAccount acc = new PaymentAccount(userId, cardHolder, cardNumber, expiry, cvv, "");
            FoodAPI.addPaymentAccount(acc).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        Toast.makeText(VisaDetailActivity.this, "Đã thêm thẻ!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(VisaDetailActivity.this, "Lỗi backend!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(VisaDetailActivity.this, "Lỗi mạng!", Toast.LENGTH_SHORT).show();
                }
            });
        });
        btnBack.setOnClickListener(v -> finish());
    }
}

