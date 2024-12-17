package com.example.whatsapp;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.whatsapp.databinding.ActivityOpenStatusBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class OpenStatus extends AppCompatActivity {

    ActivityOpenStatusBinding binding;
    FirebaseAuth mAuth;
    int statusID;
    String SERVER_URL = "http://192.168.56.1/WhatsappClone/update.php";
    String SERVER_URL2 = "http://192.168.56.1/WhatsappClone/delete.php";
    RequestQueue queue;
    int REQUEST_IMAGE_CAPTURE = 10;
    int REQUEST_PERMISSION_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOpenStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        Intent intent = getIntent();
        String userID = intent.getStringExtra("userId");
        String userName = intent.getStringExtra("userName");
        String profilePic = intent.getStringExtra("profilePicture");
        statusID = intent.getIntExtra("statusID", 0);
        Log.d("Status ID", String.valueOf(statusID));
        String statusMessage = intent.getStringExtra("statusMessage");
        String statusImage = intent.getStringExtra("statusImage");
        String statusTime = intent.getStringExtra("statusTime");
        queue = Volley.newRequestQueue(OpenStatus.this);
        mAuth = FirebaseAuth.getInstance();
        binding.name.setText(userName);
        if (profilePic != null) {
            binding.profile.setImageBitmap(convertToBitmap(profilePic));
        }
        else{
            binding.profile.setImageResource(R.drawable.user);
        }
        binding.statusMessage.setText(statusMessage);
        binding.date.setText(statusTime);
        byte[] decodedString = android.util.Base64.decode(statusImage, android.util.Base64.DEFAULT);
        binding.statusImage.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (userID.equals(mAuth.getUid())){
            Log.d("User ID", userID);
            binding.statusMenu.setVisibility(View.VISIBLE);
        }
        binding.statusMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(OpenStatus.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.status_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        CharSequence title = item.getTitle();
                        if (title.equals("Replace Image")) {
                            openCamera();
                            return true;
                        }
                        else if (title.equals("Edit Status Message")) {
                            binding.statusMessage.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.input_message, null));
                            binding.statusMessage.setEnabled(true);
                            binding.statusMessage.setTextColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
                            binding.statusMessage.requestFocus();
                            binding.confirmEdit.setVisibility(View.VISIBLE);
                            return true;
                        }
                        else if(title.equals("Delete Status")) {
                            deleteStatus(String.valueOf(statusID));
                            return true;
                        }
                        return false;
                    };
                });
            }
        });
        binding.confirmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(OpenStatus.this, response, Toast.LENGTH_SHORT).show();
                        binding.statusMessage.setBackground(new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT));
                        binding.statusMessage.setEnabled(false);
                        binding.statusMessage.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                        binding.confirmEdit.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OpenStatus.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String , String>();
                        String message = binding.statusMessage.getText().toString();
                        Log.d("Params", "statusMessage: " + message + ", id: " + statusID);
                        params.put("statusMessage", message);
                        params.put("id", String.valueOf(statusID));
                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }
    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        }
    }
    private void openCamera() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions();
            return;
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Permissions required to take picture", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_IMAGE_CAPTURE&&resultCode== AppCompatActivity.RESULT_OK){
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                binding.statusImage.setImageBitmap(imageBitmap);
                String base64Image = convertToString(imageBitmap);
                StringRequest stringRequest =  new StringRequest(Request.Method.POST,SERVER_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(OpenStatus.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OpenStatus.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String , String>();
                        params.put("img", base64Image);
                        params.put("id", String.valueOf(statusID));
                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        }else {
            Toast.makeText(OpenStatus.this, "Error occurred", Toast.LENGTH_SHORT).show();
        }
    }
    private String convertToString(Bitmap img){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 100,byteArrayOutputStream);
        byte [] imageByte = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return encoded;
    }
    private void deleteStatus(String statusID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(OpenStatus.this, response, Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OpenStatus.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String , String>();
                params.put("id", statusID);
                return params;
            }
        };
        queue.add(stringRequest);
    }
    private Bitmap convertToBitmap(String encoded) {
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}