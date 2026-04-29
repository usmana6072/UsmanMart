package com.techtitans.usman.usmanmart;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.techtitans.usman.usmanmart.databinding.ActivityEditSellerDetailsBinding;
import com.techtitans.usman.usmanmart.models.SellerModel;
import com.techtitans.usman.usmanmart.models.StoreModel;

public class EditSellerDetails extends AppCompatActivity {

    FirebaseDatabase database;
    ActivityEditSellerDetailsBinding binding;
    StoreModel storeModel;
    SellerModel sellerModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityEditSellerDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        storeModel=getIntent().getSerializableExtra("store", StoreModel.class);
        sellerModel=getIntent().getSerializableExtra("seller",SellerModel.class);
        binding.etAddress.setText(storeModel.getAddress());
        binding.etEmail.setText(storeModel.getEmail());
        binding.etPhone.setText(storeModel.getPhone());
        binding.btnSaveChanges.setOnClickListener(e->{
            String phone,mail,address;
            phone=binding.etPhone.getText().toString();
            mail=binding.etEmail.getText().toString();
            address=binding.etAddress.getText().toString();
            if(phone.isEmpty() || mail.isEmpty() || address.isEmpty()){
                Toast.makeText(this, "No field should be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            sellerModel.setEmail(mail);
            sellerModel.setPhone(phone);
            storeModel.setEmail(mail);
            storeModel.setPhone(phone);
            storeModel.setAddress(address);
            database.getReference().child("sellers").child(sellerModel.getSellerId()).setValue(sellerModel);
            database.getReference().child("stores").child(storeModel.getStoreId()).setValue(storeModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(EditSellerDetails.this, "Information Updated Successfully", Toast.LENGTH_SHORT).show();
                }
            });
        });
        binding.tvDiscard.setOnClickListener(e->{
            finish();
        });
        binding.ivBack.setOnClickListener(e->{
            finish();
        });

    }
}