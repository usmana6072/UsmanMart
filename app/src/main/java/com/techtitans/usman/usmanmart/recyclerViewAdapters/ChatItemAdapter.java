package com.techtitans.usman.usmanmart.recyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

<<<<<<< HEAD
=======
import com.google.firebase.auth.FirebaseAuth;
>>>>>>> d8f0a10 (backup whole project)
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.techtitans.usman.usmanmart.ChatActivity;
import com.techtitans.usman.usmanmart.R;
import com.techtitans.usman.usmanmart.models.BuyerModel;
<<<<<<< HEAD
=======
import com.techtitans.usman.usmanmart.models.StoreModel;
>>>>>>> d8f0a10 (backup whole project)
import com.techtitans.usman.usmanmart.models.MessageModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
<<<<<<< HEAD
import java.util.Locale;

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<String> buyers;
    private final String senderId;

    public ChatItemAdapter(Context context, ArrayList<String> buyers, String senderId) {
=======

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemAdapter.ViewHolder> {

    Context context;
    ArrayList<String> buyers;
    String senderId;

    public ChatItemAdapter(Context context, ArrayList<String> buyers) {
        this.context = context;
        this.buyers = buyers;
    }
    public ChatItemAdapter(Context context, ArrayList<String> buyers,String senderId) {
>>>>>>> d8f0a10 (backup whole project)
        this.context = context;
        this.buyers = buyers;
        this.senderId = senderId;
    }

<<<<<<< HEAD
=======

>>>>>>> d8f0a10 (backup whole project)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_conversation, parent, false);
        return new ViewHolder(view);
    }

<<<<<<< HEAD
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String receiver = buyers.get(position);

        FirebaseDatabase.getInstance().getReference()
                .child("buyers")
                .child(receiver)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            holder.buyerName.setText("Name not available");
                            holder.profile.setImageResource(R.drawable.user);
                            holder.tvStoreInitial.setText("N");
                            holder.tvStoreInitial.setVisibility(View.VISIBLE);
                            return;
                        }

                        BuyerModel buyer = snapshot.getValue(BuyerModel.class);
                        if (buyer == null) {
                            holder.buyerName.setText("Name not available");
                            holder.profile.setImageResource(R.drawable.user);
                            holder.tvStoreInitial.setText("N");
                            holder.tvStoreInitial.setVisibility(View.VISIBLE);
                            return;
                        }

                        if (buyer.getUserName() != null && !buyer.getUserName().trim().isEmpty()) {
                            holder.buyerName.setText(buyer.getUserName().trim());
                        } else {
                            holder.buyerName.setText("Name not available");
                        }

                        if (buyer.getProfilePic() != null && !buyer.getProfilePic().isEmpty()) {
                            Picasso.get().load(buyer.getProfilePic())
                                    .placeholder(R.drawable.user)
                                    .into(holder.profile);
                            holder.tvStoreInitial.setVisibility(View.GONE);
                        } else {
                            holder.profile.setImageResource(R.drawable.user);
                            String name = (buyer.getUserName() != null && !buyer.getUserName().trim().isEmpty()) ? buyer.getUserName().trim() : "N";
                            holder.tvStoreInitial.setText(name.substring(0, 1).toUpperCase(Locale.ROOT));
                            holder.tvStoreInitial.setVisibility(View.VISIBLE);
                        }

                        // Fetch last message for this conversation
                        String chat = senderId + receiver;
                        FirebaseDatabase.getInstance().getReference()
                                .child("chats")
                                .child(chat)
                                .orderByChild("messageTime")
                                .limitToLast(1)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot chatSnapshot) {
                                        if (!chatSnapshot.exists()) {
                                            holder.lastMessage.setText("No messages yet");
                                            holder.lastMessageTime.setVisibility(View.GONE);
                                            holder.tvUnreadBadge.setVisibility(View.GONE);
                                            return;
                                        }

                                        for (DataSnapshot childSnapshot : chatSnapshot.getChildren()) {
                                            MessageModel messageModel = childSnapshot.getValue(MessageModel.class);
                                            if (messageModel != null) {
                                                holder.lastMessage.setText(messageModel.getMessageText());

                                                if (messageModel.getMessageTime() != 0) {
                                                    SimpleDateFormat format = new SimpleDateFormat("h:mm a", Locale.getDefault());
                                                    holder.lastMessageTime.setText(format.format(new Date(messageModel.getMessageTime())));
                                                    holder.lastMessageTime.setVisibility(View.VISIBLE);
                                                } else {
                                                    holder.lastMessageTime.setVisibility(View.GONE);
                                                }

                                                holder.tvUnreadBadge.setVisibility(View.GONE);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
=======
    BuyerModel buyer;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String receiver=buyers.get(position);
        FirebaseDatabase.getInstance().getReference().child("buyers").child(receiver).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                    return;
                buyer=snapshot.getValue(BuyerModel.class);
                holder.buyerName.setText(buyer.getUserName());
                Picasso.get().load(buyer.getProfilePic()).placeholder(R.drawable.user).into(holder.profile);

                String chat=senderId+receiver;
                FirebaseDatabase.getInstance().getReference().child("chats").child(chat).orderByChild("messageTime").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists())
                            return;
                        MessageModel messageModel=snapshot.getValue(MessageModel.class);
                        holder.lastMessage.setText(messageModel.getMessageText());
                        SimpleDateFormat format=new SimpleDateFormat("hh:mm");
                        holder.lastMessageTime.setText(format.format(new Date(messageModel.getMessageTime())));
                        if(messageModel.isSeen())
                            holder.tvUnreadBadge.setVisibility(View.INVISIBLE);
                        else
                            holder.tvUnreadBadge.setVisibility(View.VISIBLE);
>>>>>>> d8f0a10 (backup whole project)
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
<<<<<<< HEAD

        holder.itemView.setOnClickListener(e -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("buyerId", receiver);
            intent.putExtra("senderId", senderId);
=======
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
        holder.itemView.setOnClickListener(e->{
            Intent intent=new Intent(context, ChatActivity.class);
            intent.putExtra("buyerId",receiver);
            intent.putExtra("senderId",senderId);
>>>>>>> d8f0a10 (backup whole project)
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return buyers.size();
    }

<<<<<<< HEAD
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView tvStoreInitial;
        TextView buyerName;
        TextView lastMessage;
        TextView lastMessageTime;
        TextView tvUnreadBadge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.imgStoreLogo);
            tvStoreInitial = itemView.findViewById(R.id.tvStoreInitial);
            buyerName = itemView.findViewById(R.id.tvStoreName);
            lastMessage = itemView.findViewById(R.id.tvLastMessage);
            lastMessageTime = itemView.findViewById(R.id.tvLastMessageTime);
            tvUnreadBadge = itemView.findViewById(R.id.tvUnreadBadge);
        }
    }
}
=======
    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        ImageView profile=itemView.findViewById(R.id.imgStoreLogo);
        TextView buyerName=itemView.findViewById(R.id.tvStoreName);
        TextView lastMessage=itemView.findViewById(R.id.tvLastMessage);
        TextView lastMessageTime=itemView.findViewById(R.id.tvLastMessageTime);
        TextView tvUnreadBadge=itemView.findViewById(R.id.tvUnreadBadge);
    }
}
>>>>>>> d8f0a10 (backup whole project)
