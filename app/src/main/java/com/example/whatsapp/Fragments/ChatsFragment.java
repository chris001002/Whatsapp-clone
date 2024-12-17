package com.example.whatsapp.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.whatsapp.Adapter.UsersRecyclerView;
import com.example.whatsapp.Models.Message;
import com.example.whatsapp.Models.Users;
import com.example.whatsapp.MyDatabase;
import com.example.whatsapp.databinding.FragmentChatsBinding;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    FragmentChatsBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    ArrayList<Users> fullList;
    FirebaseDatabase database;
    MyDatabase myDatabase;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    UsersRecyclerView adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChatsBinding.inflate(inflater, container, false);
        myDatabase = new MyDatabase(getContext());
        database = FirebaseDatabase.getInstance();
        adapter= new UsersRecyclerView(getContext(),list);
        binding.recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        binding.recyclerView.setLayoutManager(layoutManager);
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(myDatabase.findContact(dataSnapshot.getKey())){
                        Users users = dataSnapshot.getValue(Users.class);
                        users.setUserId(dataSnapshot.getKey());
                        users.setProfilePicture(dataSnapshot.child("profileImage").getValue(String.class));
                        list.add(users);
                    }
                }
                setLastMessages();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        binding.chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                int id = checkedIds.get(0);
                if (id == binding.chipAll.getId()){
                    list.clear();
                    list.addAll(fullList);
                    adapter.notifyDataSetChanged();
                }
                else if (id == binding.chipFavorites.getId()){
                        fullList = (ArrayList<Users>) list.clone();
                        ArrayList<String> favorites = myDatabase.getFavoriteContacts();
                        int i = 0;
                        while (i < list.size()) {
                            if (myDatabase.isFavorite(list.get(i).getUserId()) == 1) i++;
                            else list.remove(i);
                        }
                        adapter.notifyDataSetChanged();

                }
            }
        });


        return  binding.getRoot();
    }
    private void setLastMessages(){
        for (Users user : list){
            database.getReference().child("chats").child(mAuth.getUid() + user.getUserId()).limitToLast(1).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()){
                        Message message = data.getValue(Message.class);
                        user.setLastMessage(message.getMessage());
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}