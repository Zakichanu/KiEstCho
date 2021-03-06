package com.ztobbal.kiestcho.Fragements;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ztobbal.kiestcho.Adapter.UserAdapter;
import com.ztobbal.kiestcho.Modele.Chat;
import com.ztobbal.kiestcho.Modele.Liste_Chat;
import com.ztobbal.kiestcho.Modele.Utilisateur;
import com.ztobbal.kiestcho.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {
    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<Utilisateur> mUtilisateurs;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<Liste_Chat> Listutilisateurs;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        Listutilisateurs = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Liste_Chat").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Listutilisateurs.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Liste_Chat chatliste = snapshot.getValue(Liste_Chat.class);
                    Listutilisateurs.add(chatliste);
                }

                Listechat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

    private void Listechat() {
        mUtilisateurs = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Utilisateurs");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUtilisateurs.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Utilisateur utilisateur = snapshot.getValue(Utilisateur.class);
                    for (Liste_Chat liste_chat : Listutilisateurs){
                        if(utilisateur.getId().equals(liste_chat.getId())){
                            mUtilisateurs.add(utilisateur);
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(), mUtilisateurs, true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
