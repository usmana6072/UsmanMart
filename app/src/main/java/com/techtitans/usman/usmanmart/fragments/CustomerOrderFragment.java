package com.techtitans.usman.usmanmart.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.techtitans.usman.usmanmart.R;
import com.techtitans.usman.usmanmart.databinding.FragmentCustomerOrderBinding;
import com.techtitans.usman.usmanmart.models.OrderModel;
import com.techtitans.usman.usmanmart.models.SellerModel;
import com.techtitans.usman.usmanmart.recyclerViewAdapters.OrderAdapterOrderPage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CustomerOrderFragment extends Fragment {

    public CustomerOrderFragment() {
        // Required empty public constructor
    }

    FragmentCustomerOrderBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    OrderAdapterOrderPage adapter;
    FirebaseFirestore db;
    String storeId;
    private DocumentSnapshot lastVisible = null;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    ArrayList<OrderModel> list=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentCustomerOrderBinding.inflate(inflater, container, false);
        adapter=new OrderAdapterOrderPage(list,getContext());
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        binding.RecyclerViewCustomerOrder.setAdapter(adapter);
        binding.RecyclerViewCustomerOrder.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.btnNext.setOnClickListener(e->{
            loadMoreCustomerOrders();
        });
        loadCustomerOrder();

        binding.swipeRefreshLayout.setOnRefreshListener(()-> {
                    refreshOrders();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 3000);
                });
            // Inflate the layout for this fragment
        return binding.getRoot();
        }


    private void refreshOrders() {
        DocumentSnapshot lastVisible = null;
        boolean isLoading = false;
        boolean isLastPage = false;
        loadCustomerOrder();
    }

    private void loadCustomerOrder() {

        if (isLoading || isLastPage) return;

        list.clear();
        isLoading = true;

        Query query = db.collection("orders")
                .whereEqualTo("sellerId", auth.getUid())
                .limit(10);

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {

            if (!queryDocumentSnapshots.isEmpty()) {

                list.clear();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    OrderModel order = document.toObject(OrderModel.class);
                    list.add(order);
                }

                lastVisible = queryDocumentSnapshots
                        .getDocuments()
                        .get(queryDocumentSnapshots.size() - 1);

            } else {
                isLastPage = true;
            }

            adapter.notifyDataSetChanged();
            isLoading = false;
        });
    }

    private void loadMoreCustomerOrders() {

        if (isLoading || isLastPage || lastVisible == null) return;

        isLoading = true;

        Query nextQuery = db.collection("orders")
                .whereEqualTo("sellerId", auth.getUid())
                .startAfter(lastVisible)
                .limit(10);

        nextQuery.get().addOnSuccessListener(queryDocumentSnapshots -> {

            if (!queryDocumentSnapshots.isEmpty()) {

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    OrderModel order = document.toObject(OrderModel.class);
                    list.add(order);
                }

                lastVisible = queryDocumentSnapshots
                        .getDocuments()
                        .get(queryDocumentSnapshots.size() - 1);

            } else {
                isLastPage = true;
            }

            adapter.notifyDataSetChanged();
            isLoading = false;
        });
    }
}