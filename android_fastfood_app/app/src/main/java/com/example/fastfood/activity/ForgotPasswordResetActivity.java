package com.example.fastfood.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fastfood.R;
import com.example.fastfood.data.api.PasswordApi;
import com.example.fastfood.data.api.RetrofitClient;
import com.example.fastfood.data.model.ResetPasswordRequest;
import com.example.fastfood.data.model.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordResetActivity extends AppCompatActivity {

    EditText edtNewPassword, edtConfirmPassword;
    Button btnConfirmReset;
    ImageView ivBack;
    String phone;
    PasswordApi passwordApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_reset);

        edtNewPassword = findViewById(R.id.edt_new_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        btnConfirmReset = findViewById(R.id.btn_reset_password);
        ivBack = findViewById(R.id.iv_back);
        phone = getIntent().getStringExtra("phone");
        passwordApi = RetrofitClient.getApi(PasswordApi.class);

        ivBack.setOnClickListener(v -> finish());

        btnConfirmReset.setOnClickListener(v -> {
            String pass = edtNewPassword.getText().toString().trim();
            String confirm = edtConfirmPassword.getText().toString().trim();

            if (pass.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(confirm)) {
                Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            passwordApi.resetPassword(new ResetPasswordRequest(phone, pass)).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ForgotPasswordResetActivity.this, "Đặt lại mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ForgotPasswordResetActivity.this, "Thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(ForgotPasswordResetActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
