package com.example.whatsapp.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whatsapp.Fragments.ChatsFragment;
import com.example.whatsapp.MainActivity;
import com.example.whatsapp.MyDatabase;
import com.example.whatsapp.OpenChat;
import com.example.whatsapp.Models.Users;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.Preferences;
import com.example.whatsapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersRecyclerView extends RecyclerView.Adapter<UsersRecyclerView.RecyclerViewHolder> {
        Context context;
        MyDatabase database;
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
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
            database = new MyDatabase(context);
            Users users = usersList.get(position);;
            holder.name.setText(users.getUserName());
            holder.last_message.setText(users.getLastMessage());
            if (users.getIsFavorite()==0)holder.is_favorite.setVisibility(View.GONE);
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
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(context).setTitle("Favorite").setMessage("Do you want to add/remove this contact to your favorites?")
                            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.d(String.valueOf(Preferences.getUserId(context)), String.valueOf(users.getUserId()));
                                    database.setFavorite(Preferences.getUserId(context), users.getUserId(), true);
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }
                            }
                            ).setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    database.setFavorite(Preferences.getUserId(context), users.getUserId(), false);
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }
                            }).show();
                    return true;
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
        ImageView is_favorite;
        public RecyclerViewHolder(@NonNull View UserView) {
            super(UserView);
            imageView = UserView.findViewById(R.id.profile);
            name = UserView.findViewById(R.id.user_name);
            last_message = UserView.findViewById(R.id.last_message);
            is_favorite = UserView.findViewById(R.id.favorite);
        }
    }
}
