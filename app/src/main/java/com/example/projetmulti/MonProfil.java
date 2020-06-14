package com.example.projetmulti;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.projetmulti.Modele.Utilisateur;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MonProfil extends AppCompatActivity {

    private CircleImageView pdp;
    private TextView pseudonyme;

    private TextView infoperso;
    private TextView comp1;
    private TextView desc_comp1;
    private TextView comp2;
    private TextView desc_comp2;
    private TextView comp3;
    private TextView desc_comp3;

    private FirebaseUser useractuel;
    private DatabaseReference reference;

    private StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monprofil);

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

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        useractuel = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Utilisateurs").child(useractuel.getUid());


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
                    Glide.with(getApplicationContext()).load(utilisateur.getImageURL()).into(pdp);
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
                Intent intent = new Intent(MonProfil.this, Parametre_Profil.class);
                startActivity(intent);

            }
        });

        pdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvertureImage();
            }
        });
    }

    // Ouverture du gestionnaire de fichier
    private void ouvertureImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getExtensionFichier(Uri uri){
        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getExtensionFichier(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Utilisateurs").child(useractuel.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", mUri);
                        reference.updateChildren(map);

                        pd.dismiss();
                    } else{
                        Toast.makeText(MonProfil.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MonProfil.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else{
        Toast.makeText(this, "Pas d'image sélectionnée", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null
        && data.getData() != null){
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else{
                uploadImage();
            }
        }
    }
}
