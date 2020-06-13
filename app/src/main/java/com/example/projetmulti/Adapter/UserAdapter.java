package com.example.projetmulti.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projetmulti.Accueil_test;
import com.example.projetmulti.Conversation;
import com.example.projetmulti.Modele.Utilisateur;
import com.example.projetmulti.ProfilAutreUtilisateur;
import com.example.projetmulti.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<Utilisateur> mUtilisateurs;
    private boolean ischat;


    public UserAdapter(Context mContext, List<Utilisateur> mUtilisateurs, boolean ischat){
        this.mContext = mContext;
        this.mUtilisateurs = mUtilisateurs;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Utilisateur utilisateur = mUtilisateurs.get(position);
        holder.pseudonyme.setText(utilisateur.getPseudo());
        if (utilisateur.getImageURL().equals("default")){
            holder.pdp.setImageResource(R.drawable.images);
        }else{
            Glide.with(mContext).load(utilisateur.getImageURL()).into(holder.pdp);
        }

        if (ischat){
            if (utilisateur.getStatus().equals("en ligne")){
                holder.online.setVisibility(View.VISIBLE);
                holder.offline.setVisibility(View.GONE);
            }else{
                holder.online.setVisibility(View.GONE);
                holder.offline.setVisibility(View.VISIBLE);
            }
        }

        // Redirection vers la conversation avec le user sélectionner
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Conversation.class);
                intent.putExtra("id_utilisateur", utilisateur.getId());
                mContext.startActivity(intent);
            }
        });

        holder.pdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfilAutreUtilisateur.class);
                intent.putExtra("id_utilisateur", utilisateur.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUtilisateurs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView pseudonyme;
        public ImageView pdp;
        public ImageView online;
        public ImageView offline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pseudonyme = itemView.findViewById(R.id.pseudonyme);
            pdp = itemView.findViewById(R.id.pdp);
            online = itemView.findViewById(R.id.online);
            offline = itemView.findViewById(R.id.offline);
        }
    }
}
