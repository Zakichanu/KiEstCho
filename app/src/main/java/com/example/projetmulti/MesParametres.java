package com.example.projetmulti;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;

import com.bumptech.glide.Glide;
import com.example.projetmulti.Modele.Utilisateur;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MesParametres extends AppCompatActivity {
    private CircleImageView pdp;
    private TextView pseudonyme;
    private Button option1;
    private Button option2;

    FirebaseUser useractuel;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        pdp = findViewById(R.id.pdp);
        pseudonyme = findViewById(R.id.pseudonyme);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);

        useractuel = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Utilisateurs").child(useractuel.getUid());

        // Remplissage dynamique de la toolbar
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Utilisateur utilisateur = dataSnapshot.getValue(Utilisateur.class);
                pseudonyme.setText(utilisateur.getPseudo());
                if (utilisateur.getImageURL().equals("default")){
                    pdp.setImageResource(R.drawable.images);
                }else{
                    Glide.with(getApplicationContext()).load(utilisateur.getImageURL()).into(pdp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        pdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MesParametres.this, MonProfil.class);
                startActivity(intent);
                finish();
            }
        });

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MesParametres.this, Parametre_Profil.class);
                startActivity(intent);
            }
        });
    }
}