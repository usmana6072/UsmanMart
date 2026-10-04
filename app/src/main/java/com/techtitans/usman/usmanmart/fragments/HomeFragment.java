package com.techtitans.usman.usmanmart.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techtitans.usman.usmanmart.EditSellerDetails;
import com.techtitans.usman.usmanmart.MainActivity;
import com.techtitans.usman.usmanmart.OrderDetailsActivity;
import com.techtitans.usman.usmanmart.R;
import com.techtitans.usman.usmanmart.databinding.FragmentHomeBinding;
import com.techtitans.usman.usmanmart.models.OrderModel;
import com.techtitans.usman.usmanmart.models.Product;
import com.techtitans.usman.usmanmart.models.SellerModel;
import com.techtitans.usman.usmanmart.models.StoreModel;
import com.techtitans.usman.usmanmart.recyclerViewAdapters.RecentOrdersAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private FirebaseAuth auth=FirebaseAuth.getInstance();
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    private SellerModel sellerModel;
    private StoreModel storeModel;

    private final ArrayList<OrderModel> recentOrders = new ArrayList<>();
    private RecentOrdersAdapter ordersAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        setupRecyclerView();
        setupQuickActions();

        binding.swipeRefresh.setOnRefreshListener(this::loadDashboard);
        loadDashboard();
        return binding.getRoot();
    }

    private void setupRecyclerView() {
        ordersAdapter = new RecentOrdersAdapter(requireContext(), recentOrders, order -> {
            // Heads up: OrderModel implements Serializable, but OrderDetailsActivity currently
            // reads it via getParcelableExtra(). That call returns null for a Serializable.
            // Either change OrderModel to implement Parcelable, or change OrderDetailsActivity
            // to use intent.getSerializableExtra("order", OrderModel.class) instead.
            Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
            intent.putExtra("order", order);
            startActivity(intent);
        });
        binding.rvRecentOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecentOrders.setAdapter(ordersAdapter);
    }

    private void setupQuickActions() {
        binding.actionProducts.setOnClickListener(v -> goToTab(R.id.productItem));
        binding.actionOrders.setOnClickListener(v -> goToTab(R.id.orderItem));
        binding.actionMessages.setOnClickListener(v -> goToTab(R.id.messageItem));
        binding.tvViewAllOrders.setOnClickListener(v -> goToTab(R.id.orderItem));

        binding.actionEditStore.setOnClickListener(v -> {
            if (storeModel == null || sellerModel == null) {
                Toast.makeText(getContext(), "Still loading your store info, try again in a second", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(getActivity(), EditSellerDetails.class);
            intent.putExtra("store", storeModel);
            intent.putExtra("seller", sellerModel);
            startActivity(intent);
        });
    }

    private void goToTab(int itemId) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateToTab(itemId);
        }
    }

    private void loadDashboard() {
        String uid = auth.getUid();
        if (uid == null) {
            binding.swipeRefresh.setRefreshing(false);
            return;
        }

        binding.swipeRefresh.setRefreshing(true);

        database.getReference().child("sellers").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sellerModel = snapshot.getValue(SellerModel.class);

                        if (sellerModel == null) {
                            binding.swipeRefresh.setRefreshing(false);
                            return;
                        }

                        bindSellerHeader();
                        loadProducts(uid);

                        if (sellerModel.getStoreId() != null && !sellerModel.getStoreId().isEmpty()) {
                            loadStore(sellerModel.getStoreId());
                            loadOrders(uid, sellerModel.getStoreId());
                        } else {
                            binding.swipeRefresh.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        binding.swipeRefresh.setRefreshing(false);
                        Toast.makeText(getContext(), "Couldn't load dashboard: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void bindSellerHeader() {
        String name = sellerModel.getUserName();
        String firstName = (name != null && !name.trim().isEmpty()) ? name.trim().split(" ")[0] : "Seller";

        binding.tvGreeting.setText("Welcome back, " + firstName);
        binding.tvAvatarInitial.setText(
                (name != null && !name.trim().isEmpty())
                        ? String.valueOf(name.trim().charAt(0)).toUpperCase(Locale.getDefault())
                        : "S");
    }

    private void loadStore(String storeId) {
        database.getReference().child("stores").child(storeId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        storeModel = snapshot.getValue(StoreModel.class);
                        if (storeModel == null) return;

                        binding.tvStoreName.setText(
                                storeModel.getStoreName() != null ? storeModel.getStoreName() : "Your Store");
                        binding.tvStoreRating.setText(String.format(Locale.getDefault(), "%.1f", storeModel.getRating()));
                        binding.bannerPendingApproval.setVisibility(storeModel.isApproved() ? View.GONE : View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Header still shows seller info even if store fetch fails.
                    }
                });
    }

    private void loadProducts(String uid) {
        database.getReference().child("products")
                .orderByChild("sellerId").equalTo(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int total = 0, lowStock = 0, outOfStock = 0, onSale = 0;

                        for (DataSnapshot child : snapshot.getChildren()) {
                            Product product = child.getValue(Product.class);
                            if (product == null) continue;

                            total++;
                            if (product.getQuantity() <= 0) outOfStock++;
                            else if (product.getQuantity() <= 5) lowStock++;
                            if (product.getSale() > 0) onSale++;
                        }

                        binding.tvTotalProducts.setText(String.valueOf(total));
                        binding.tvLowStock.setText(String.valueOf(lowStock));
                        binding.tvOutOfStock.setText(String.valueOf(outOfStock));
                        binding.tvOnSale.setText(String.valueOf(onSale));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Leave stat cards at their last known values.
                    }
                });
    }

    private void loadOrders(String uid, String storeId) {
        ArrayList<OrderModel> combined = new ArrayList<>();

        firestore.collection("orders")
                .whereEqualTo("sellerId", uid)
                .get()
                .addOnSuccessListener(customerSnap -> {
                    for (DocumentSnapshot doc : customerSnap.getDocuments()) {
                        OrderModel order = doc.toObject(OrderModel.class);
                        if (order == null) continue;
                        if (order.getOrderId() == null) order.setOrderId(doc.getId());
                        combined.add(order);
                    }

                    firestore.collection("stores").document(storeId)
                            .collection("manualOrders")
                            .get()
                            .addOnSuccessListener(manualSnap -> {
                                for (DocumentSnapshot doc : manualSnap.getDocuments()) {
                                    OrderModel order = doc.toObject(OrderModel.class);
                                    if (order == null) continue;
                                    if (order.getOrderId() == null) order.setOrderId(doc.getId());
                                    combined.add(order);
                                }
                                bindOrderStats(combined);
                            })
                            .addOnFailureListener(e -> bindOrderStats(combined));
                })
                .addOnFailureListener(e -> {
                    binding.swipeRefresh.setRefreshing(false);
                    Toast.makeText(getContext(), "Couldn't load orders: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void bindOrderStats(ArrayList<OrderModel> orders) {
        int totalOrders = orders.size();
        int pending = 0;
        double revenue = 0;

        for (OrderModel order : orders) {
            String status = order.getOrderStatus() == null ? "" : order.getOrderStatus().trim();
            if (status.equalsIgnoreCase("Completed")) {
                revenue += order.getTotalPrice();
            } else {
                pending++;
            }
        }

        binding.tvTotalOrders.setText(String.valueOf(totalOrders));
        binding.tvPendingOrders.setText(String.valueOf(pending));
        binding.tvTotalRevenue.setText("Rs " + formatAmount(revenue));

        Collections.sort(orders, (a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()));

        recentOrders.clear();
        recentOrders.addAll(orders.subList(0, Math.min(5, orders.size())));
        ordersAdapter.notifyDataSetChanged();

        binding.layoutEmptyOrders.setVisibility(recentOrders.isEmpty() ? View.VISIBLE : View.GONE);
        binding.rvRecentOrders.setVisibility(recentOrders.isEmpty() ? View.GONE : View.VISIBLE);

        binding.swipeRefresh.setRefreshing(false);
    }

    private String formatAmount(double amount) {
        if (amount >= 100000) return String.format(Locale.getDefault(), "%.1fL", amount / 100000);
        if (amount >= 1000) return String.format(Locale.getDefault(), "%.1fK", amount / 1000);
        return String.format(Locale.getDefault(), "%.0f", amount);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
