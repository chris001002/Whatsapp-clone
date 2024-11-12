package com.example.whatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsapp.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {
    MyDatabase myDatabase;
    ActivitySignInBinding binding;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDatabase = new MyDatabase(SignInActivity.this);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressBar = new ProgressBar(SignInActivity.this);
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.txtEmail.getText().toString();
                String password = binding.txtPassword.getText().toString();
                if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(SignInActivity.this,"Please enter your email and password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.isEmpty()){
                    Toast.makeText(SignInActivity.this,"Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()){
                    Toast.makeText(SignInActivity.this,"Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                int loginResult =myDatabase.login(email, password);
                if (loginResult == -1){
                    Toast.makeText(SignInActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                    return;
                }
                Preferences.setUserId(SignInActivity.this, loginResult);
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        TextView textView = findViewById(R.id.txtSignUpPhone);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        if(Preferences.getUserId(SignInActivity.this)!=-1)startActivity(new Intent(SignInActivity.this, MainActivity.class));
    }
}