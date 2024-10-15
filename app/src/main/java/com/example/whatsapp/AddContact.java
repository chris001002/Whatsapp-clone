package com.example.whatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.whatsapp.Models.Users;
import com.example.whatsapp.databinding.ActivityAddContactBinding;
import com.example.whatsapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class AddContact extends AppCompatActivity {
    ActivityAddContactBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyDatabase myDatabase =  new MyDatabase(AddContact.this);
        binding = ActivityAddContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddContact.this, MainActivity.class);
                startActivity(intent);
            }
        });
        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                String email = binding.email.getText().toString();
                Users user;
                firebaseDatabase.getReference().child("Users").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        Log.d("Firebase attempt", "Success");
                        boolean found = false;
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            if (data.child("email").getValue(String.class).equals(email)){
                                Users user =data.getValue(Users.class);
                                user.setUserId(data.getKey());
                                binding.userName.setText(user.getUserName());
                                binding.addContact.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        myDatabase.addUser(user.getUserId());
                                        Log.d("data: ",myDatabase.getContacts().toString());
                                    }
                                });
                                binding.contact.setVisibility(View.VISIBLE);
                            }
                        }
                        if (!found) Toast.makeText(AddContact.this, "Email is not found in database", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
    }
}