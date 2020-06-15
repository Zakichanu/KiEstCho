package com.ztobbal.kiestcho.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ztobbal.kiestcho.Modele.Chat;
import com.ztobbal.kiestcho.R;
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

        if(position ==  mChat.size()-1){
            if(chat.isVu()){
                holder.txt_vu.setText("Vu");
            } else{
                holder.txt_vu.setText("Distribué");
            }
        } else{
            holder.txt_vu.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView message_afficher;
        public ImageView pdp;

        public TextView txt_vu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message_afficher = itemView.findViewById(R.id.message_afficher);
            pdp = itemView.findViewById(R.id.pdp);
            txt_vu = itemView.findViewById(R.id.txt_vu);
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
