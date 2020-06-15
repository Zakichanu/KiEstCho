package com.ztobbal.kiestcho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ztobbal.kiestcho.Modele.Utilisateur;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Parametre_Profil extends AppCompatActivity {
    private CircleImageView pdp;
    private TextView pseudonyme;

    private TextView infoperso;
    private TextView comp1;
    private TextView comp2;
    private TextView comp3;
    private TextView desc_comp1;
    private TextView desc_comp2;
    private TextView desc_comp3;

    private Button btn_valider;

    FirebaseUser useractuel;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre_profil);
        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        pdp = findViewById(R.id.pdp);
        pseudonyme = findViewById(R.id.pseudonyme);
        infoperso = findViewById(R.id.infoperso);
        comp1 = findViewById(R.id.comp1);
        comp2 = findViewById(R.id.comp2);
        comp3 = findViewById(R.id.comp3);
        desc_comp1 = findViewById(R.id.desc_comp1);
        desc_comp2 = findViewById(R.id.desc_comp2);
        desc_comp3 = findViewById(R.id.desc_comp3);
        btn_valider = findViewById(R.id.btn_valider);

        useractuel = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Utilisateurs").child(useractuel.getUid());

        // Remplissage dynamique de la toolbar avec la photo de profile et le username
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Utilisateur utilisateur = dataSnapshot.getValue(Utilisateur.class);
                pseudonyme.setText(utilisateur.getPseudo());
                if (utilisateur.getImageURL().equals("default")) {
                    pdp.setImageResource(R.drawable.images);
                } else {
                    Glide.with(Parametre_Profil.this).load(utilisateur.getImageURL()).into(pdp);
                }
                infoperso.setText(utilisateur.getInfopers());
                comp1.setText(utilisateur.getComp1());
                desc_comp1.setText(utilisateur.getDesc_comp1());
                comp2.setText(utilisateur.getComp2());
                desc_comp2.setText(utilisateur.getDesc_comp2());
                comp3.setText(utilisateur.getComp3());
                desc_comp3.setText(utilisateur.getDesc_comp3());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        pdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Parametre_Profil.this, MonProfil.class);
                startActivity(intent);
                finish();
            }
        });
        //

        // Indexation des informations dans notre base de données
        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String txt_infoperso = infoperso.getText().toString();
                final String txt_comp1 = comp1.getText().toString();
                final String txt_desc_comp1 = desc_comp1.getText().toString();
                final String txt_comp2 = comp2.getText().toString();
                final String txt_desc_comp2 = desc_comp2.getText().toString();
                final String txt_comp3 = comp3.getText().toString();
                final String txt_desc_comp3 = desc_comp3.getText().toString();

                if (TextUtils.isEmpty(txt_infoperso) || TextUtils.isEmpty(txt_comp1) || TextUtils.isEmpty(txt_comp2) || TextUtils.isEmpty(txt_comp3)
                        || TextUtils.isEmpty(txt_desc_comp1) || TextUtils.isEmpty(txt_desc_comp2) || TextUtils.isEmpty(txt_desc_comp3)) {
                    Snackbar.make(v, "Veuillez remplir tous les champs", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Utilisateur utilisateur = dataSnapshot.getValue(Utilisateur.class);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("infopers", txt_infoperso);
                            hashMap.put("comp1", txt_comp1);
                            hashMap.put("desc_comp1", txt_desc_comp1);
                            hashMap.put("comp2", txt_comp2);
                            hashMap.put("desc_comp2", txt_desc_comp2);
                            hashMap.put("comp3", txt_comp3);
                            hashMap.put("desc_comp3", txt_desc_comp3);
                            reference.updateChildren(hashMap);
                            Intent intent = new Intent(Parametre_Profil.this, MonProfil.class);
                            startActivity(intent);
                            finish();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
    }
}
