package com.example.fastfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.fastfood.adapter.FoodAdapter;
import com.example.fastfood.data.api.RetrofitClient;
import com.example.fastfood.data.local.AppDatabase;
import com.example.fastfood.data.local.CartItem;
import com.example.fastfood.data.model.FoodModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends Fragment implements FoodAdapter.OnItemAddListener {

    private RecyclerView rvFoods;
    private FoodAdapter foodAdapter;
    private FloatingActionButton fabCart;
    private AppDatabase database;
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = AppDatabase.getDatabase(getContext());
        rvFoods = view.findViewById(R.id.rv_foods); // Đảm bảo ID này đúng với layout
        fabCart = view.findViewById(R.id.fab_cart);

        setupRecyclerView();
        fetchData();
        setupCartButton();
    }

    private void setupRecyclerView() {
        rvFoods.setLayoutManager(new LinearLayoutManager(getContext()));
        foodAdapter = new FoodAdapter(getContext(), new ArrayList<>(), this);
        rvFoods.setAdapter(foodAdapter);
    }

    private void setupCartButton() {
        fabCart.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CartActivity.class);
            startActivity(intent);
        });
    }

    private void fetchData() {
        Call<List<FoodModel>> call = RetrofitClient.getApi().getFoods();
        call.enqueue(new Callback<List<FoodModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<FoodModel>> call, @NonNull Response<List<FoodModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    foodAdapter.updateData(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FoodModel>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemAdd(FoodModel food) {
        Toast.makeText(getContext(), "Đã thêm: " + food.getName(), Toast.LENGTH_SHORT).show();

        databaseExecutor.execute(() -> {
            CartItem existingItem = database.cartDao().findItemById(food.getId());

            if (existingItem != null) {
                existingItem.quantity++;
                database.cartDao().update(existingItem);
            } else {
                CartItem newItem = new CartItem();
                newItem.foodId = food.getId();
                newItem.name = food.getName();
                newItem.price = food.getPrice();
                newItem.imageUrl = food.getImageUrl();
                newItem.quantity = 1;
                database.cartDao().insert(newItem);
            }
        });
    }
}