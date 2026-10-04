package com.techtitans.usman.usmanmart;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cloudinary.android.MediaManager;
import com.techtitans.usman.usmanmart.databinding.ActivityMainBinding;
import com.techtitans.usman.usmanmart.fragments.AboutFragment;
import com.techtitans.usman.usmanmart.fragments.CustomerOrderFragment;
import com.techtitans.usman.usmanmart.fragments.HomeFragment;
import com.techtitans.usman.usmanmart.fragments.ManualOrdersFragment;
import com.techtitans.usman.usmanmart.fragments.MessageFragment;
import com.techtitans.usman.usmanmart.fragments.OrderFragment;
import com.techtitans.usman.usmanmart.fragments.ProductsFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        initConfig();
        setSupportActionBar(binding.toolbar2);
        Toolbar toolbar=binding.toolbar2;
        TextView textview=toolbar.findViewById(R.id.textView3);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainFragment,new HomeFragment())
                .commit();
        textview.setText(R.string.app_name);

        //permission request
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){

            if(ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.POST_NOTIFICATIONS
                        },
                        101);
            }
        }

        binding.BottomNav.setOnItemSelectedListener(e->{
            if(e.getItemId()==R.id.homeItem){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.MainFragment,new HomeFragment())
                        .commit();
                textview.setText(R.string.app_name);
            }
            else if(e.getItemId()==R.id.orderItem) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.MainFragment, new OrderFragment())
                        .commit();
                textview.setText("Orders Page");
            }
            else if(e.getItemId()==R.id.messageItem) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.MainFragment, new MessageFragment())
                        .commit();
                textview.setText("Message Page");
            }
            else if(e.getItemId()==R.id.productItem) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.MainFragment, new ProductsFragment())
                        .commit();
                textview.setText("Products");
            }
            else if(e.getItemId()==R.id.aboutItem) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.MainFragment, new AboutFragment())
                        .commit();
                textview.setText("About");
            }

                return true;
        });
    }

    private void initConfig(){
        try {
            MediaManager.get();
        } catch (Exception e) {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name","dyfjjuzkv");
            config.put("api_key","385755796768381");
            config.put("api_secret","0I-6iNQBkNMrYq6LlKTsiKZ5ag8");
            MediaManager.init(this, config);
        }
    }

    public void navigateToTab(int itemId) {
        binding.BottomNav.setSelectedItemId(itemId);
    }
}