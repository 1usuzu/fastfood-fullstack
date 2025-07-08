package com.example.fastfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastfood.R;
import com.example.fastfood.data.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private Context context;

    public OrderAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.tvDate.setText(order.getCreatedAt());
        holder.tvStatus.setText(order.getStatus());
        holder.tvOrderCode.setText("Mã đơn: #" + order.getId());
        holder.tvItems.setText("Số món: " + order.getItemCount()); // bạn cần có field này
        holder.tvTotal.setText("Tổng tiền: " + order.getTotal() + "₫");

        // Xử lý nút nếu muốn
        holder.btnDetails.setOnClickListener(v -> {
            // TODO: Xử lý sự kiện "Đặt lại"
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvStatus, tvOrderCode, tvItems, tvTotal;
        Button btnDetails;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvOrderCode = itemView.findViewById(R.id.tv_order_code);
            tvItems = itemView.findViewById(R.id.tv_items);
            tvTotal = itemView.findViewById(R.id.tv_total);
            btnDetails = itemView.findViewById(R.id.btn_details);
        }
    }
}
