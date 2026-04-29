package com.techtitans.usman.usmanmart.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtitans.usman.usmanmart.R;
import com.techtitans.usman.usmanmart.databinding.FragmentProductsBinding;
import com.techtitans.usman.usmanmart.forms.AddProductActivity;
import com.techtitans.usman.usmanmart.models.Product;
import com.techtitans.usman.usmanmart.recyclerViewAdapters.ProductAdapterProductsPage;

import java.util.ArrayList;

public class ProductsFragment extends Fragment {

    FirebaseDatabase database;
    FirebaseAuth auth;
    String storeId="";
    ProductAdapterProductsPage adapter;

    public ProductsFragment() {
        // Required empty public constructor
    }

    FragmentProductsBinding binding;
    ArrayList<Product> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentProductsBinding.inflate(inflater, container, false);
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        list=new ArrayList<>();
        adapter=new ProductAdapterProductsPage(list,getContext());
        binding.recyclerViewProductsFragment.setAdapter(adapter);
        binding.recyclerViewProductsFragment.setLayoutManager(new LinearLayoutManager(getContext()));

        loadDataFromDataBase();




        binding.addProductBtn.setOnClickListener(e->{
            startActivity(new Intent(getContext(), AddProductActivity.class));
        });

        return binding.getRoot();

    }

    private void loadDataFromDataBase() {

        database.getReference()
                .child("sellers")
                .child(auth.getUid())
                .child("storeId")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()) {

                            storeId = snapshot.getValue(String.class);

                            database.getReference()
                                    .child("storeProducts")
                                    .child(storeId)
                                    .addValueEventListener(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot1) {

                                            if(snapshot1.exists()) {

                                                list.clear(); // VERY IMPORTANT

                                                for(DataSnapshot snapshot2 : snapshot1.getChildren()) {

                                                    String productId = snapshot2.getValue(String.class);

                                                    database.getReference()
                                                            .child("products")
                                                            .child(productId)
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {

                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                    Product product = snapshot.getValue(Product.class);

                                                                    if(product != null){
                                                                        list.add(product);
                                                                        adapter.notifyDataSetChanged();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) { }
                                                            });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) { }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }}