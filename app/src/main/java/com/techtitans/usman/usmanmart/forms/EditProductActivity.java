package com.techtitans.usman.usmanmart.forms;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
import com.techtitans.usman.usmanmart.R;
import com.techtitans.usman.usmanmart.databinding.ActivityEditProductBinding;
import com.techtitans.usman.usmanmart.models.Product;

import java.util.Map;

public class EditProductActivity extends AppCompatActivity {

    ActivityEditProductBinding binding;
    FirebaseDatabase database;
    String[] imageURL=new String[6];
    Product product;
    Uri uri;
    int currentImage=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityEditProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        product=getIntent().getSerializableExtra("product", Product.class);
        database=FirebaseDatabase.getInstance("https://usmanmart-cce76-default-rtdb.firebaseio.com/");
        int type=getIntent().getIntExtra("type",1);
        TextView view=binding.toolbar2.findViewById(R.id.textView3);
        Button button=binding.saveChangesBtn;
        if(type==1){
            view.setText("Edit Product Details");
            button.setText("Save Changes");
        }else{
            view.setText("Delete Product");
            button.setText("Delete Product");
        }
        setSpinner();
        setIntentProductData();
        setImagesActionListener();

        setSupportActionBar(binding.toolbar2);
        ImageView backArrow=binding.toolbar2.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(e->{
            finish();
        });
        
        binding.saveChangesBtn.setOnClickListener(e->{
            if(type==1) {
                if (!updateDataInProduct()) {
                    Toast.makeText(this, "No Field Should be Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                database.getReference().child("products").child(product.getProductId()).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditProductActivity.this, "Product Edited Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }else if(type==2){
                database.getReference().child("products").child(product.getProductId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("categories").child(product.getCategory()).child(product.getProductId()).removeValue();
                        database.getReference().child("sellers").child(product.getSellerId()).child("storeId").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    database.getReference().child("storeProducts").child(snapshot.getValue(String.class)).child(product.getProductId()).removeValue();
                                    Toast.makeText(EditProductActivity.this, "Product Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }

        });

    }

    private boolean updateDataInProduct() {
        String s=binding.etProductName.getText().toString();
        if(!s.isEmpty())
            product.setProductTitle(s);
        else
            return false;

        s=binding.etProductQuantity.getText().toString();
        if(!s.isEmpty())
            product.setQuantity(Integer.parseInt(s));
        else
            return false;

        s=binding.etProductPrice.getText().toString();
        if(!s.isEmpty())
            product.setPrice(Double.parseDouble(s));
        else
            return false;

        product.setCategory(binding.spinner.getSelectedItem().toString());

        s=binding.etBulletPoint1.getText().toString();
        s+="&& "+binding.etBulletPoint2.getText().toString();
        s+="&& "+binding.etBulletPoint3.getText().toString();
        product.setBulletPoints(s);

        s=binding.etProductDes.getText().toString();
        if(!s.isEmpty())
            product.setProductDes(s);
        else
            return false;

        s="";
        for(int i=0;i<imageURL.length;i++){
            if(imageURL[i] !=null && !imageURL[i].isEmpty()){
                if(s.isEmpty())
                    s+=imageURL[i];
                else{
                    s+="&&"+imageURL[i];
                }
            }
        }
        if(!s.isEmpty())
            product.setImages(s);
        else
            return false;
        return true;

    }

    private void setIntentProductData() {
        String[] images=product.getImages().split("&&");
        for(int i=0;i<images.length;i++)
            imageURL[i]=images[i];
        Picasso.get().load(imageURL[0]).placeholder(R.drawable.image).into(binding.productImage1);
        Picasso.get().load(imageURL[1]).placeholder(R.drawable.image).into(binding.productImage2);
        Picasso.get().load(imageURL[2]).placeholder(R.drawable.image).into(binding.productImage3);
        Picasso.get().load(imageURL[3]).placeholder(R.drawable.image).into(binding.productImage4);
        Picasso.get().load(imageURL[4]).placeholder(R.drawable.image).into(binding.productImage5);
        Picasso.get().load(imageURL[5]).placeholder(R.drawable.image).into(binding.productImage6);

        binding.etProductName.setText(product.getProductTitle());
        binding.etProductPrice.setText(product.getPrice()+"");
        binding.etProductQuantity.setText(String.valueOf(product.getQuantity()));
        String[] points=product.getBulletPoints().split("&&");
        binding.etBulletPoint1.setText(points[0]);
        binding.etBulletPoint2.setText(points[1]);
        binding.etBulletPoint3.setText(points[2]);
        binding.etProductDes.setText(product.getProductDes());
        binding.etTags.setText(product.getTags());
    }

    private void setSpinner() {
        String[] categories = {
                "Electronics",
                "Clothing",
                "Home & Kitchen",
                "Beauty & Personal Care",
                "Sports & Outdoors",
                "Books",
                "Toys & Games",
                "Health & Wellness",
                "Automotive",
                "Groceries"
        };
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,categories);
        binding.spinner.setAdapter(adapter);

        for(int i=0;i<categories.length;i++)
            if(categories[i].equals(product.getCategory()))
                binding.spinner.setSelection(i);

    }
    private void setImagesActionListener() {
        binding.productImage1.setOnClickListener(e->{
            currentImage=0;
            uploadImage();
        });
        binding.productImage2.setOnClickListener(e->{
            currentImage=1;
            uploadImage();
        });
        binding.productImage3.setOnClickListener(e->{
            currentImage=2;
            uploadImage();
        });
        binding.productImage4.setOnClickListener(e->{
            currentImage=3;
            uploadImage();
        });
        binding.productImage5.setOnClickListener(e->{
            currentImage=4;
            uploadImage();
        });
        binding.productImage6.setOnClickListener(e->{
            currentImage=5;
            uploadImage();
        });
    }

    private final int IMAGE_CODE=8;
    private void uploadImage(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String url="";
        if(requestCode==IMAGE_CODE && data!=null){
            uri=data.getData();
            switch (currentImage){
                case 0:
                    binding.productImage1.setImageURI(uri);
                    break;
                case 1:
                    binding.productImage2.setImageURI(uri);
                    break;
                case 2:
                    binding.productImage3.setImageURI(uri);
                    break;
                case 3:
                    binding.productImage4.setImageURI(uri);
                    break;
                case 4:
                    binding.productImage5.setImageURI(uri);
                    break;
                case 5:
                    binding.productImage6.setImageURI(uri);
                    break;
            }

            MediaManager.get().upload(uri).unsigned("UsmanMart").callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {

                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {

                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    Toast.makeText(EditProductActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    String url=resultData.get("secure_url").toString();
                    imageURL[currentImage]=url;

                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    Toast.makeText(EditProductActivity.this, error.getDescription(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {

                }
            }).dispatch();
        }
    }
}