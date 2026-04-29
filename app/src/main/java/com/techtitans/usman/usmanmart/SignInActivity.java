package com.techtitans.usman.usmanmart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.FirebaseDatabase;
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

        // If already logged in → Check store
        if (auth.getCurrentUser()!=null) {
            checkStoreAndNavigate(auth.getUid());
        }
        EdgeToEdge.enable(this);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        dialog = new ProgressDialog(this);
        dialog.setTitle("Login");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

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
    }

    private void checkStoreAndNavigate(String uid) {
        database.getReference()
                .child("sellers")
                .child(uid)
                .child("storeId")
                .get()
                .addOnSuccessListener(snapshot -> {

                    dialog.dismiss();

                    if (snapshot.exists()) {
                        goToMain();
                    } else {
                        goToStoreForm();
                    }
                });
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToStoreForm() {
        Intent intent = new Intent(this, StoreFormActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}