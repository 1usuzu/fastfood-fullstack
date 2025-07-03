package com.example.fastfood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fastfood.data.api.PasswordApi;
import com.example.fastfood.data.api.RetrofitClient;
import com.example.fastfood.data.model.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordPhoneActivity extends AppCompatActivity {

    EditText edtPhone;
    View btnGetOtp;
    PasswordApi passwordApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_phone);

        edtPhone = findViewById(R.id.edt_phone);
        btnGetOtp = findViewById(R.id.btn_get_otp);
        passwordApi = RetrofitClient.getClient().create(PasswordApi.class);

        btnGetOtp.setOnClickListener(v -> {
            String phone = edtPhone.getText().toString().trim();
            if (phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                return;
            }

            passwordApi.sendOtp(phone).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ForgotPasswordPhoneActivity.this, "Đã gửi OTP", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPasswordPhoneActivity.this, ForgotPasswordOtpActivity.class);
                        intent.putExtra("phone", phone);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ForgotPasswordPhoneActivity.this, "Số điện thoại chưa đăng ký", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(ForgotPasswordPhoneActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
    }
}
