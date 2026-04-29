package com.techtitans.usman.usmanmart;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.techtitans.usman.usmanmart.databinding.ActivityCreateSaleBinding;
import com.techtitans.usman.usmanmart.models.Product;

public class CreateSaleActivity extends AppCompatActivity {

    FirebaseDatabase database;
    Product product;
    ActivityCreateSaleBinding binding;

    boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityCreateSaleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        product = getIntent().getSerializableExtra("product", Product.class);
        binding.tvProductName.setText(product.getProductTitle());
        binding.tvOriginalPrice.setText(String .format("%.2f",product.getPrice()));
        binding.etSalePercent.setText(String.valueOf(product.getSale()));

        // Percent -> Price
        binding.etSalePercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                if (isUpdating) return;
                if (s.toString().isEmpty()) return;

                isUpdating = true;

                double percent = Double.parseDouble(s.toString());

                double salePrice = product.getPrice() -
                        (product.getPrice() * percent / 100);

                binding.etSalePrice.setText(String.format("%.2f", salePrice));
                isUpdating = false;
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


        // Price -> Percent
        binding.etSalePrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                if (isUpdating) return;
                if (s.toString().isEmpty()) return;

                isUpdating = true;

                double salePrice = Double.parseDouble(s.toString());

                double percent =
                        ((product.getPrice() - salePrice) / product.getPrice()) * 100;

                binding.etSalePercent.setText(String.format("%.2f", percent));
                product.setSale(percent);
                isUpdating = false;
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        
        binding.btnCreateSale.setOnClickListener(e->{
            database.getReference().child("products").child(product.getProductId()).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(CreateSaleActivity.this, "Sale added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });

        binding.backArrow.setOnClickListener(e->{
            finish();
        });
    }
}