package com.techtitans.usman.usmanmart.forms;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtitans.usman.usmanmart.R;
import com.techtitans.usman.usmanmart.databinding.ActivityAddProductBinding;
import com.techtitans.usman.usmanmart.databinding.ActivityStoreFormBinding;
import com.techtitans.usman.usmanmart.models.Product;
import com.techtitans.usman.usmanmart.models.SellerModel;

import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {

    ActivityAddProductBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    Uri uri;
    double price;
    String sellerId,title,category,des;
    String bulletPoints;
    String tags;
    StringBuilder imagesURL=new StringBuilder();
    int currentImage=0;
    int quantity=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityAddProductBinding.inflate(getLayoutInflater());
//        initConfig();
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance("https://usmanmart-cce76-default-rtdb.firebaseio.com/");
        auth=FirebaseAuth.getInstance();

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
        binding.spinner.setSelection(0);

        setSupportActionBar(binding.toolbar2);
        ImageView backArrow=binding.toolbar2.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(e->{
            finish();
        });
        new Thread(this::setImagesActionListener).start();

        binding.uploadButton.setOnClickListener(e-> {
            if (!getTextViewsData())
                return;
            Product product = new Product(auth.getUid(), title, des, category, imagesURL.toString(), bulletPoints, tags, price,quantity);
            DatabaseReference reference = database.getReference().child("products").push();
            product.setProductId(reference.getKey());
            if(product!=null)
                reference.setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    database.getReference().child("categories").child(product.getCategory()).push().setValue(product.getProductId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    if (auth.getUid() == null) {
                        Toast.makeText(AddProductActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    database.getReference().child("sellers").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                Toast.makeText(AddProductActivity.this,
                                        "Seller data not found",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                            SellerModel seller = snapshot.getValue(SellerModel.class);

                            if (seller == null || seller.getStoreId() == null) {
                                Toast.makeText(AddProductActivity.this,
                                        "Store ID is null",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                            database.getReference()
                                    .child("storeProducts")
                                    .child(seller.getStoreId())
                                    .push()
                                    .setValue(product.getProductId());
                            Toast.makeText(AddProductActivity.this, "Product Uploaded successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(AddProductActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            });


        }

    private boolean getTextViewsData() {
        title=binding.etProductName.getText().toString();
        des=binding.etProductDes.getText().toString();
        category=binding.spinner.getSelectedItem().toString();
        String p=binding.etProductPrice.getText().toString();
        String b1=binding.etBulletPoint1.getText().toString();
        String b2=binding.etBulletPoint1.getText().toString();
        String b3=binding.etBulletPoint1.getText().toString();
        String quan=binding.etProductQuantity.getText().toString();
        tags=binding.etTags.getText().toString();
        if(!(title.isEmpty()|| des.isEmpty()||p.isEmpty() || b1.isEmpty() || b2.isEmpty() || b3.isEmpty() ||imagesURL.isEmpty() || quan.isEmpty())){
            price=Double.parseDouble(p);
            quantity=Integer.parseInt(quan);
            bulletPoints=b1+"&&"+b2+"&&"+b3;
            return true;
        }else {
            Toast.makeText(this, "No Field should be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void setImagesActionListener() {
        binding.productImage1.setOnClickListener(e->{
            currentImage=1;
            uploadImage();
        });
        binding.productImage2.setOnClickListener(e->{
            currentImage=2;
            uploadImage();
        });
        binding.productImage3.setOnClickListener(e->{
            currentImage=3;
            uploadImage();
        });
        binding.productImage4.setOnClickListener(e->{
            currentImage=4;
            uploadImage();
        });
        binding.productImage5.setOnClickListener(e->{
            currentImage=5;
            uploadImage();
        });
        binding.productImage6.setOnClickListener(e->{
            currentImage=6;
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
                case 1:
                    binding.productImage1.setImageURI(uri);
                    break;
                case 2:
                    binding.productImage2.setImageURI(uri);
                    break;
                case 3:
                    binding.productImage3.setImageURI(uri);
                    break;
                case 4:
                    binding.productImage4.setImageURI(uri);
                    break;
                case 5:
                    binding.productImage5.setImageURI(uri);
                    break;
                case 6:
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
                    Toast.makeText(AddProductActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    String url=resultData.get("secure_url").toString();
                    if(imagesURL.isEmpty())
                            imagesURL.append(url);
                    else{
                        imagesURL.append("&&");
                        imagesURL.append(url);
                    }

                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    Toast.makeText(AddProductActivity.this, error.getDescription(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {

                }
            }).dispatch();
        }
    }

    public String[] getImagesURL(String url){
        return imagesURL.toString().split("&&");
    }

    private void initConfig(){
        Map config=new HashMap();
        config.put("cloud_name","dyfjjuzkv");
        config.put("api_key","385755796768381");
        config.put("api_secret","0I-6iNQBkNMrYq6LlKTsiKZ5ag8");
        MediaManager.init(this,config);
    }
}