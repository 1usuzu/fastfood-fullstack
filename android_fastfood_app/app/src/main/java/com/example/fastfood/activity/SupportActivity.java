package com.example.fastfood.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fastfood.data.model.ShopInfo;
import com.example.fastfood.data.model.SupportRequest;
import com.example.fastfood.data.api.FoodAPI;
import com.example.fastfood.data.api.RetrofitClient;
import com.example.fastfood.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportActivity extends AppCompatActivity {
    private TextView tvEmail, tvPhone, tvAddress;
    private EditText edtSupportContent;
    private Button btnSendSupport;
    private ImageView btnBack, imgDropdown;
    private LinearLayout contactInfoContent;
    private boolean contactInfoVisible = true;
    private String userName, userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        // Ánh xạ view
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        edtSupportContent = findViewById(R.id.edtSupportContent);
        btnSendSupport = findViewById(R.id.btnSendSupport);
        btnBack = findViewById(R.id.btnBack);
        imgDropdown = findViewById(R.id.imgDropdown);
        contactInfoContent = findViewById(R.id.contactInfoContent);

        // Lấy info_user từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        userPhone = prefs.getString("phone", "");
        userName = prefs.getString("name", "");
        // Nếu cần, có thể show tên/sđt ra header hoặc Toast

        // Expand/collapse contact info
        findViewById(R.id.headerContactInfo).setOnClickListener(v -> {
            contactInfoVisible = !contactInfoVisible;
            contactInfoContent.setVisibility(contactInfoVisible ? View.VISIBLE : View.GONE);
            imgDropdown.setRotation(contactInfoVisible ? 0 : -90);
        });

        // Gọi API lấy thông tin shop
        FoodAPI FoodAPI = RetrofitClient.getApi();
        FoodAPI.getShopInfo().enqueue(new Callback<ShopInfo>() {
            @Override
            public void onResponse(Call<ShopInfo> call, Response<ShopInfo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ShopInfo shop = response.body();
                    tvEmail.setText("Email liên hệ: " + shop.getEmail());
                    tvPhone.setText("Hotline: " + shop.getPhone());
                    tvAddress.setText("Địa chỉ: " + shop.getAddress());
                } else {
                    tvEmail.setText("Không lấy được email");
                    tvPhone.setText("Không lấy được hotline");
                    tvAddress.setText("Không lấy được địa chỉ");
                }
            }
            @Override
            public void onFailure(Call<ShopInfo> call, Throwable t) {
                tvEmail.setText("Không kết nối server");
                tvPhone.setText("");
                tvAddress.setText("");
            }
        });


        // Nút back
        btnBack.setOnClickListener(v -> finish());

        // Nút gửi hỗ trợ
        btnSendSupport.setOnClickListener(v -> {
            String content = edtSupportContent.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(this, "Bạn cần nhập nội dung!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (userPhone.isEmpty()) {
                Toast.makeText(this, "Không xác định được tài khoản!", Toast.LENGTH_SHORT).show();
                return;
            }
            SupportRequest req = new SupportRequest(userPhone, content);
            FoodAPI.sendSupportRequest(req).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(SupportActivity.this, "Đã gửi yêu cầu hỗ trợ!", Toast.LENGTH_SHORT).show();
                        edtSupportContent.setText("");
                    } else {
                        Toast.makeText(SupportActivity.this, "Gửi thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(SupportActivity.this, "Lỗi mạng!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
