package com.example.whatsapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.Models.Message;
import com.example.whatsapp.Preferences;
import com.example.whatsapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Predicate;

public class Chat extends RecyclerView.Adapter{
    @NonNull
    ArrayList<Message>messages;
    Context context;
    int receiverid;

    public Chat(@NonNull ArrayList<Message> messages, Context context, int receiverid) {
        this.messages = messages;
        this.context = context;
        this.receiverid = receiverid;
    }

    int SENDER = 1;
    int RECEIVER = 2;
    public Chat(@NonNull ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==SENDER){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_send,parent,false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_receive,parent,false);
            return  new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if(holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder) holder).senderMessage.setText(message.getMessage());
            Log.d("date", message.getTimestamp());
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(message.getTimestamp(), inputFormatter);
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm");
            String formattedDate = dateFormat.format(dateTime);
            ((SenderViewHolder) holder).senderTime.setText(formattedDate);
        }
        else{
            ((ReceiverViewHolder)holder).receiverMessage.setText(message.getMessage());
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(message.getTimestamp(), inputFormatter);
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm");
            String formattedDate = dateFormat.format(dateTime);
            ((ReceiverViewHolder) holder).receiverTime.setText(formattedDate);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (Preferences.getUserId(context) != messages.get(position).getUserId()) {
            return RECEIVER;
        } else {
            return SENDER;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView receiverMessage, receiverTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMessage = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.receiver_time);
        }

    }
    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderMessage, senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.sender_time);
        }
    }
}
