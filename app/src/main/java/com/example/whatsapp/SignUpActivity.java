package com.example.whatsapp;


import android.content.Intent;
import android.os.Bundle;
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
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_sign_up);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        //set binding  = activitysignup
        setContentView(binding.getRoot());//set content to get layout
        mAuth = FirebaseAuth.getInstance();//firebase authenthication
        database = FirebaseDatabase.getInstance();//get the firebase
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
                    mAuth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString(), binding.txtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Sign Up successful", Toast.LENGTH_SHORT).show();
                                String username = binding.txtUsername.getText().toString();
                                String email = binding.txtEmail.getText().toString();
                                String password = binding.txtPassword.getText().toString();
                                String id =task.getResult().getUser().getUid();
                                Users user = new Users(username,email,password);
                                database.getReference().child("Users").child(id).setValue(user);
                                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                            }
                            else Toast.makeText(SignUpActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Fill up the form", Toast.LENGTH_SHORT).show();
                }
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
