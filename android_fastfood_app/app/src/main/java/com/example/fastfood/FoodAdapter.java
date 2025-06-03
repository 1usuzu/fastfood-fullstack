package com.example.fastfood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
// import android.widget.ImageView; // Bỏ comment nếu bạn dùng ImageView
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastfood.data.model.FoodModel;

import java.util.List;
import java.util.Locale; // Để format giá tiền

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<FoodModel> foodList;

    // Constructor
    public FoodAdapter(List<FoodModel> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo view mới từ layout item_food.xml
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        // Lấy dữ liệu từ foodList tại vị trí position
        FoodModel currentFood = foodList.get(position);

        // Gán dữ liệu vào các view trong ViewHolder
        holder.foodName.setText(currentFood.getName());
        // Format giá tiền cho dễ nhìn hơn, ví dụ: 25.000 VND
        String formattedPrice = String.format(Locale.GERMAN, "Giá: %,.0f VND", currentFood.getPrice());
        holder.foodPrice.setText(formattedPrice);

        // Nếu bạn có ImageView và imageUrl:
        // Ví dụ sử dụng thư viện Glide hoặc Picasso để tải ảnh từ URL
        // if (currentFood.getImageUrl() != null && !currentFood.getImageUrl().isEmpty()) {
        // Glide.with(holder.itemView.getContext()).load(currentFood.getImageUrl()).into(holder.foodImage);
        // } else {
        // holder.foodImage.setImageResource(R.mipmap.ic_launcher); // Ảnh mặc định
        // }
    }

    @Override
    public int getItemCount() {
        return foodList == null ? 0 : foodList.size();
    }

    // ViewHolder chứa các view của mỗi item
    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName;
        TextView foodPrice;
        // ImageView foodImage; // Bỏ comment nếu bạn dùng ImageView

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.tv_food_name);
            foodPrice = itemView.findViewById(R.id.tv_food_price);
            // foodImage = itemView.findViewById(R.id.iv_food_image); // Bỏ comment nếu bạn dùng ImageView
        }
    }

    // Phương thức để cập nhật dữ liệu cho adapter
    public void updateData(List<FoodModel> newFoodList) {
        this.foodList.clear();
        if (newFoodList != null) {
            this.foodList.addAll(newFoodList);
        }
        notifyDataSetChanged(); // Thông báo cho RecyclerView cập nhật lại giao diện
    }
}