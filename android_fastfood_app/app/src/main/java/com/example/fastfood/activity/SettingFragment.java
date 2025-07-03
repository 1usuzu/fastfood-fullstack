package com.example.fastfood.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fastfood.R;
import com.example.fastfood.activity.InfoActivity;
import com.example.fastfood.activity.PaymentAccountActivity;
import com.example.fastfood.activity.SupportActivity;
import com.example.fastfood.data.model.User;
import com.example.fastfood.data.api.FoodAPI;
import com.example.fastfood.data.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment {
    private TextView tvName, tvPhone;
    private LinearLayout btnInfo, btnSupport, btnPayment;
    private Button btnLogout;
    private Switch switchDarkMode;
    private FoodAPI foodAPI;
    private String userId = "685c822dd6b7134a4ee4cc50";
    private String userPhone = "0345543580";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting, container, false);

        tvName = view.findViewById(R.id.tvName);
        tvPhone = view.findViewById(R.id.tvPhone);
        btnInfo = view.findViewById(R.id.btnInfo);
        btnSupport = view.findViewById(R.id.btnSupport);
        btnPayment = view.findViewById(R.id.btnPayment);
        btnLogout = view.findViewById(R.id.btnLogout);
        switchDarkMode = view.findViewById(R.id.switchDarkMode);

        foodAPI = RetrofitClient.getApi();

        // Load user info
        loadUserData();

        // Sự kiện click
        btnInfo.setOnClickListener(v -> {
            // Dùng Activity vì InfoActivity không phải fragment
            Intent intent = new Intent(getActivity(), InfoActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        btnSupport.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SupportActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        btnPayment.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PaymentAccountActivity.class);
            intent.putExtra("userPhone", userPhone);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
            // Thực hiện logout thực tế ở đây...
        });

        // Dark mode: chỉ demo
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Code chuyển theme, nếu muốn
        });

        return view;
    }

    private void loadUserData() {
        foodAPI.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (user != null) {
                    tvName.setText(user.getName());
                    tvPhone.setText(user.getPhone());
                    // Lưu lại vào SharedPreferences nếu cần
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("USER_PREFS", getActivity().MODE_PRIVATE).edit();
                    editor.putString("phone", user.getPhone());
                    editor.putString("name", user.getName());
                    editor.apply();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
