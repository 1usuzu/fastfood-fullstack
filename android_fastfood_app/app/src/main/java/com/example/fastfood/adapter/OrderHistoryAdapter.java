package com.example.fastfood.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fastfood.R;
import com.example.fastfood.data.model.Order;
import com.example.fastfood.data.model.OrderItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {
    private List<Order> orderList;
    private OnReorderClickListener reorderClickListener;


    public interface OnReorderClickListener {
        void onReorderClick(Order order);
    }

    public OrderHistoryAdapter(List<Order> orderList, OnReorderClickListener listener) {
        this.orderList = orderList;
        this.reorderClickListener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        // Truyền listener vào hàm bind để gán sự kiện click
        holder.bind(order, reorderClickListener);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderStatus, tvOrderDate, tvOrderItems, tvOrderTotal;
        Button btnReorder;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderItems = itemView.findViewById(R.id.tv_order_items);
            tvOrderTotal = itemView.findViewById(R.id.tv_order_total);
            btnReorder = itemView.findViewById(R.id.btn_reorder);
        }

        /**
         * Cập nhật hàm bind để gán sự kiện click cho nút "Đặt lại"
         */
        void bind(final Order order, final OnReorderClickListener listener) {
            tvOrderId.setText("Mã đơn hàng #" + order.getId());
            tvOrderStatus.setText(order.getStatus());
            tvOrderTotal.setText("Tổng tiền: " + String.format(Locale.GERMAN, "%,.0fđ", order.getTotalPrice()));
            tvOrderDate.setText("Ngày đặt: " + formatIsoDate(order.getCreatedAt()));

            StringBuilder itemsBuilder = new StringBuilder();
            if (order.getOrderItems() != null) {
                for (OrderItem item : order.getOrderItems()) {
                    itemsBuilder.append(item.getFoodName()).append(" (x").append(item.getQuantity()).append("), ");
                }
                if (itemsBuilder.length() > 2) {
                    itemsBuilder.setLength(itemsBuilder.length() - 2);
                }
            }
            tvOrderItems.setText("Sản phẩm: " + itemsBuilder.toString());

            // Gán sự kiện click cho nút "Đặt lại"
            btnReorder.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onReorderClick(order);
                }
            });
        }

        private String formatIsoDate(String isoDate) {
            if (isoDate == null) return "Không rõ";
            try {
                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = isoFormat.parse(isoDate);
                SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                return newFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return isoDate;
            }
        }
    }
}