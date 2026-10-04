package com.techtitans.usman.usmanmart.fragments;

import android.os.Bundle;
<<<<<<< HEAD
=======

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

>>>>>>> d8f0a10 (backup whole project)
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

<<<<<<< HEAD
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

=======
>>>>>>> d8f0a10 (backup whole project)
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
<<<<<<< HEAD
import com.techtitans.usman.usmanmart.databinding.FragmentMessageBinding;
import com.techtitans.usman.usmanmart.recyclerViewAdapters.ChatItemAdapter;
=======
import com.techtitans.usman.usmanmart.recyclerViewAdapters.ChatItemAdapter;
import     com.techtitans.usman.usmanmart.recyclerViewAdapters.ChatItemAdapter;
import com.techtitans.usman.usmanmart.R;
import com.techtitans.usman.usmanmart.databinding.FragmentMessageBinding;
>>>>>>> d8f0a10 (backup whole project)

import java.util.ArrayList;

public class MessageFragment extends Fragment {

    public MessageFragment() {
        // Required empty public constructor
    }
<<<<<<< HEAD

    private FragmentMessageBinding binding;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private String senderId;
    private final ArrayList<String> chatStores = new ArrayList<>();
    private ChatItemAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);

        if (auth.getUid() != null) {
            getSenderId();
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.emptyStateContainer.setVisibility(View.VISIBLE);
        }

        binding.swipeRefresh.setOnRefreshListener(this::loadChatUsers);

        return binding.getRoot();
    }

    private void getSenderId() {
        String sellerId = auth.getUid();
        if (sellerId == null) return;

        database.getReference().child("sellers").child(sellerId).child("storeId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    senderId = snapshot.getValue(String.class);
                    adapter = new ChatItemAdapter(getContext(), chatStores, senderId);
                    binding.recyclerConversations.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerConversations.setAdapter(adapter);
                    loadChatUsers();
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.emptyStateContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void loadChatUsers() {
        if (senderId == null) {
            binding.swipeRefresh.setRefreshing(false);
            return;
        }

        DatabaseReference reference = database.getReference().child("buyers");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatStores.clear();
                if (!snapshot.exists()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.emptyStateContainer.setVisibility(View.VISIBLE);
                    binding.swipeRefresh.setRefreshing(false);
                    return;
                }

                long totalBuyers = snapshot.getChildrenCount();
                if (totalBuyers == 0) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.emptyStateContainer.setVisibility(View.VISIBLE);
                    binding.swipeRefresh.setRefreshing(false);
                    return;
                }

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String receiverId = snapshot1.getKey();
                    String chatId = senderId + receiverId;

                    database.getReference().child("chats").child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot3) {
                            if (snapshot3.exists() && snapshot3.hasChildren()) {
                                if (!chatStores.contains(receiverId)) {
                                    chatStores.add(receiverId);
                                }
                            }
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }

                            binding.progressBar.setVisibility(View.GONE);
                            binding.swipeRefresh.setRefreshing(false);

                            if (chatStores.isEmpty()) {
                                binding.emptyStateContainer.setVisibility(View.VISIBLE);
                            } else {
                                binding.emptyStateContainer.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.swipeRefresh.setRefreshing(false);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);
            }
        });
=======
    FragmentMessageBinding binding;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String sellerId=auth.getUid();
    String senderId;
    ArrayList<String> chatStores=new ArrayList<>();
    ChatItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentMessageBinding.inflate(inflater,container, false);
        getSenderId();
        return binding.getRoot();
>>>>>>> d8f0a10 (backup whole project)
    }

    private void getSenderId() {
        database.getReference().child("sellers").child(sellerId).child("storeId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    senderId=snapshot.getValue(String.class);
                    loadChatUsers();
                    adapter=new ChatItemAdapter(getContext(),chatStores,senderId);
                    binding.recyclerConversations.setAdapter(adapter);
                    binding.recyclerConversations.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadChatUsers(){
        DatabaseReference reference=database.getReference().child("buyers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatStores.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    String receiverId=snapshot1.getKey();
                    String chatId=senderId+receiverId;
                    database.getReference().child("chats").child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot3) {
                            if(snapshot3.exists()) {
                                chatStores.add(receiverId);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                binding.progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}