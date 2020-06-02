package com.example.projetmulti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Authentification extends AppCompatActivity {
    private MaterialEditText email, mdp;
    private Button btn_inscription, btn_auth;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        btn_inscription = findViewById(R.id.inscription);
        email = findViewById(R.id.mail);
        mdp = findViewById(R.id.mdp);
        btn_auth = findViewById(R.id.btn_connexion);

        auth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Connexion");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_mdp = mdp.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_mdp)){
                    Toast.makeText(Authentification.this, "Tous les champs doivent être rempli", Toast.LENGTH_SHORT).show();
                } else{
                    auth.signInWithEmailAndPassword(txt_email, txt_mdp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(Authentification.this, Accueil.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(Authentification.this, "Problème d'authentification", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
