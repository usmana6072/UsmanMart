package com.techtitans.usman.usmanmart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

<<<<<<< HEAD
import com.google.firebase.FirebaseApp;
=======
>>>>>>> d8f0a10 (backup whole project)
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.techtitans.usman.usmanmart.databinding.ActivityChatBinding;
import com.techtitans.usman.usmanmart.helperclasses.Data;
import com.techtitans.usman.usmanmart.helperclasses.FcmAccessTokenManager;
import com.techtitans.usman.usmanmart.helperclasses.NotificationSender;
import com.techtitans.usman.usmanmart.interfaces.ApiService;
import com.techtitans.usman.usmanmart.models.BuyerModel;
import com.techtitans.usman.usmanmart.models.MessageModel;
<<<<<<< HEAD
import com.techtitans.usman.usmanmart.recyclerViewAdapters.ChatDetailsAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
=======
import com.techtitans.usman.usmanmart.databinding.ActivityChatBinding;
import com.techtitans.usman.usmanmart.models.StoreModel;
import com.techtitans.usman.usmanmartbuyer.recyclerViewAdapters.ChatDetailsAdapter;

import java.util.ArrayList;
import java.util.Date;
>>>>>>> d8f0a10 (backup whole project)

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

<<<<<<< HEAD
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
=======
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
>>>>>>> d8f0a10 (backup whole project)
    ActivityChatBinding binding;

    String receiverId;
    String senderId;
    String receiverRoom;
    String senderRoom;
<<<<<<< HEAD
    ArrayList<MessageModel> messages = new ArrayList<>();
    ChatDetailsAdapter adapter;
    ApiService apiService;
    String fcmToken;

=======
    ArrayList<MessageModel> messages=new ArrayList<>();
    ChatDetailsAdapter adapter;
    ApiService apiService;
    String fcmToken;
>>>>>>> d8f0a10 (backup whole project)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
<<<<<<< HEAD
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbarChatDetails.setNavigationOnClickListener(v -> finish());

        receiverId = getIntent().getStringExtra("buyerId");
        senderId = getIntent().getStringExtra("senderId");
=======
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        receiverId=getIntent().getStringExtra("buyerId");
        senderId=getIntent().getStringExtra("senderId");
>>>>>>> d8f0a10 (backup whole project)

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

<<<<<<< HEAD
        apiService = retrofit.create(ApiService.class);

        getFCMToken(receiverId);
        loadPageHeader();

        senderRoom = senderId + receiverId;
        receiverRoom = receiverId + senderId;

        adapter = new ChatDetailsAdapter(this, messages, senderId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        binding.recyclerMessages.setLayoutManager(layoutManager);
        binding.recyclerMessages.setAdapter(adapter);

        binding.btnSendMessage.setOnClickListener(e -> sendMessage());

        loadChat();
    }

    private void loadChat() {
        database.getReference().child("chats").child(senderRoom).orderByChild("messageTime").limitToLast(100).addValueEventListener(new ValueEventListener() {
=======
        apiService =
                retrofit.create(ApiService.class);

        getFCMToken(receiverId);
        loadPageHeader();
        senderRoom=senderId+receiverId;
        receiverRoom=receiverId+senderId;
        binding.btnSendMessage.setOnClickListener(e->{
            sendMessage();
        });

        loadChat();
        adapter=new ChatDetailsAdapter(this,messages,senderId);
        binding.recyclerMessages.setAdapter(adapter);
        binding.recyclerMessages.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerMessages.scrollToPosition(messages.size()-1);
    }

    private void loadChat() {
        messages.clear();
        database.getReference().child("chats").child(senderRoom).orderByChild("messageTime").limitToLast(50).addValueEventListener(new ValueEventListener() {
>>>>>>> d8f0a10 (backup whole project)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();

                if (!snapshot.exists()) {
                    adapter.notifyDataSetChanged();
<<<<<<< HEAD
                    binding.progressBarMessages.setVisibility(View.GONE);
=======
>>>>>>> d8f0a10 (backup whole project)
                    return;
                }

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    MessageModel model = snapshot1.getValue(MessageModel.class);
<<<<<<< HEAD
=======

>>>>>>> d8f0a10 (backup whole project)
                    if (model != null) {
                        model.setSeen(false);
                        messages.add(model);
                    }
                }

                adapter.notifyDataSetChanged();
<<<<<<< HEAD
                binding.progressBarMessages.setVisibility(View.GONE);

                if (messages.size() > 0) {
                    binding.recyclerMessages.post(() ->
                        binding.recyclerMessages.smoothScrollToPosition(messages.size() - 1)
                    );
=======

                if(messages.size() > 0){
                    binding.recyclerMessages.scrollToPosition(messages.size() - 1);
>>>>>>> d8f0a10 (backup whole project)
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
<<<<<<< HEAD
                binding.progressBarMessages.setVisibility(View.GONE);
            }
        });
=======

            }
        });
        binding.progressBarMessages.setVisibility(View.INVISIBLE);
>>>>>>> d8f0a10 (backup whole project)
    }

    @SuppressLint("NotifyDataSetChanged")
    private void sendMessage() {
<<<<<<< HEAD
        String text = binding.etMessageInput.getText().toString().trim();
        if (text.isEmpty())
            return;

        binding.etMessageInput.setText("");

        DatabaseReference reference = database.getReference().child("chats").child(senderRoom).push();
        MessageModel message = new MessageModel(senderId, receiverId, text, new Date().getTime(), reference.getKey());
        message.setSeen(true);
        reference.setValue(message);

        DatabaseReference reference2 = database.getReference().child("chats").child(receiverRoom).push();
        message.setSeen(false);
        reference2.setValue(message);

        sendNotification(message, fcmToken);
=======
        String text=binding.etMessageInput.getText().toString();
        binding.etMessageInput.setText("");
        if(text.isEmpty())
            return;
        DatabaseReference reference=database.getReference().child("chats").child(senderRoom).push();
        MessageModel message=new MessageModel(senderId,receiverId,text,new Date().getTime(),reference.getKey());
        message.setSeen(true);
        reference.setValue(message);

        DatabaseReference reference2=database.getReference().child("chats").child(receiverRoom).push();
        message.setSeen(false);
        reference2.setValue(message);
        adapter.notifyDataSetChanged();
        sendNotification(message,fcmToken);
>>>>>>> d8f0a10 (backup whole project)
    }

    private void loadPageHeader() {
        database.getReference().child("buyers").child(receiverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
<<<<<<< HEAD
                if (!snapshot.exists()) {
                    binding.tvToolbarStoreName.setText("Name not available");
                    binding.tvToolbarStoreInitial.setText("N");
                    binding.tvToolbarStoreInitial.setVisibility(View.VISIBLE);
                    return;
                }

                BuyerModel model = snapshot.getValue(BuyerModel.class);
                if (model != null) {
                    if (model.getProfilePic() != null && !model.getProfilePic().isEmpty()) {
                        Picasso.get().load(model.getProfilePic()).placeholder(R.drawable.user).into(binding.imgToolbarStoreLogo);
                        binding.tvToolbarStoreInitial.setVisibility(View.GONE);
                    } else {
                        binding.imgToolbarStoreLogo.setImageResource(R.drawable.user);
                    }

                    if (model.getUserName() != null && !model.getUserName().trim().isEmpty()) {
                        binding.tvToolbarStoreName.setText(model.getUserName().trim());
                        binding.tvToolbarStoreInitial.setText(model.getUserName().trim().substring(0, 1).toUpperCase(Locale.ROOT));
                    } else {
                        binding.tvToolbarStoreName.setText("Name not available");
                        binding.tvToolbarStoreInitial.setText("N");
                    }
                    
                    if (model.getProfilePic() == null || model.getProfilePic().isEmpty()) {
                        binding.tvToolbarStoreInitial.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.tvToolbarStoreName.setText("Name not available");
                    binding.tvToolbarStoreInitial.setText("N");
                    binding.tvToolbarStoreInitial.setVisibility(View.VISIBLE);
=======
                BuyerModel model=snapshot.getValue(BuyerModel.class);
                if(model!=null){
                    Picasso.get().load(model.getProfilePic()).placeholder(R.drawable.user).into(binding.imgToolbarStoreLogo);
                    binding.tvToolbarStoreName.setText(model.getUserName());
>>>>>>> d8f0a10 (backup whole project)
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
<<<<<<< HEAD

=======
>>>>>>> d8f0a10 (backup whole project)
    private void sendNotification(MessageModel model, String fcm) {
        if (fcm == null || fcm.isEmpty()) {
            Log.w("ChatActivity", "No FCM token for receiver yet, skipping push notification");
            return;
        }

<<<<<<< HEAD
        Data data = new Data(auth.getCurrentUser() != null ? auth.getCurrentUser().getDisplayName() : "UsmanMart Seller", model.getMessageText(), senderId);
        NotificationSender sender = new NotificationSender(fcm, data);
        String projectId = FirebaseApp.getInstance().getOptions().getProjectId();
=======
        Data data = new Data(auth.getCurrentUser().getDisplayName(), model.getMessageText(),senderId);
        NotificationSender sender = new NotificationSender(fcm, data);
        String projectId = com.google.firebase.FirebaseApp.getInstance().getOptions().getProjectId();
>>>>>>> d8f0a10 (backup whole project)

        FcmAccessTokenManager.getAccessToken(this, new FcmAccessTokenManager.TokenCallback() {
            @Override
            public void onToken(String accessToken) {
                apiService.sendNotification(projectId, "Bearer " + accessToken, sender)
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    Log.d("ChatActivity", "Notification sent: " + response.code());
                                } else {
                                    try {
                                        String error = response.errorBody() != null ? response.errorBody().string() : "unknown";
                                        Log.e("ChatActivity", "FCM rejected request: " + response.code() + " " + error);
                                    } catch (Exception ignored) {}
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                                Log.e("ChatActivity", "Network failure sending notification", t);
                            }
                        });
            }

            @Override
            public void onError(Exception e) {
                Log.e("ChatActivity", "Failed to get FCM access token", e);
                Toast.makeText(ChatActivity.this, "Couldn't get push token: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

<<<<<<< HEAD
    private void getFCMToken(String id) {
        database.getReference().child("buyers").child(id).child("fcmToken").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                    return;
                fcmToken = snapshot.getValue(String.class);
=======
    private void getFCMToken(String id){
        database.getReference().child("buyers").child(id).child("fcmToken").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                    return;
                fcmToken=snapshot.getValue(String.class);
                return;
>>>>>>> d8f0a10 (backup whole project)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
<<<<<<< HEAD
        fcmToken = "";
=======
        fcmToken="";
>>>>>>> d8f0a10 (backup whole project)
    }
}