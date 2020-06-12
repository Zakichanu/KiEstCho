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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class Inscription extends AppCompatActivity {
    private MaterialEditText prenom, email, pseudo, mdp, cmdp;
    private Button btn_inscrire;

    private FirebaseAuth auth;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Inscription");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prenom = findViewById(R.id.prénom);
        email = findViewById(R.id.mail);
        pseudo = findViewById(R.id.pseudo);
        mdp = findViewById(R.id.mdp);
        cmdp = findViewById(R.id.cmdp);
        btn_inscrire = findViewById(R.id.btn_inscription);

        auth = FirebaseAuth.getInstance();

        btn_inscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_prenom = prenom.getText().toString();
                String txt_pseudo = pseudo.getText().toString();
                String txt_mail = email.getText().toString();
                String txt_mdp = mdp.getText().toString();
                String txt_cmdp = cmdp.getText().toString();

                if(TextUtils.isEmpty(txt_prenom) || TextUtils.isEmpty(txt_pseudo) || TextUtils.isEmpty(txt_mail)
                        || TextUtils.isEmpty(txt_mdp) || TextUtils.isEmpty(txt_cmdp)){
                    Toast.makeText(Inscription.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                } else if(txt_mdp.length() < 8){
                    Toast.makeText(Inscription.this, "Votre mot de passe doit contenir au moins 8 caractères", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!txt_mdp.equals(txt_cmdp)){
                    Toast.makeText(Inscription.this, "Les mots de passes ne correspondent pas", Toast.LENGTH_SHORT).show();
                    return;
                } else{
                    inscription(txt_prenom, txt_pseudo, txt_mail, txt_mdp, txt_cmdp);

                }
            }
        });
    }


    private void inscription(final String prenom, final String pseudo, final String email, final String mdp, final String cmdp){
        auth.createUserWithEmailAndPassword(email, mdp)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String pseudo_id = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Utilisateurs").child(pseudo_id);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", pseudo_id);
                            hashMap.put("prenom", prenom);
                            hashMap.put("pseudo", pseudo);
                            hashMap.put("mail", email);
                            hashMap.put("mdp", mdp);
                            hashMap.put("infopers", "");
                            hashMap.put("comp1", "");
                            hashMap.put("desc_comp1", "");
                            hashMap.put("comp2", "");
                            hashMap.put("desc_comp2", "");
                            hashMap.put("comp3", "");
                            hashMap.put("desc_comp3", "");
                            hashMap.put("imageURL", "default");


                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(Inscription.this, Authentification.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else{
                            Toast.makeText(Inscription.this, "Vous ne pouvez pas vous inscrire avec cet e-mail ou ce mot de passe", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
