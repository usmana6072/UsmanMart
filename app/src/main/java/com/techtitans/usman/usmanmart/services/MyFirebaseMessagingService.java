package com.techtitans.usman.usmanmart.services;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.techtitans.usman.usmanmart.ChatActivity;
import com.techtitans.usman.usmanmart.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "chat_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

<<<<<<< HEAD
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String title = "New Message";
        String body = "";
        String senderId = "";
        String storeId = "";
=======
    String  title = "New Message";
    String body = "";
    String senderId="";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
>>>>>>> d8f0a10 (backup whole project)

        // Handle DATA messages
        if (remoteMessage.getData() != null && !remoteMessage.getData().isEmpty()) {
            title = remoteMessage.getData().getOrDefault("title", title);
            body = remoteMessage.getData().getOrDefault("body", "");
<<<<<<< HEAD
            senderId = remoteMessage.getData().getOrDefault("buyerId", "");
            storeId = remoteMessage.getData().getOrDefault("storeId", "");
=======
            senderId=remoteMessage.getData().getOrDefault("senderId","");
>>>>>>> d8f0a10 (backup whole project)
        }

        // Handle NOTIFICATION messages (optional fallback)
        if (remoteMessage.getNotification() != null) {
            if (remoteMessage.getNotification().getTitle() != null) {
                title = remoteMessage.getNotification().getTitle();
            }
            if (remoteMessage.getNotification().getBody() != null) {
                body = remoteMessage.getNotification().getBody();
            }
        }

<<<<<<< HEAD
        final String finalTitle = title;
        final String finalBody = body;
        final String finalSenderId = senderId;

        // If storeId is provided in the data payload, display notification immediately
        if (storeId != null && !storeId.isEmpty()) {
            showNotification(finalTitle, finalBody, finalSenderId, storeId);
            return;
        }

        // Otherwise, fall back to checking the database (only if the user is authenticated)
        String currentUid = FirebaseAuth.getInstance().getUid();
        if (currentUid != null) {
            FirebaseDatabase.getInstance().getReference().child("sellers").child(currentUid).child("storeId").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String dbStoreId = snapshot.exists() ? snapshot.getValue(String.class) : "";
                    showNotification(finalTitle, finalBody, finalSenderId, dbStoreId);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    showNotification(finalTitle, finalBody, finalSenderId, "");
                }
            });
        } else {
            // If completely unauthenticated or background process lacks auth state, display it anyway with an empty storeId
            showNotification(finalTitle, finalBody, finalSenderId, "");
        }
=======
        FirebaseDatabase.getInstance().getReference().child("sellers").child(FirebaseAuth.getInstance().getUid()).child("storeId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                    return;
                showNotification(title,body,senderId,snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
>>>>>>> d8f0a10 (backup whole project)
    }

    private void showNotification(String title, String body, String senderId,String storeId) {

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("senderId",storeId );
        intent.putExtra("buyerId",senderId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (senderId != null) {
            intent.putExtra("storeId", senderId);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                (int) System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        manager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Chat Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription("Notifications for chat messages");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            FirebaseDatabase.getInstance().getReference("sellers")
                    .child(auth.getUid())
                    .child("fcmToken")
                    .setValue(token);
        }
    }
}