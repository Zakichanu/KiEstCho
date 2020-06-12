package com.example.projetmulti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projetmulti.Accueil_test;
import com.example.projetmulti.Modele.Utilisateur;
import com.example.projetmulti.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<Utilisateur> mUtilisateurs;

    public UserAdapter(Context mContext, List<Utilisateur> mUtilisateurs){
        this.mContext = mContext;
        this.mUtilisateurs = mUtilisateurs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Utilisateur utilisateur = mUtilisateurs.get(position);
        holder.pseudonyme.setText(utilisateur.getPseudo());
        if (utilisateur.getImageURL().equals("default")){
            holder.pdp.setImageResource(R.drawable.images);
        }else{
            Glide.with(mContext).load(utilisateur.getImageURL()).into(holder.pdp);
        }
    }

    @Override
    public int getItemCount() {
        return mUtilisateurs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView pseudonyme;
        public ImageView pdp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pseudonyme = itemView.findViewById(R.id.pseudonyme);
            pdp = itemView.findViewById(R.id.pdp);
        }
    }
}
