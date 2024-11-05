package com.example.whatsapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.whatsapp.Adapter.UsersRecyclerView;
import com.example.whatsapp.Models.Message;
import com.example.whatsapp.Models.Users;
import com.example.whatsapp.MyDatabase;
import com.example.whatsapp.Preferences;
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
    MyDatabase myDatabase;
    UsersRecyclerView adapter;
    ArrayList<Users> fullList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChatsBinding.inflate(inflater, container, false);
        myDatabase = new MyDatabase(getContext());
        adapter= new UsersRecyclerView(getContext(),list);
        binding.recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        binding.recyclerView.setLayoutManager(layoutManager);
        list.addAll(myDatabase.getAllContacts(Preferences.getUserId(getContext())));
        adapter.notifyDataSetChanged();
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
                        ArrayList<Users> favoriteList = new ArrayList<>();
                        ArrayList<Integer> favorites = myDatabase.getFavoriteContacts(Preferences.getUserId(getContext()));
                        for (int i : favorites){
                            for(Users user: list){
                                if (user.getUserId()==i){
                                    favoriteList.add(user);
                                }
                            }
                        }
                        fullList = (ArrayList<Users>) list.clone();
                        list.clear();
                        list.addAll(favoriteList);
                        adapter.notifyDataSetChanged();
                }
            }
        });


        return binding.getRoot();
    }
}