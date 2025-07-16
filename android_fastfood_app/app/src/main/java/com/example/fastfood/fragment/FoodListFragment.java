package com.example.fastfood.fragment;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastfood.R;
import com.example.fastfood.adapter.FoodAdapter;
import com.example.fastfood.data.api.RetrofitClient;
import com.example.fastfood.data.model.FoodModel;

import java.util.ArrayList;
import java.util.List;

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
            // Nhận danh mục, có thể là null
            category = getArguments().getString("category");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);

        tvCategoryTitle = view.findViewById(R.id.tv_title);
        btnBack = view.findViewById(R.id.btn_back);
        recyclerView = view.findViewById(R.id.rv_foods);

        setupUI();
        setupRecyclerView();
        loadFoods(); // Gọi hàm tải dữ liệu

        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return view;
    }

    private void setupUI() {
        // Nếu category là null, hiển thị tiêu đề "Tất cả món ăn"
        if (category == null || category.isEmpty()) {
            tvCategoryTitle.setText("Tất cả món ăn");
        } else {
            tvCategoryTitle.setText(category);
        }
    }

    private void setupRecyclerView() {
        foodAdapter = new FoodAdapter(getContext(), foodList, new FoodAdapter.OnItemAddListener() {
            @Override
            public void onItemAdd(FoodModel food) {
                openFoodDetail(food);
            }

            @Override
            public void onItemClick(FoodModel food) {
                openFoodDetail(food);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(foodAdapter);
    }

    private void openFoodDetail(FoodModel food) {
        FoodDetailFragment detailFragment = FoodDetailFragment.newInstance(food);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Hàm này đã được sửa lại để xử lý trường hợp category là null
     */
    private void loadFoods() {
        RetrofitClient.getApi().getFoods().enqueue(new Callback<List<FoodModel>>() {
            @Override
            public void onResponse(Call<List<FoodModel>> call, Response<List<FoodModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<FoodModel> allFoods = response.body();
                    List<FoodModel> foodsToShow = new ArrayList<>();

                    // **SỬA LỖI LOGIC Ở ĐÂY**
                    // Nếu category là null hoặc rỗng, hiển thị tất cả.
                    // Ngược lại, lọc theo danh mục.
                    if (category == null || category.isEmpty()) {
                        foodsToShow.addAll(allFoods);
                    } else {
                        for (FoodModel food : allFoods) {
                            if (category.equalsIgnoreCase(food.getCategory())) {
                                foodsToShow.add(food);
                            }
                        }
                    }

                    foodAdapter.updateData(foodsToShow);

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
