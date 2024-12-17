package com.example.whatsapp.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.whatsapp.Adapter.StatusRecyclerView;
import com.example.whatsapp.CreateStatus;
import com.example.whatsapp.Models.Status;
import com.example.whatsapp.Models.Users;
import com.example.whatsapp.MyDatabase;
import com.example.whatsapp.databinding.FragmentStatusBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class StatusFragment extends Fragment {
    FragmentStatusBinding binding;
    private static final String SERVER_URL = "http://192.168.56.1/WhatsappClone/view.php";
    ArrayList<Status> statuses;
    FirebaseDatabase database;
    MyDatabase myDatabase;
    StatusRecyclerView adapter;
    ArrayList<Users> list;
    FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        list  = new ArrayList<>();
        binding = FragmentStatusBinding.inflate(getLayoutInflater());
        database = FirebaseDatabase.getInstance();
        myDatabase = new MyDatabase(getContext());
        statuses = new ArrayList<>();
        adapter = new StatusRecyclerView(getContext(),list);
        mAuth = FirebaseAuth.getInstance();
        binding.statusRecyclerView.setAdapter(adapter);
        binding.statusRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        binding.addStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateStatus.class);
                startActivity(intent);
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    list.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    statuses.clear();
                    boolean hidden = false;
                    for (int i =0; i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        Status status = new Status(object.getInt("id"), object.getString("userId"), object.getString("statusMessage"), object.getString("image"), object.getString("time"));
                        statuses.add(status);
                        if (status.getUserId().equals(mAuth.getUid())) {
                            hidden = true;
                            Users user = new Users();
                            user.setUserId(mAuth.getUid());
                            user.setStatus(status);
                            user.setUserName("My status");
                            list.add(user);
                        }
                    }
                    if (hidden){
                        binding.addStatus.setVisibility(View.GONE);
                    }else{
                        binding.addStatus.setVisibility(View.VISIBLE);
                    }
                    updateUsersList(statuses);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                queue.add(stringRequest);
            }
        };
        timer.schedule(timerTask, 0, 5000);
        return binding.getRoot();
    }
    public void updateUsersList(ArrayList<Status> statuses){
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(!myDatabase.findContact(dataSnapshot.getKey())){
                        continue;
                    }
                    for (Status status: statuses){
                        if (status.getUserId().equals(dataSnapshot.getKey())) {
                            Users users = dataSnapshot.getValue(Users.class);
                            users.setProfilePicture(dataSnapshot.child("profileImage").getValue(String.class));
                            users.setUserId(dataSnapshot.getKey());
                            users.setStatus(status);
                            if (users.getUserId().equals(mAuth.getUid())){
                                users.setUserName("My status");
                                list.add(0,users);
                            }
                            else list.add(users);
                        }
                    }
                }
                Log.d("Length", String.valueOf(list.size()));
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}