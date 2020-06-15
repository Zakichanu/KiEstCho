package com.ztobbal.kiestcho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ztobbal.kiestcho.Modele.Utilisateur;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilAutreUtilisateur extends AppCompatActivity {
    private CircleImageView pdp;
    private TextView pseudonyme;

    private TextView infoperso;
    private TextView comp1;
    private TextView desc_comp1;
    private TextView comp2;
    private TextView desc_comp2;
    private TextView comp3;
    private TextView desc_comp3;
    private DatabaseReference reference;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_autre_utilisateur);
        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        pdp = findViewById(R.id.pdp);
        pseudonyme = findViewById(R.id.pseudonyme);
        infoperso = findViewById(R.id.infoperso);
        comp1 = findViewById(R.id.comp1);
        desc_comp1 = findViewById(R.id.desc_comp1);
        comp2 = findViewById(R.id.comp2);
        desc_comp2 = findViewById(R.id.desc_comp2);
        comp3 = findViewById(R.id.comp3);
        desc_comp3 = findViewById(R.id.desc_comp3);

        //Récuperation des infos du user sélectionné
        intent = getIntent();
        final String id_utilisateur = intent.getStringExtra("id_utilisateur");
        reference = FirebaseDatabase.getInstance().getReference("Utilisateurs").child(id_utilisateur);


        // Remplissage dynamique de la toolbar ainsi que du profil
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Utilisateur utilisateur = dataSnapshot.getValue(Utilisateur.class);

                // Toolbar en fonction du user connecté
                pseudonyme.setText(utilisateur.getPseudo());
                if (utilisateur.getImageURL().equals("default")){
                    pdp.setImageResource(R.drawable.images);
                }else{
                    Glide.with(ProfilAutreUtilisateur.this).load(utilisateur.getImageURL()).into(pdp);
                }

                // Profil du user connecté
                if(!utilisateur.getComp1().equals("") && !utilisateur.getComp2().equals("") && !utilisateur.getComp3().equals("") && !utilisateur.getDesc_comp1().equals("")
                        && !utilisateur.getDesc_comp2().equals("") && !utilisateur.getDesc_comp3().equals("") && !utilisateur.getInfopers().equals("")){
                    infoperso.setText(utilisateur.getInfopers());
                    comp1.setText(utilisateur.getComp1());
                    desc_comp1.setText(utilisateur.getDesc_comp1());
                    comp2.setText(utilisateur.getComp2());
                    desc_comp2.setText(utilisateur.getDesc_comp2());
                    comp3.setText(utilisateur.getComp3());
                    desc_comp3.setText(utilisateur.getDesc_comp3());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Accès aux paramètres
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilAutreUtilisateur.this, Conversation.class);
                intent.putExtra("id_utilisateur", id_utilisateur);
                startActivity(intent);
            }
        });
    }
}
