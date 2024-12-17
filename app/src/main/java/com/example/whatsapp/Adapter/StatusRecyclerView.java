package com.example.whatsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.Models.Status;
import com.example.whatsapp.Models.Users;
import com.example.whatsapp.MyDatabase;
import com.example.whatsapp.OpenStatus;
import com.example.whatsapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StatusRecyclerView extends RecyclerView.Adapter<StatusRecyclerView.RecyclerViewHolder> {
    Context context;
    List<Users> usersList;
    public StatusRecyclerView(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.status_cover, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Users users = usersList.get(position);
        holder.name.setText(users.getUserName());
        String img = users.getProfilePicture();
        if (img == null){
            holder.imageView.setImageResource(R.drawable.user);
        }
        else {
            holder.imageView.setImageBitmap(convertToBitmap(img));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OpenStatus.class);
                intent.putExtra("userId", users.getUserId());
                intent.putExtra("userName", users.getUserName());
                intent.putExtra("profilePicture", users.getProfilePicture());
                intent.putExtra("statusID", users.getStatus().getStatusID());
                intent.putExtra("statusMessage", users.getStatus().getMessage());
                intent.putExtra("statusImage", users.getStatus().getImage());
                intent.putExtra("statusTime", users.getStatus().getTimestamp());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        de.hdodenhof.circleimageview.CircleImageView imageView;
        TextView name;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.status_profile);
            name = itemView.findViewById(R.id.user_name_status);
        }
    }
    private Bitmap convertToBitmap(String encoded) {
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
