package com.example.fastfood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastfood.R;
import com.example.fastfood.activity.CartActivity;
import com.example.fastfood.adapter.FoodAdapter;
import com.example.fastfood.data.api.FoodAPI;
import com.example.fastfood.data.api.RetrofitClient;
import com.example.fastfood.data.model.FoodModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodListFragment extends Fragment {
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<FoodModel> foodList = new ArrayList<>();
    private TextView tvCategoryTitle;
    private ImageView btnBack;
    private String category;

    public static FoodListFragment newInstance(String category) {
        FoodListFragment fragment = new FoodListFragment();
        Bundle args = new Bundle();
        args.putString("category", category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString("category");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);

        // Ánh xạ đúng các ID từ layout
        tvCategoryTitle = view.findViewById(R.id.tv_title);
        btnBack = view.findViewById(R.id.btn_back);
        recyclerView = view.findViewById(R.id.rv_foods);

        if (category != null) {
            tvCategoryTitle.setText(category);
        }

        setupRecyclerView();
        loadFoodsAndFilterByCategory();

        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return view;
    }

    private void setupRecyclerView() {
        // **SỬA LẠI CHO KHỚP VỚI FoodAdapter BẠN CUNG CẤP**
        foodAdapter = new FoodAdapter(getContext(), foodList, new FoodAdapter.OnItemAddListener() {
            @Override
            public void onItemAdd(FoodModel food) {
                // Khi nhấn nút "Add", chuyển đến trang chi tiết
                openFoodDetail(food);
            }

            @Override
            public void onItemClick(FoodModel food) {
                // Khi nhấn vào cả item, cũng chuyển đến trang chi tiết
                openFoodDetail(food);
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(foodAdapter);
    }

    // Hàm helper để tránh lặp code
    private void openFoodDetail(FoodModel food) {
        FoodDetailFragment detailFragment = FoodDetailFragment.newInstance(food);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    private void loadFoodsAndFilterByCategory() {
        if (category == null) return;

        FoodAPI foodApi = RetrofitClient.getApi();
        foodApi.getFoods().enqueue(new Callback<List<FoodModel>>() {
            @Override
            public void onResponse(Call<List<FoodModel>> call, Response<List<FoodModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<FoodModel> allFoods = response.body();
                    List<FoodModel> filteredFoods = new ArrayList<>();

                    // Lọc các món ăn thuộc danh mục đã chọn
                    for (FoodModel food : allFoods) {
                        // Dùng equalsIgnoreCase để không phân biệt hoa thường
                        if (category.equalsIgnoreCase(food.getCategory())) {
                            filteredFoods.add(food);
                        }
                    }

                    // Cập nhật dữ liệu cho adapter
                    foodAdapter.updateData(filteredFoods);

                } else {
                    Toast.makeText(getContext(), "Không thể tải danh sách món ăn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FoodModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
