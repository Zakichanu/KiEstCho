package com.example.projetmulti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.example.projetmulti.Adapter.MessageAdapter;
import com.example.projetmulti.Modele.Chat;
import com.example.projetmulti.Modele.Utilisateur;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Conversation extends AppCompatActivity {

    private CircleImageView pdp;
    private TextView pseudonyme;

    private ImageButton btn_envoyer;
    private EditText txt_message;

    private MessageAdapter messageAdapter;
    private List<Chat> mChat;

    RecyclerView recyclerView;

    private FirebaseUser fuser;
    private DatabaseReference reference;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        // Permet l'empilement des messages
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        pdp = findViewById(R.id.pdp);
        pseudonyme = findViewById(R.id.pseudonyme);
        btn_envoyer = findViewById(R.id.btn_envoyer);
        txt_message = findViewById(R.id.txt_message);

        //Récuperation des infos du user sélectionné
        intent = getIntent();
        final String id_utilisateur = intent.getStringExtra("id_utilisateur");

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = txt_message.getText().toString();
                if (!msg.equals("")){
                    envoieMessage(fuser.getUid(), id_utilisateur, msg);
                    txt_message.setText("");
                }else {
                    Snackbar.make(v, "Le message est vide", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


        reference = FirebaseDatabase.getInstance().getReference("Utilisateurs").child(id_utilisateur);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Utilisateur utilisateur = dataSnapshot.getValue(Utilisateur.class);
                pseudonyme.setText(utilisateur.getPseudo());
                if (utilisateur.getImageURL().equals("default")){
                    pdp.setImageResource(R.drawable.images);
                }else{
                    Glide.with(Conversation.this).load(utilisateur.getImageURL()).into(pdp);
                }
                lectureMessage(fuser.getUid(), id_utilisateur, utilisateur.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void envoieMessage(String expediteur, String destinataire, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("expediteur", expediteur);
        hashMap.put("destinataire", destinataire);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);
    }

    private void lectureMessage(final String monID, final String id_utilisateur, final String imageurl){
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getDestinataire().equals(monID) && chat.getExpediteur().equals(id_utilisateur)
                    || chat.getDestinataire().equals(id_utilisateur) && chat.getExpediteur().equals(monID)){
                        mChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(Conversation.this, mChat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void statusUtilisateur(String status){
        reference = FirebaseDatabase.getInstance().getReference("Utilisateurs").child(fuser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        statusUtilisateur("en ligne");
    }

    @Override
    protected void onPause() {
        super.onPause();
        statusUtilisateur("hors-ligne");
    }
}
