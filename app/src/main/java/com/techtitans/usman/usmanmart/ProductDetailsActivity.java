package com.techtitans.usman.usmanmart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.techtitans.usman.usmanmart.databinding.ActivityProductDetailsBinding;
import com.techtitans.usman.usmanmart.models.Product;

public class ProductDetailsActivity extends AppCompatActivity {

    ActivityProductDetailsBinding binding;
    FirebaseDatabase database;
    String[] images;
    int currentImage=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityProductDetailsBinding.inflate(getLayoutInflater());
        database=FirebaseDatabase.getInstance();
        setContentView(binding.getRoot());
        Product product=getIntent().getSerializableExtra("product",Product.class);
        setSupportActionBar(binding.toolbar2);

        images=product.getImages().split("&&");
        Picasso.get().load(images[0]).placeholder(R.drawable.image).into(binding.ivMainImage);
        binding.tvProductName.setText(product.getProductTitle());
        binding.tvPrice.setText("Original Price: "+product.getPrice()+" PKR");
        binding.tvSalePercentage.setText("Sale "+product.getSale()+"%");
        binding.tvCategory.setText(product.getCategory());
        String[] bulletPoints=product.getBulletPoints().split("&&");
        binding.tvBulletPoint1.setText(bulletPoints[0]);
        binding.tvBulletPoint2.setText(bulletPoints[1]);
        binding.tvBulletPoint3.setText(bulletPoints[2]);
        binding.tvDescription.setText(product.getProductDes());
        String sellerId=product.getSellerId();
        binding.ratingBar.setRating(product.getRating());
        binding.tvRating.setText(product.getRating()+"");
        double salePrice=product.getPrice()-(product.getPrice()*product.getSale()/100);
        binding.tvSalePrice.setText(String.format("Sale Price: %.2f",salePrice));
        database.getReference().child("sellers").child(sellerId).child("storeId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                    return;
                String storeId=snapshot.getValue(String.class);
                database.getReference().child("stores").child(storeId).child("storeName").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            binding.tvStoreName.setText(snapshot.getValue(String.class));
                        }}

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.backArrow.setOnClickListener(e->{
            finish();
        });

        binding.leftMove.setOnClickListener(e->{
            if(images.length==1)
                return;
            if(currentImage==0){
                Picasso.get().load(images[images.length-1]).placeholder(R.drawable.image).into(binding.ivMainImage);
                currentImage=images.length-1;
            }
            else
                Picasso.get().load(images[--currentImage]).placeholder(R.drawable.image).into(binding.ivMainImage);
        });

        binding.rightMove.setOnClickListener(e->{
            if(images.length==1)
                return;
            if(currentImage==images.length-1){
                Picasso.get().load(images[0]).placeholder(R.drawable.image).into(binding.ivMainImage);
                currentImage=0;
            }else
                Picasso.get().load(images[++currentImage]).placeholder(R.drawable.image).into(binding.ivMainImage);

        });
    }

}