package com.example.whatsapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.whatsapp.Models.Users;
import com.example.whatsapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity{

    ActivitySignUpBinding binding;
    MyDatabase myDatabase;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        //set binding  = activitysignup
        setContentView(binding.getRoot());//set content to get layout
        myDatabase = new MyDatabase(SignUpActivity.this);
        progressBar =  new ProgressBar(SignUpActivity.this);
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.txtUsername.getText().toString().isEmpty()&&
                        !binding.txtEmail.getText().toString().isEmpty()&&
                        !binding.txtPassword.getText().toString().isEmpty()){
                    if(binding.txtPassword.getText().toString().length()<8){
                        Toast.makeText(SignUpActivity.this,"Password must be at least 8 Characters",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    progressBar.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Fill up the form", Toast.LENGTH_SHORT).show();
                    return;
                }
                String username = binding.txtUsername.getText().toString();
                String password = binding.txtPassword.getText().toString();
                String email = binding.txtEmail.getText().toString();
                if(myDatabase.userExists(email)){
                    Toast.makeText(SignUpActivity.this, "Account already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                Users user = new Users(username, email, password);
                myDatabase.createUser(user);
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
        TextView textView = findViewById(R.id.Login);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
        //getSupportActionBar().hide();
    }
}
