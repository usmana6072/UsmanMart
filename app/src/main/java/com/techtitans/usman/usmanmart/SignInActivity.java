package com.techtitans.usman.usmanmart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.techtitans.usman.usmanmart.databinding.ActivitySignInBinding;
import com.techtitans.usman.usmanmart.forms.StoreFormActivity;
import com.techtitans.usman.usmanmart.models.SellerModel;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 100;
    boolean storeExist=false;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // If logged in, check local cache for instant 0ms launch
        if (auth.getCurrentUser() != null) {
            String uid = auth.getUid();
            boolean hasStoreLocal = getSharedPreferences("app_prefs", MODE_PRIVATE)
                    .getBoolean("has_store_" + uid, false);

            if (hasStoreLocal) {
                goToMain();
                return;
            }
        }

        EdgeToEdge.enable(this);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Session");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

        // If logged in but cache was empty, show dialog while fetching
        if (auth.getCurrentUser() != null) {
            dialog.show();
            checkStoreAndNavigate(auth.getUid());
        }

        setupGoogleSignIn();
        setupClickListeners();
    }

    private void setupClickListeners() {

        binding.btnSignIn.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String pass = binding.etPassword.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Enter email & password", Toast.LENGTH_SHORT).show();
                return;
            }

            dialog.show();

            auth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(authResult -> {
                        dialog.dismiss();
                        checkStoreAndNavigate(auth.getCurrentUser().getUid());
                        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if(!task.isSuccessful())
                                    return;
                                storeToken(task.getResult());
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        dialog.dismiss();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        binding.btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        binding.tvSignUp.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class))
        );
    }

    private void storeToken(String result) {
        database.getReference().child("sellers").child(auth.getUid()).child("fcmToken").setValue(result);
    }


    private void setupGoogleSignIn() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN && data != null) {

            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account =
                        task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                Toast.makeText(this,
                        "Google Sign-In Failed",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String token) {

        dialog.show();

        AuthCredential credential =
                GoogleAuthProvider.getCredential(token, null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {

                    if (!task.isSuccessful()) {
                        dialog.dismiss();
                        Toast.makeText(this,
                                "Authentication Failed",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseUser user = auth.getCurrentUser();
                    if (user == null) {
                        dialog.dismiss();
                        return;
                    }

                    checkSellerExists(user);
                });
    }
    private void checkSellerExists(FirebaseUser user) {

        database.getReference()
                .child("sellers")
                .child(user.getUid())
                .get()
                .addOnSuccessListener(snapshot -> {

                    if (!snapshot.exists()) {

                        SellerModel model = new SellerModel();
                        model.setSellerId(user.getUid());
                        model.setEmail(user.getEmail());
                        model.setUserName(user.getDisplayName());

                        database.getReference()
                                .child("sellers")
                                .child(user.getUid())
                                .setValue(model)
                                .addOnSuccessListener(unused ->
                                        checkStoreAndNavigate(user.getUid()));
                    } else {
                        checkStoreAndNavigate(user.getUid());
                    }
                });
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful())
                    return;
                storeToken(task.getResult());
            }
        });
    }

    private void checkStoreAndNavigate(String uid) {
        database.getReference()
                .child("sellers")
                .child(uid)
                .child("storeId")
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    if (snapshot.exists()) {
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                                .edit()
                                .putBoolean("has_store_" + uid, true)
                                .apply();
                        goToMain();
                    } else {
                        goToStoreForm();
                    }
                })
                .addOnFailureListener(e -> {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void goToStoreForm() {
        Intent intent = new Intent(this, StoreFormActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}