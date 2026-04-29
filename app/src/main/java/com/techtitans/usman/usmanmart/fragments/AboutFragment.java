package com.techtitans.usman.usmanmart.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.techtitans.usman.usmanmart.EditSellerDetails;
import com.techtitans.usman.usmanmart.R;
import com.techtitans.usman.usmanmart.SignInActivity;
import com.techtitans.usman.usmanmart.databinding.FragmentAboutBinding;
import com.techtitans.usman.usmanmart.models.SellerModel;
import com.techtitans.usman.usmanmart.models.StoreModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class AboutFragment extends Fragment {

    public AboutFragment() {
        // Required empty public constructor
    }

    FragmentAboutBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    SellerModel seller;
    StoreModel store;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentAboutBinding.inflate(inflater, container, false);
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        database.getReference().child("sellers").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    seller = snapshot.getValue(SellerModel.class);

                    database.getReference().child("stores").child(seller.getStoreId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                store=snapshot.getValue(StoreModel.class);
                                binding.tvSellerName.setText(seller.getUserName());
                                binding.tvEmail.setText(seller.getEmail());
                                binding.tvPhone.setText(store.getPhone());
                                binding.tvLocation.setText(store.getAddress());
                                Date date=new Date(store.getCreatedAt());
                                SimpleDateFormat format=new SimpleDateFormat("d:M:y");
                                binding.tvMemberSince.setText(format.format(date).toString());
                                Picasso.get().load(store.getStoreLogo()).placeholder(R.drawable.user).into(binding.ivSellerAvatar);
                                Picasso.get().load(store.getStoreBanner()).placeholder(R.drawable.banner).into(binding.ivBanner);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.ivEdit1.setOnClickListener(e->{
            getContext().startActivity(getIntent());
        });
        binding.ivEdit2.setOnClickListener(e->{
            getContext().startActivity(getIntent());
        });
        binding.ivEdit3.setOnClickListener(e->{
            getContext().startActivity(getIntent());
        });

        binding.ivUpdateBanner.setOnClickListener(e->{
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,32);
        });

        binding.btnLogout.setOnClickListener(e->{
            auth.signOut();
            getContext().startActivity(new Intent(getContext(), SignInActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
        });
        return binding.getRoot();
    }
    private Intent getIntent(){
        Intent intent=new Intent(getContext(), EditSellerDetails.class);
        intent.putExtra("store",store);
        intent.putExtra("seller",seller);
        return intent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || data == null || data.getData() == null) {
            return;
        }

        Uri imageUri = data.getData();
        binding.ivBanner.setImageURI(imageUri);
            uploadBannerImage(imageUri);
    }

    private void uploadBannerImage(Uri imageUri) {
        MediaManager.get().upload(imageUri).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {

            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {

            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                String imageUrl = resultData.get("secure_url").toString();
                database.getReference().child("stores").child(store.getStoreId()).child("storeBanner").setValue(imageUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Banner added successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {

            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {

            }
        }).dispatch();
    }
}