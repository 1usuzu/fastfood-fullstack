package com.example.fastfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fastfood.R;
import com.example.fastfood.data.api.PasswordApi;
import com.example.fastfood.data.api.RetrofitClient;
import com.example.fastfood.data.model.OtpRequest;
import com.example.fastfood.data.model.ApiResponse;
import com.example.fastfood.data.model.PhoneRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordOtpActivity extends AppCompatActivity {

    private EditText edtOtp;
    private Button btnVerifyOtp;
    private TextView tvResendOtp;
    private ImageView ivBack;
    private String phone;
    PasswordApi passwordApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_otp);

        edtOtp = findViewById(R.id.edt_otp);
        btnVerifyOtp = findViewById(R.id.btn_verify_otp);
        tvResendOtp = findViewById(R.id.tv_resend_otp);
        ivBack = findViewById(R.id.iv_back);
        phone = getIntent().getStringExtra("phone");

        // ✅ Đổi từ getClient() sang getApi()
        passwordApi = RetrofitClient.getApi(PasswordApi.class);

        ivBack.setOnClickListener(v -> finish());

        tvResendOtp.setOnClickListener(v -> {
            passwordApi.sendOtp(new PhoneRequest(phone)).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    Toast.makeText(ForgotPasswordOtpActivity.this, "OTP đã gửi lại", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(ForgotPasswordOtpActivity.this, "Lỗi gửi lại OTP: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnVerifyOtp.setOnClickListener(v -> {
            String otp = edtOtp.getText().toString().trim();

            if (otp.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            passwordApi.verifyOtp(new OtpRequest(phone, otp)).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        Intent intent = new Intent(ForgotPasswordOtpActivity.this, ForgotPasswordResetActivity.class);
                        intent.putExtra("phone", phone);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ForgotPasswordOtpActivity.this, "Mã OTP không đúng", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(ForgotPasswordOtpActivity.this, "Lỗi xác minh OTP: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
