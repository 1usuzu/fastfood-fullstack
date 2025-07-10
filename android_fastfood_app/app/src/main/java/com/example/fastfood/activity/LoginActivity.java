// Vị trí: com.example.fastfood.activity/LoginActivity.java
package com.example.fastfood.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fastfood.R;
import com.example.fastfood.data.api.SessionManager;
import com.example.fastfood.data.api.RetrofitClient;
import com.example.fastfood.data.api.UserApi;
import com.example.fastfood.data.model.UserLoginRequest;
import com.example.fastfood.data.model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    ImageView ivBack;
    LinearLayout llGoToRegister;
    View btnLogin;
    TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // SessionManager để kiểm tra nếu đã đăng nhập thì vào thẳng màn hình chính
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
            finish();
            return; // Dừng hàm onCreate ở đây để không chạy code thừa
        }

        edtPhone = findViewById(R.id.edt_phone);
        edtPassword = findViewById(R.id.edt_password);
        ivBack = findViewById(R.id.iv_back);
        llGoToRegister = findViewById(R.id.ll_go_to_register);
        btnLogin = findViewById(R.id.btn_login_submit);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);

        SpannableString span = new SpannableString("Bạn quên mật khẩu? Quên mật khẩu");
        span.setSpan(new StyleSpan(Typeface.BOLD), 21, 35, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvForgotPassword.setText(span);

        ivBack.setOnClickListener(v -> finish());

        llGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            String phone = edtPhone.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            UserApi api = RetrofitClient.getApi(UserApi.class);
            UserLoginRequest loginRequest = new UserLoginRequest(phone, password);

            api.loginUser(loginRequest).enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        // Lấy token
                        String token = response.body().getToken();

                        // Tạo và lưu session chỉ với token
                        SessionManager sessionManager = new SessionManager(getApplicationContext());
                        sessionManager.createLoginSession(token);

                        // Chuyển sang màn hình chính
                        Intent intent = new Intent(LoginActivity.this, OrderHistoryActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Sai SĐT hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}