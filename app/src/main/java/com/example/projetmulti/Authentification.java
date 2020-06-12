package com.example.projetmulti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Authentification extends AppCompatActivity {
    private MaterialEditText email, mdp;
    private Button btn_inscription, btn_auth;

    // Authentification permettant la connexion
    private FirebaseAuth auth;

    // Variable permettant de savoir si on s'est déjà connecté auparavant
    private FirebaseUser useractuel;


    @Override
    protected void onStart() {
        super.onStart();

        useractuel = FirebaseAuth.getInstance().getCurrentUser();


        // Condition permettant la connexion automatique
        if(useractuel != null){
            Intent intent = new Intent(Authentification.this, Accueil_test.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);


        btn_inscription = findViewById(R.id.inscription);
        email = findViewById(R.id.mail);
        email.setTextColor(Color.WHITE);
        mdp = findViewById(R.id.mdp);
        mdp.setTextColor(Color.WHITE);
        btn_auth = findViewById(R.id.btn_connexion);

        auth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Connexion");

        btn_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Authentification.this, Inscription.class);
                startActivity(intent);
                finish();
            }
        });

        btn_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String txt_email = email.getText().toString();
                String txt_mdp = mdp.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_mdp)){
                    Snackbar.make(v, "Tous les champs doivent être rempli", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else{
                    auth.signInWithEmailAndPassword(txt_email, txt_mdp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(Authentification.this, Accueil_test.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                Snackbar.make(v, "Problème d'authentification", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
