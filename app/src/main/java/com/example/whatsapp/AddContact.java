package com.example.whatsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.whatsapp.Models.Users;
import com.example.whatsapp.databinding.ActivityAddContactBinding;


public class AddContact extends AppCompatActivity {
    ActivityAddContactBinding binding;
    MyDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = AddContact.this;
        database = new MyDatabase(context);
        super.onCreate(savedInstanceState);
        MyDatabase myDatabase =  new MyDatabase(AddContact.this);
        binding = ActivityAddContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.email.getText().toString();
                if (email.isEmpty()){
                    Toast.makeText(context, "Please fill the email", Toast.LENGTH_SHORT).show();
                    return;
                }
                Users user = database.getUser(email);
                if(user.getUserId()==-1){
                    Toast.makeText(context, "Account not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(user.getUserId()==Preferences.getUserId(AddContact.this)){
                    Toast.makeText(context, "You cannot add yourself", Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.userName.setText(user.getUserName());
                binding.addContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (database.findContact(Preferences.getUserId(context), user.getUserId())){
                            Toast.makeText(context, "User already in contacts", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        database.addContact(Preferences.getUserId(context), user.getUserId());
                    }
                });
                binding.contact.setVisibility(View.VISIBLE);
            }
        });
    }
}