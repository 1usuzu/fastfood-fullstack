package com.example.fastfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fastfood.R;
import com.example.fastfood.data.api.SessionManager; // **THÊM IMPORT NÀY**

public class StartActivity extends AppCompatActivity {

    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra trạng thái đăng nhập ngay khi mở ứng dụng
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLoggedIn()) {
            // Nếu đã đăng nhập, vào thẳng màn hình chính
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Đóng StartActivity để người dùng không quay lại được
            return;   // Dừng thực thi các mã còn lại trong onCreate
        }


        // Nếu chưa đăng nhập, ứng dụng sẽ tiếp tục hiển thị layout của StartActivity
        setContentView(R.layout.activity_start);

        // Kết nối các nút từ layout XML vào mã Java
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        // Xử lý sự kiện khi nhấn nút "Đăng Nhập"
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở màn hình Đăng nhập (LoginActivity)
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện khi nhấn nút "Đăng Ký"
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở màn hình Đăng ký (RegisterActivity)
                Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
