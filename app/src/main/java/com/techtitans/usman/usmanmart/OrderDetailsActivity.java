package com.techtitans.usman.usmanmart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.squareup.picasso.Picasso;
import com.techtitans.usman.usmanmart.databinding.ActivityOrderDetailsBinding;
import com.techtitans.usman.usmanmart.models.OrderModel;
import com.techtitans.usman.usmanmart.models.SellerModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderDetailsActivity extends AppCompatActivity {

    ActivityOrderDetailsBinding binding;
    FirebaseFirestore db;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String storeId,type,processType;
    OrderModel order;
    int orderType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar2);
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        Intent intent=getIntent();
        order=intent.getParcelableExtra("order", OrderModel.class);

        if(order!=null){
            binding.tvOrderId.setText("Order ID: " + order.getOrderId());
            binding.tvSellerId.setText("Seller ID: " + order.getSellerId());
            binding.tvBuyerId.setText("Buyer ID: " + order.getBuyerId());
            binding.tvProductId.setText("Product ID: " + order.getProductId());
            binding.tvPrice.setText("Price: " + order.getTotalPrice());
            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
            binding.tvDate.setText("Date: " + format.format(new Date(order.getCreatedAt())));
            binding.tvQuantity.setText("Quantity: " + order.getQuantity());
            binding.tvStatus.setText("Status: " + order.getOrderStatus());
            binding.tvAddress.setText(order.getShippingAddress());
            Picasso.get()
                    .load(order.getProductImage())
                    .placeholder(R.drawable.image)
                    .into(binding.ivMainImage);
        }

        if(order.getSellerId().equals(order.getBuyerId()))
            type="manualOrders";
        else
            type="customerOrders";

        //to set the type of process you want to do
        setProcessType();
        binding.btnProcessOrder.setOnClickListener(e->{
            if(order.getOrderStatus().equals("Completed"))
                return;
            if(type.equals("manualOrders"))
                getStoreId();
            else
                processCustomOrder();
        });

        binding.backArrow.setOnClickListener(e->{
            finish();
        });
    }


    private void setProcessType() {
        String status = order.getOrderStatus().trim();

        if(status.equalsIgnoreCase("Processing")) {
            processType = "Shipped";
            binding.btnProcessOrder.setText("Ship Order");
        }
        else if (status.equalsIgnoreCase("Shipped")) {
            processType = "Completed";
            binding.btnProcessOrder.setText("Complete Order");
        }
        else if(status.equalsIgnoreCase("Completed")){
            processType = "Completed";
            binding.btnProcessOrder.setText("Already Completed");
        }
    }
    private void getStoreId() {
        database.getReference().child("sellers").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(OrderDetailsActivity.this,
                            "Seller data not found",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                SellerModel seller = snapshot.getValue(SellerModel.class);

                if (seller == null || seller.getStoreId() == null) {
                    Toast.makeText(OrderDetailsActivity.this,
                            "Store ID is null",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                storeId=seller.getStoreId();
                processOrder();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void processOrder(){
        db.collection("stores")
                .document(storeId)
                .collection(type)
                .document(order.getOrderId())
                .update("orderStatus", processType)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        binding.tvStatus.setText(processType);
                        Toast.makeText(OrderDetailsActivity.this, "Order "+processType+" Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
    private void processCustomOrder() {
        db.collection("orders")
                .document(order.getOrderId())
                .update("orderStatus", processType)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        binding.tvStatus.setText(processType);
                        Toast.makeText(OrderDetailsActivity.this, "Order "+processType+" Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }


}