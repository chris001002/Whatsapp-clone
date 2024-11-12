package com.example.whatsapp.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsapp.CreateStatus;
import com.example.whatsapp.R;
import com.example.whatsapp.databinding.FragmentStatusBinding;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusFragment extends Fragment {
    FragmentStatusBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatusBinding.inflate(getLayoutInflater());
        binding.addStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateStatus.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }
}