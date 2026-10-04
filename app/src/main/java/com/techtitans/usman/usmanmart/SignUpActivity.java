package com.techtitans.usman.usmanmart;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.techtitans.usman.usmanmart.databinding.ActivitySignUpBinding;
import com.techtitans.usman.usmanmart.forms.StoreFormActivity;
import com.techtitans.usman.usmanmart.models.SellerModel;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    ActivitySignUpBinding binding;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSigninClient;
    private final int RC_SIGN_IN=56;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSigninClient=GoogleSignIn.getClient(this,gso);

        dialog=new ProgressDialog(this);
                dialog.setTitle("Creating Your Account");
                dialog.setMessage("please wait");

        binding.btnSignUP.setOnClickListener(e->{
            dialog.show();
            String email,password,username,id;
            email=binding.etEmail.getText().toString();
            password=binding.etPassword.getText().toString();
            username=binding.etUserName.getText().toString();
            if(!email.isEmpty() && !password.isEmpty() && !username.isEmpty())
                auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user=authResult.getUser();
                        SellerModel model=new SellerModel(user.getUid(),username,email,password);
                        database.getReference().child("sellers").child(user.getUid()).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                Toast.makeText(SignUpActivity.this, "SignUp Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(SignUpActivity.this, StoreFormActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {
                                        if(!task.isSuccessful())
                                            return;
                                        storeToken(task.getResult());
                                    }
                                });
                            }
                        });
                   }
                });
        });

        binding.btnGoogle.setOnClickListener(e->{
            Intent signinIntent=mGoogleSigninClient.getSignInIntent();
            startActivityForResult(signinIntent,RC_SIGN_IN);
        });

        binding.tvSignIn.setOnClickListener(e->{
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account=task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String token) {
        dialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(token, null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser user = auth.getCurrentUser();
                        if (user == null) {
                            dialog.dismiss();
                            Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            return;
                        }

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
                                                .setValue(model);
                                        Intent intent = new Intent(SignUpActivity.this, StoreFormActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }else{
                                        dialog.dismiss();
                                        Intent intent = new Intent(SignUpActivity.this, StoreFormActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
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

                    } else {
                        dialog.dismiss();
                        Toast.makeText(this,
                                "Google Sign-In Failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

    }

    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void storeToken(String result) {
        database.getReference().child("sellers").child(auth.getUid()).child("fcmToken").setValue(result);
    }
}