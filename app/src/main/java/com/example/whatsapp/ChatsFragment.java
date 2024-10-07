package com.example.whatsapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {
    private class AdapterForRecyclerView extends RecyclerView.Adapter<RecyclerViewHolder> {
        Context context;
        List<Item> items;

        public AdapterForRecyclerView(Context context, List<Item> items) {
            this.context = context;
            this.items = items;
        }

        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_cover,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            holder.name.setText(items.get(position).name);
            holder.last_message.setText(items.get(position).lastMessage);
            holder.imageView.setImageResource(items.get(position).image);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
    private class Item {
        String name;
        String lastMessage;
        int image;

        public Item(String name, String lastMessage, int image) {
            this.name = name;
            this.lastMessage = lastMessage;
            this.image = image;
        }
    }
    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        de.hdodenhof.circleimageview.CircleImageView imageView;
        TextView name,last_message;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.user_name);
            last_message = itemView.findViewById(R.id.last_message);
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Item> items = new ArrayList<Item>();
        items.add(new Item("Jeff","Last message",R.drawable.user));
        items.add(new Item("Test","Last message",R.drawable.user));
        items.add(new Item("Test2","Last message",R.drawable.user));
        items.add(new Item("Test3","Last message",R.drawable.user));
        items.add(new Item("Jeff","Last message",R.drawable.user));
        items.add(new Item("Test","Last message",R.drawable.user));
        items.add(new Item("Test2","Last message",R.drawable.user));
        items.add(new Item("Test3","Last message",R.drawable.user));
        recyclerView.setAdapter(new AdapterForRecyclerView(getContext(),items));
        return view;
    }
}