package com.techtitans.usman.usmanmart.recyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.techtitans.usman.usmanmart.OrderDetailsActivity;
import com.techtitans.usman.usmanmart.R;
import com.techtitans.usman.usmanmart.models.OrderModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderAdapterOrderPage extends RecyclerView.Adapter<OrderAdapterOrderPage.ViewHolder>{
    ArrayList<OrderModel> list;
    Context context;

    public OrderAdapterOrderPage(ArrayList<OrderModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_order_orderpage,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel order=list.get(position);
        Picasso.get().load(order.getProductImage()).placeholder(R.drawable.image).into(holder.mainImage);
        holder.orderId.setText(order.getOrderId());
        holder.itemId.setText(order.getProductId());
        holder.price.setText(""+order.getTotalPrice());
        holder.quantity.setText(""+order.getQuantity());
        holder.status.setText(order.getOrderStatus());
        Date date=new Date(order.getCreatedAt());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat t = new SimpleDateFormat("hh:mm a");

        holder.date.setText(""+d.format(date));
        holder.time.setText(""+t.format(date));

        holder.itemView.setOnClickListener(e->{
            Intent intent = new Intent(context, OrderDetailsActivity.class);
            intent.putExtra("order",order);
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mainImage;
        TextView orderId,itemId,price,quantity,status,time,date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mainImage=itemView.findViewById(R.id.orderProductMainImage);
            orderId=itemView.findViewById(R.id.tvOrderId);
            itemId=itemView.findViewById(R.id.tvOrderProductId);
            price=itemView.findViewById(R.id.orderPrice);
            quantity=itemView.findViewById(R.id.tvOrderProductQuantity);
            status=itemView.findViewById(R.id.tvOrderStatus);
            date=itemView.findViewById(R.id.tvOrderDate);
            time=itemView.findViewById(R.id.tvOrderTime);
        }
    }
}
