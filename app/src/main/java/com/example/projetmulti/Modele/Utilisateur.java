package com.example.projetmulti.Modele;

public class Utilisateur {
    private String id;
    private String prenom;
    private String pseudo;
    private String mail;
    private String mdp;
    private String infopers;
    private String comp1;
    private String desc_comp1;
    private String comp2;
    private String desc_comp2;
    private String comp3;
    private String desc_comp3;
    private String imageURL;

    public Utilisateur(String id, String pseudo, String imageURL, String prenom, String mail, String mdp, String infopers,
                       String comp1, String desc_comp1, String comp2, String desc_comp2, String comp3, String desc_comp3) {
        this.id = id;
        this.prenom = prenom;
        this.pseudo = pseudo;
        this.mail = mail;
        this.mdp = mdp;
        this.infopers = infopers;
        this.comp1 = comp1;
        this.desc_comp1 = desc_comp1;
        this.comp2 = comp2;
        this.desc_comp2 = desc_comp2;
        this.comp3 = comp3;
        this.desc_comp3 = desc_comp3;
        this.imageURL = imageURL;
    }

    public Utilisateur() {

    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getInfopers() {
        return infopers;
    }

    public void setInfopers(String infopers) {
        this.infopers = infopers;
    }

    public String getComp1() {
        return comp1;
    }

    public void setComp1(String comp1) {
        this.comp1 = comp1;
    }

    public String getDesc_comp1() {
        return desc_comp1;
    }

    public void setDesc_comp1(String desc_comp1) {
        this.desc_comp1 = desc_comp1;
    }

    public String getComp2() {
        return comp2;
    }

    public void setComp2(String comp2) {
        this.comp2 = comp2;
    }

    public String getDesc_comp2() {
        return desc_comp2;
    }

    public void setDesc_comp2(String desc_comp2) {
        this.desc_comp2 = desc_comp2;
    }

    public String getComp3() {
        return comp3;
    }

    public void setComp3(String comp3) {
        this.comp3 = comp3;
    }

    public String getDesc_comp3() {
        return desc_comp3;
    }

    public void setDesc_comp3(String desc_comp3) {
        this.desc_comp3 = desc_comp3;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getEmail() {
        return mail;
    }

    public void setEmail(String mail) {
        this.mail = mail;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
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
