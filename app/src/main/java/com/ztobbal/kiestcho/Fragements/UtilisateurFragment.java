package com.ztobbal.kiestcho.Fragements;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ztobbal.kiestcho.Adapter.UserAdapter;
import com.ztobbal.kiestcho.Modele.Utilisateur;
import com.ztobbal.kiestcho.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UtilisateurFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Utilisateur> mUtilisateurs;

    private EditText recherche_bar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_utilisateurs, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUtilisateurs = new ArrayList<>();

        listeUtilisateurs();

        recherche_bar = view.findViewById(R.id.recherche_bar);

        recherche_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rechercheUtilisateur(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void rechercheUtilisateur(String s) {
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Utilisateurs").orderByChild("pseudo")
                .startAt(s).endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUtilisateurs.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Utilisateur utilisateur = snapshot.getValue(Utilisateur.class);
                    if (!utilisateur.getId().equals(fuser.getUid())){
                        mUtilisateurs.add(utilisateur);
                    }
                }

                userAdapter = new UserAdapter(getContext(), mUtilisateurs, false);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void listeUtilisateurs() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Utilisateurs");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(recherche_bar.getText().toString().equals("")) {
                    mUtilisateurs.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Utilisateur utilisateur = snapshot.getValue(Utilisateur.class);
                        if (!utilisateur.getId().equals(firebaseUser.getUid())) {
                            mUtilisateurs.add(utilisateur);
                        }
                    }
                    userAdapter = new UserAdapter(getContext(), mUtilisateurs, false);
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
