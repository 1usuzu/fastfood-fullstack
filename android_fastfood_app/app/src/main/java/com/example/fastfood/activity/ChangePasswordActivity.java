package com.example.fastfood.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.fastfood.data.api.FoodAPI;
import com.example.fastfood.data.model.ChangePasswordRequest;
import com.example.fastfood.data.api.RetrofitClient;
import com.example.fastfood.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText edtOldPass, edtNewPass, edtConfirmPass;
    private AppCompatButton btnChangePass;
    private FoodAPI FoodAPI;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edtOldPass = findViewById(R.id.edtOldPass);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtConfirmPass = findViewById(R.id.edtConfirmPass);
        btnChangePass = findViewById(R.id.btnChangePass);

        FoodAPI = RetrofitClient.getApi();

        userId = getIntent().getStringExtra("userId");

        btnChangePass.setOnClickListener(v -> {
            String oldPass = edtOldPass.getText().toString();
            String newPass = edtNewPass.getText().toString();
            String confirmPass = edtConfirmPass.getText().toString();

            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ các trường", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            ChangePasswordRequest request = new ChangePasswordRequest(oldPass, newPass);
            FoodAPI.changePassword(userId, request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(ChangePasswordActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Ẩn bàn phím khi chạm ngoài
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                int[] loc = new int[2];
                v.getLocationOnScreen(loc);
                float x = ev.getRawX() + v.getLeft() - loc[0];
                float y = ev.getRawY() + v.getTop() - loc[1];
                if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
