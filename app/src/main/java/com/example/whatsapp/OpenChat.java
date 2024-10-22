package com.example.whatsapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.whatsapp.Adapter.Chat;
import com.example.whatsapp.services.sentSoundEffect;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsapp.Models.Message;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.databinding.ActivityOpenChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class OpenChat extends AppCompatActivity {
    ActivityOpenChatBinding binding;
    MyDatabase myDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDatabase = new MyDatabase(OpenChat.this);
        binding = ActivityOpenChatBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        int receiverId = intent.getIntExtra("userId", -1);
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
        final ArrayList<Message> messages = myDatabase.getMessages(Preferences.getUserId(OpenChat.this),receiverId);
        final Chat adapter = new Chat(messages, this, receiverId);
        binding.chatRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        binding.chatRecyclerView.setLayoutManager(linearLayoutManager);

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = binding.newMessage.getText().toString();
                Message message = new Message(Preferences.getUserId(OpenChat.this), msg);
                myDatabase.createMessage(receiverId,message);
                Intent playSound = new Intent(OpenChat.this, sentSoundEffect.class);
                startService(playSound);
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}