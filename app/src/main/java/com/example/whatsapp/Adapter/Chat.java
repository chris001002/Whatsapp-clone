package com.example.whatsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.OpenChat;
import com.example.whatsapp.Models.Message;
import com.example.whatsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Chat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @NonNull
    ArrayList<Message> messages;
    Context context;
    String receiverid;

    public Chat(@NonNull ArrayList<Message> messages, Context context, String receiverid) {
        this.messages = messages;
        this.context = context;
        this.receiverid = receiverid;
    }

    int SENDER = 1;
    int RECEIVER = 2;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_send, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_receive, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder.getClass() == SenderViewHolder.class) {
            ((SenderViewHolder) holder).senderMessage.setText(message.getMessage());
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String formattedDate = dateFormat.format(new Date(message.getTimestamp()));
            ((SenderViewHolder) holder).senderTime.setText(formattedDate);
            int finalPosition = position; // Capture the position as a final variable
            ((SenderViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPopupMenu(v, message, finalPosition);
                    return true;
                }
            });
        } else {
            ((ReceiverViewHolder) holder).receiverMessage.setText(message.getMessage());
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String formattedDate = dateFormat.format(new Date(message.getTimestamp()));
            ((ReceiverViewHolder) holder).receiverTime.setText(formattedDate);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getUserId().equals(FirebaseAuth.getInstance().getUid())) return SENDER;
        else return RECEIVER;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMessage, receiverTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMessage = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.receiver_time);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMessage, senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.sender_time);
        }
    }

    private void showPopupMenu(View view, Message message, int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.message_options);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.edit) {
                    // Handle edit
                    ((OpenChat) context).editMessage(message);
                    return true;
                } else if (item.getItemId() == R.id.delete) {
                    // Handle delete
                    deleteMessage(message, position);
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void deleteMessage(Message message, int position) {
        FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(FirebaseAuth.getInstance().getUid() + receiverid)
                .child(message.getMessageid())
                .removeValue();

        FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(receiverid + FirebaseAuth.getInstance().getUid())
                .child(message.getMessageid())
                .removeValue();

        messages.remove(position);
        notifyItemRemoved(position);
    }
}
