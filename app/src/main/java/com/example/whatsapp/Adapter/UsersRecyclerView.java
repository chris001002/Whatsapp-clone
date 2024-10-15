package com.example.whatsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.whatsapp.OpenChat;
import com.example.whatsapp.Models.Users;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersRecyclerView extends RecyclerView.Adapter<UsersRecyclerView.RecyclerViewHolder> {
        Context context;
        List<Users> usersList;
        public UsersRecyclerView(Context context, List<Users> users) {
            this.context = context;
            this.usersList = users;
        }

        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_cover,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            Users users = usersList.get(position);;
            holder.name.setText(users.getUserName());
            holder.last_message.setText(users.getLastMessage());
            Picasso.get().load(users.getProfilePicture()).placeholder(R.drawable.user).into(holder.imageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OpenChat.class);
                    intent.putExtra("userId", users.getUserId());
                    intent.putExtra("profilePicture",users.getProfilePicture());
                    intent.putExtra("userName", users.getUserName());
                    context.startActivity(intent);
                }
            });
        }
        @Override
        public int getItemCount() {
            return usersList.size();
        }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        de.hdodenhof.circleimageview.CircleImageView imageView;
        TextView name,last_message;
        public RecyclerViewHolder(@NonNull View UserView) {
            super(UserView);
            imageView = UserView.findViewById(R.id.profile);
            name = UserView.findViewById(R.id.user_name);
            last_message = UserView.findViewById(R.id.last_message);
        }
    }
}
