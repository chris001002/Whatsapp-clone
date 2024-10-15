package com.example.whatsapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.whatsapp.Adapter.Chat;
import com.example.whatsapp.services.sentSoundEffect;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsapp.Models.Message;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.databinding.ActivityOpenChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class OpenChat extends AppCompatActivity {
    ActivityOpenChatBinding binding;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityOpenChatBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final String senderId = mAuth.getUid();
        Intent intent = getIntent();
        String receiverId = intent.getStringExtra("userId");
        String userName = intent.getStringExtra("userName");
        String profilePic = intent.getStringExtra("profilePicture");
        binding.userName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.user).into(binding.chatProfile);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpenChat.this, MainActivity.class);
                startActivity(intent);
            }
        });
        final ArrayList<Message> messages = new ArrayList<>();
        final Chat adapter = new Chat(messages, this, receiverId);
        binding.chatRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        binding.chatRecyclerView.setLayoutManager(linearLayoutManager);
        final String senderRoom = senderId + receiverId;
        final String receiverRoom = receiverId + senderId;

        database.getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Message message = dataSnapshot.getValue(Message.class);
                    message.setMessageid(dataSnapshot.getKey());
                    messages.add(message);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Throwable err = new Error(String.valueOf(error));
                System.err.println(err);
            }
        });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newMessage = binding.newMessage.getText().toString();
                final  Message message = new Message(senderId, newMessage);
                message.setTimestamp(new Date().getTime());
                binding.newMessage.setText("");
                database.getReference().child("chats").child(senderRoom).push().setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats").child(receiverRoom).push().setValue(message);
                    }
                }
                );
                Intent playSound = new Intent(OpenChat.this, sentSoundEffect.class);
                startService(playSound);
            }
        });
    }
}