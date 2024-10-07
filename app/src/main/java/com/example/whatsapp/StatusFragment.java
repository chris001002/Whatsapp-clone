package com.example.whatsapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_status, container, false);
        CircleImageView imageView= view.findViewById(R.id.status_profile);
        imageView.setBorderColor(Color.GREEN);
        imageView.setBorderWidth(20);
        return view;
    }
}