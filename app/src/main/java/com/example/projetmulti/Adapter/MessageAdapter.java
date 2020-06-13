package com.example.projetmulti.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projetmulti.Conversation;
import com.example.projetmulti.Modele.Chat;
import com.example.projetmulti.Modele.Utilisateur;
import com.example.projetmulti.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chat> mChat;
    private  String imageurl;

    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl){
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = mChat.get(position);

        holder.message_afficher.setText(chat.getMessage());

        if(imageurl.equals("default")){
            holder.pdp.setImageResource(R.drawable.images);
        }else{
            Glide.with(mContext).load(imageurl).into(holder.pdp);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView message_afficher;
        public ImageView pdp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message_afficher = itemView.findViewById(R.id.message_afficher);
            pdp = itemView.findViewById(R.id.pdp);
        }
    }


    // Méthode permettant la gestion de l'affichage des messages
    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getExpediteur().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}