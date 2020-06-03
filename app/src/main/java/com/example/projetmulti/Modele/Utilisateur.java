package com.example.projetmulti.Modele;

public class Utilisateur {
    private String id;
    private String pseudo;
    private String imageURL;

    public Utilisateur(String id, String pseudo, String imageURL) {
        this.id = id;
        this.pseudo = pseudo;
        this.imageURL = imageURL;
    }

    public Utilisateur() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String username) {
        this.pseudo = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
