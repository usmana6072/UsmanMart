package com.techtitans.usman.usmanmart.fragments;

import static android.app.ProgressDialog.show;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
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
import com.techtitans.usman.usmanmart.databinding.FragmentManualOrdersBinding;
import com.techtitans.usman.usmanmart.forms.OrderCreateForm;
import com.techtitans.usman.usmanmart.models.OrderModel;
import com.techtitans.usman.usmanmart.models.SellerModel;
import com.techtitans.usman.usmanmart.recyclerViewAdapters.OrderAdapterOrderPage;

import java.util.ArrayList;

public class ManualOrdersFragment extends Fragment {

    public ManualOrdersFragment() {
        // Required empty public constructor
    }

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseFirestore db;
    FragmentManualOrdersBinding binding;
    String storeId="";
    OrderAdapterOrderPage adapter;
    private DocumentSnapshot lastVisible = null;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    ArrayList<OrderModel> list =new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentManualOrdersBinding.inflate(inflater, container, false);
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        getStoreId();

        adapter=new OrderAdapterOrderPage(list,getContext());
        binding.recyclerViewManualOrders.setAdapter(adapter);
        binding.recyclerViewManualOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.btnNext.setOnClickListener(e->{
            loadMoreManualOrders();
        });
        binding.swipeRefresh.setOnRefreshListener(() -> {
            refreshManualOrders();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.swipeRefresh.setRefreshing(false);
                }
            }, 3000);
        });

        return binding.getRoot();
    }

    private void refreshManualOrders() {
        DocumentSnapshot lastVisible = null;
        boolean isLoading = false;
        boolean isLastPage = false;
        loadManualOrders();
    }

    private void getStoreId() {
        database.getReference().child("sellers").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(getContext(),
                            "Seller data not found",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                SellerModel seller = snapshot.getValue(SellerModel.class);

                if (seller == null || seller.getStoreId() == null) {
                    Toast.makeText(getContext(),
                            "Store ID is null",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                storeId=seller.getStoreId();
                loadManualOrders();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadManualOrders() {

        if (isLoading || isLastPage) return;

        list.clear();
        isLoading = true;

        Query query = db.collection("stores")
                .document(storeId)
                .collection("manualOrders")
                .orderBy("createdAt", Query.Direction.DESCENDING)
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

    private void loadMoreManualOrders() {

        if (isLoading || isLastPage || lastVisible == null) return;

        isLoading = true;

        Query nextQuery = db.collection("stores")
                .document(storeId)
                .collection("manualOrders")
                .orderBy("createdAt", Query.Direction.DESCENDING)
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