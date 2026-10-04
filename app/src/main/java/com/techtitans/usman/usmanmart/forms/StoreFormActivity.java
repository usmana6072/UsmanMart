package com.techtitans.usman.usmanmart.forms;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techtitans.usman.usmanmart.MainActivity;
import com.techtitans.usman.usmanmart.databinding.ActivityStoreFormBinding;
import com.techtitans.usman.usmanmart.models.StoreModel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StoreFormActivity extends AppCompatActivity {

    ActivityStoreFormBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    String storeId,storeName,logo,description,category,phone,address;
    Uri uri;
    ActivityResultLauncher<Intent> imagePickerLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityStoreFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        initConfig();

        imagePickerLauncher=
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                    if(result.getResultCode()==RESULT_OK){
                        uri=result.getData().getData();
                        binding.imageViewLogo.setImageURI(uri);
                        uploadImage();
                    }else if(result.getResultCode()== ImagePicker.RESULT_ERROR){
                        Toast.makeText(this, "Error occurred while uploading image try again", Toast.LENGTH_SHORT).show();
                    }
                });

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

        binding.imageViewLogo.setOnClickListener(e->{
            Intent intent=ImagePicker.with(this)
                    .crop()
                    .galleryOnly()
                    .cropFreeStyle()
                    .createIntent();
            imagePickerLauncher.launch(intent);
        });
        binding.button.setOnClickListener(e->{
            storeName=binding.etStoreName.getText().toString().trim();
            phone=binding.etSellerPhone.getText().toString().trim();
            description=binding.etStoreDes.getText().toString().trim();
            address=binding.etAddress.getText().toString().trim();
            category=binding.spinner.getSelectedItem().toString().trim();
            if(!(storeName.isEmpty() || description.isEmpty() || address.isEmpty()||category.isEmpty()) && logo!=null ){
                assert user != null;
                StoreModel model = getStoreModel(user);
                createStore(model);
            }
            else{
                Toast.makeText(this, "No Field should be empty", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @NonNull
    private StoreModel getStoreModel(FirebaseUser user) {
        StoreModel model=new StoreModel(user.getUid(),storeName);
        model.setDescription(description);
        model.setAddress(address);
        model.setEmail(user.getEmail());
        model.setStoreLogo(logo);
        model.setCategory(category);
        model.setPhone(phone);
        model.setApproved(true);
        model.setCreatedAt(new Date().getTime());
        model.setTotalProducts(0);
        model.setTotalOrders(0);
        model.setRating(200);
        return model;
    }

    private void initConfig(){
        Map config=new HashMap();
        config.put("cloud_name","dyfjjuzkv");
        config.put("api_key","385755796768381");
        config.put("api_secret","0I-6iNQBkNMrYq6LlKTsiKZ5ag8");
        MediaManager.init(this,config);
    }

    private void  uploadImage(){
        MediaManager.get().upload(uri).unsigned("UsmanMart").callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {

            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {

            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                logo=resultData.get("secure_url").toString();
                Toast.makeText(StoreFormActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                Toast.makeText(StoreFormActivity.this, error.getDescription(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {

            }
        }).dispatch();
    }

    boolean result=false;

    public void createStore(StoreModel model){

        DatabaseReference reference = database.getReference()
                .child("stores")
                .push();

        model.setStoreId(reference.getKey());

        reference.setValue(model).addOnSuccessListener(unused -> {

            database.getReference()
                    .child("sellers")
                    .child(model.getSellerId())
                    .child("storeId")
                    .setValue(model.getStoreId())
                    .addOnSuccessListener(unused1 -> {
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                                .edit()
                                .putBoolean("has_store_" + model.getSellerId(), true)
                                .apply();

                        Toast.makeText(this, "Store Created Successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(StoreFormActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    });

        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }}