package com.techtitans.usman.usmanmart.forms;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techtitans.usman.usmanmart.databinding.ActivityOrderCreateFormBinding;
import com.techtitans.usman.usmanmart.models.OrderModel;
import com.techtitans.usman.usmanmart.models.Product;
import com.techtitans.usman.usmanmart.models.SellerModel;

import java.util.Date;

public class OrderCreateForm extends AppCompatActivity {

    ActivityOrderCreateFormBinding binding;
    FirebaseDatabase database;
    FirebaseFirestore db;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    String sellerId, storeId;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrderCreateFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();

        product = getIntent().getSerializableExtra("product", Product.class);
        
        sellerId = auth.getCurrentUser().getUid();

        binding.tvSellerId.setText("Seller: " + sellerId);
        binding.tvProductId.setText("Product: " + product.getProductTitle());
        binding.tvPrice.setText("Rs. 0.00");

        binding.etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                if (!input.isEmpty()) {
                    int qty = Integer.parseInt(input);
                    double total = qty * product.getPrice(); // FIX: use price from product directly
                    binding.tvPrice.setText(String.format("Rs. %.2f", total));
                } else {
                    binding.tvPrice.setText("Rs. 0.00");
                }
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        binding.btnCreateOrder.setOnClickListener(e -> {
            String address = binding.etShippingAddress.getText().toString().trim();
            String quantityStr = binding.etQuantity.getText().toString().trim();

            if (address.isEmpty() || quantityStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            double totalPrice = quantity * product.getPrice();

            // Check stock before creating order
            database.getReference().child("products").child(product.getProductId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Product latestProduct = snapshot.getValue(Product.class);
                            if (latestProduct == null) return;

                            if (latestProduct.getQuantity() == 0) {
                                Toast.makeText(OrderCreateForm.this, "Item is out of stock", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (latestProduct.getQuantity() < quantity) {
                                Toast.makeText(OrderCreateForm.this, "Only " + latestProduct.getQuantity() + " items available", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Build order
                            OrderModel order = new OrderModel(
                                    sellerId,
                                    auth.getCurrentUser().getUid(), // FIX: buyerId = current user UID
                                    product.getProductId(),
                                    product.getProductTitle(),
                                    product.getMainImage(),
                                    quantity,
                                    product.getPrice()
                            );
                            order.setCreatedAt(new Date().getTime());
                            order.setOrderStatus("Processing");
                            order.setShippingAddress(address);
                            order.setTotalPrice(totalPrice);
                            order.setPaymentMethod("Cash on delivery");
                            order.setBuyerName(auth.getCurrentUser().getDisplayName());
                            order.setBuyerPhone(auth.getCurrentUser().getPhoneNumber());

                            fetchStoreIdAndCreateOrder(order, quantity);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(OrderCreateForm.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        binding.backArrow.setOnClickListener(e -> finish());
    }

    private void fetchStoreIdAndCreateOrder(OrderModel order, int quantity) {
        // FIX: use addListenerForSingleValueEvent (not addValueEventListener) to avoid leaks
        database.getReference().child("sellers").child(sellerId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Toast.makeText(OrderCreateForm.this, "Seller data not found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SellerModel seller = snapshot.getValue(SellerModel.class);
                        if (seller == null || seller.getStoreId() == null) {
                            Toast.makeText(OrderCreateForm.this, "Store ID not found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        storeId = seller.getStoreId();
                        createOrder(order, quantity);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(OrderCreateForm.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createOrder(OrderModel order, int quantity) {
        DocumentReference orderRef = db.collection("stores")
                .document(storeId)
                .collection("manualOrders")
                .document();

        order.setOrderId(orderRef.getId());

        orderRef.set(order).addOnSuccessListener(unused -> {
            // Deduct stock after order is saved
            database.getReference()
                    .child("products")
                    .child(product.getProductId())
                    .child("quantity")
                    .runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                            Integer current = currentData.getValue(Integer.class);
                            if (current == null) return Transaction.success(currentData);
                            currentData.setValue(current - quantity);
                            return Transaction.success(currentData);
                        }

                        @Override
                        public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
                            Toast.makeText(OrderCreateForm.this, "Order created successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to create order: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }
}