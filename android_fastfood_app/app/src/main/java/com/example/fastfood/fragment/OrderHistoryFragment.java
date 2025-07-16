package com.example.fastfood.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fastfood.R;
import com.example.fastfood.activity.CartActivity;
import com.example.fastfood.adapter.OrderHistoryAdapter;
import com.example.fastfood.data.api.FoodAPI;
import com.example.fastfood.data.api.RetrofitClient;
import com.example.fastfood.data.local.AppDatabase;
import com.example.fastfood.data.local.CartItem;
import com.example.fastfood.data.model.Order;
import com.example.fastfood.data.model.OrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryFragment extends Fragment implements OrderHistoryAdapter.OnReorderClickListener {

    private RecyclerView rvOrderHistory;
    private OrderHistoryAdapter adapter;
    private List<Order> orderList = new ArrayList<>();
    private AppDatabase database;
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        rvOrderHistory = view.findViewById(R.id.rv_order_history);
        database = AppDatabase.getDatabase(getContext());

        setupRecyclerView();
        loadOrderHistory();
        return view;
    }

    private void setupRecyclerView() {
        // Truyền "this" (tức là Fragment này) làm listener cho sự kiện click
        adapter = new OrderHistoryAdapter(orderList, this);
        rvOrderHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOrderHistory.setAdapter(adapter);
    }

    private void loadOrderHistory() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        if (userId == null) {
            Toast.makeText(getContext(), "Vui lòng đăng nhập để xem lịch sử", Toast.LENGTH_SHORT).show();
            return;
        }

        FoodAPI foodAPI = RetrofitClient.getApi();
        foodAPI.getOrderHistory(userId).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderList.clear();
                    orderList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không thể tải lịch sử đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Hàm này được gọi từ Adapter khi người dùng nhấn nút "Đặt lại"
     * @param order Đơn hàng cần đặt lại
     */
    @Override
    public void onReorderClick(Order order) {
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            Toast.makeText(getContext(), "Đơn hàng này không có sản phẩm để đặt lại.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sử dụng Executor để thực hiện thao tác với database trên luồng nền
        databaseExecutor.execute(() -> {
            for (OrderItem item : order.getOrderItems()) {
                CartItem cartItem = new CartItem();
                cartItem.name = item.getFoodName();
                cartItem.price = item.getPrice();
                cartItem.quantity = item.getQuantity();
                cartItem.imageUrl = item.getImageUrl();
                // Bạn có thể cần thêm các trường khác nếu có, ví dụ như foodId, notes,...

                database.cartDao().insert(cartItem);
            }

            // Sau khi thêm xong, chuyển sang màn hình giỏ hàng trên luồng chính (UI thread)
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Đã thêm các sản phẩm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
            });
        });
    }
}