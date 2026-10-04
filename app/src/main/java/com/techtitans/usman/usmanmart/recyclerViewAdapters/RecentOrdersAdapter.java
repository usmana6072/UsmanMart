package com.techtitans.usman.usmanmart.recyclerViewAdapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.techtitans.usman.usmanmart.R;
import com.techtitans.usman.usmanmart.databinding.ItemRecentOrderBinding;
import com.techtitans.usman.usmanmart.models.OrderModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Shows the seller's most recent orders (customer orders + manual sales) on the Home dashboard.
 */
public class RecentOrdersAdapter extends RecyclerView.Adapter<RecentOrdersAdapter.ViewHolder> {

    public interface OnOrderClickListener {
        void onOrderClick(OrderModel order);
    }

    private final Context context;
    private final List<OrderModel> orders;
    private final OnOrderClickListener listener;

    public RecentOrdersAdapter(Context context, List<OrderModel> orders, OnOrderClickListener listener) {
        this.context = context;
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecentOrderBinding binding = ItemRecentOrderBinding.inflate(
                LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel order = orders.get(position);

        holder.binding.tvProductName.setText(
                order.getProductName() != null ? order.getProductName() : "Product");
        holder.binding.tvBuyerName.setText(
                order.getBuyerName() != null ? order.getBuyerName() : "Customer");
        holder.binding.tvAmount.setText(
                String.format(Locale.getDefault(), "Rs %.0f", order.getTotalPrice()));
        holder.binding.tvDate.setText(formatDate(order.getCreatedAt()));

        Picasso.get()
                .load(order.getProductImage())
                .placeholder(R.drawable.image)
                .into(holder.binding.ivProductImage);

        bindStatus(holder, order.getOrderStatus());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onOrderClick(order);
        });
    }

    private void bindStatus(ViewHolder holder, String status) {
        String label = (status == null || status.trim().isEmpty()) ? "Processing" : status.trim();

        int bgColor;
        int textColor;

        switch (label.toLowerCase(Locale.ROOT)) {
            case "completed":
                bgColor = ContextCompat.getColor(context, R.color.status_completed_bg);
                textColor = ContextCompat.getColor(context, R.color.status_completed_text);
                break;
            case "shipped":
                bgColor = ContextCompat.getColor(context, R.color.status_shipped_bg);
                textColor = ContextCompat.getColor(context, R.color.status_shipped_text);
                break;
            default:
                bgColor = ContextCompat.getColor(context, R.color.status_pending_bg);
                textColor = ContextCompat.getColor(context, R.color.status_pending_text);
        }

        holder.binding.tvStatus.setText(label);
        holder.binding.tvStatus.setTextColor(textColor);
        holder.binding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(bgColor));
    }

    private String formatDate(long millis) {
        if (millis == 0) return "";
        return new SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(new Date(millis));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemRecentOrderBinding binding;

        ViewHolder(ItemRecentOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
