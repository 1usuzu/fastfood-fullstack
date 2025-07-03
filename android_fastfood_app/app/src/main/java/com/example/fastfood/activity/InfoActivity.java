package com.example.fastfood.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.fastfood.data.model.User;
import com.example.fastfood.data.api.FoodAPI;
import com.example.fastfood.data.api.RetrofitClient;
import com.example.fastfood.R;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoActivity extends AppCompatActivity {
    private EditText edtName, edtDate, edtPhone, edtEmail, edtAddress;
    private ImageView btnBack;
    private AppCompatButton btnConfirm, btnChangePassword;
    private FoodAPI FoodAPI;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        edtName = findViewById(R.id.edtName);
        edtDate = findViewById(R.id.edtDate);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edtAddress);
        btnBack = findViewById(R.id.btnBack);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        FoodAPI = RetrofitClient.getApi();

        // Nhận userId từ intent
        userId = getIntent().getStringExtra("userId");

        loadUserData();

        // Thêm text watcher cho ngày tháng (tự động thêm /)
        edtDate.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (!input.equals(current)) {
                    String clean = input.replaceAll("[^\\d]", "");
                    String cleanC = current.replaceAll("[^\\d]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));
                        mon = (mon < 1) ? 1 : mon;
                        mon = (mon > 12) ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year < 1900)? 1900: (year > 2100)? 2100:year;
                        cal.set(Calendar.YEAR, year);
                        day = (day > cal.getActualMaximum(Calendar.DAY_OF_MONTH)) ? cal.getActualMaximum(Calendar.DAY_OF_MONTH):day;
                        clean = String.format("%02d%02d%04d",day, mon, year);
                    }

                    String formatted = clean.substring(0,2) + "/" + clean.substring(2,4) + "/" + clean.substring(4,8);
                    current = formatted;
                    edtDate.removeTextChangedListener(this);
                    edtDate.setText(formatted);
                    edtDate.setSelection(sel < formatted.length() ? sel : formatted.length());
                    edtDate.addTextChangedListener(this);
                }
            }
        });

        btnBack.setOnClickListener(v -> finish());

        btnConfirm.setOnClickListener(v -> {
            String date = edtDate.getText().toString();
            if (!isValidDate(date)) {
                Toast.makeText(this, "Ngày không hợp lệ!", Toast.LENGTH_SHORT).show();
                edtDate.requestFocus();
                return;
            }
            User user = new User();
            user.setName(edtName.getText().toString());
            user.setDate(edtDate.getText().toString());
            user.setPhone(edtPhone.getText().toString());
            user.setEmail(edtEmail.getText().toString());
            user.setAddress(edtAddress.getText().toString());

            FoodAPI.updateUser(userId, user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(InfoActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(InfoActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(InfoActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(InfoActivity.this, ChangePasswordActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }

    private void loadUserData() {
        FoodAPI.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    edtName.setText(user.getName());
                    edtDate.setText(user.getDate());
                    edtPhone.setText(user.getPhone());
                    edtEmail.setText(user.getEmail());
                    edtAddress.setText(user.getAddress());
                } else {
                    Toast.makeText(InfoActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(InfoActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidDate(String date) {
        if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) return false;
        String[] parts = date.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        Calendar cal = Calendar.getInstance();
        cal.setLenient(false);
        try {
            cal.set(year, month - 1, day);
            cal.getTime();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                int[] outLocation = new int[2];
                v.getLocationOnScreen(outLocation);
                float x = ev.getRawX() + v.getLeft() - outLocation[0];
                float y = ev.getRawY() + v.getTop() - outLocation[1];
                if (x < v.getLeft() || x > v.getRight() ||
                        y < v.getTop() || y > v.getBottom()) {
                    v.clearFocus();
                    hideKeyboard(this, v);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
