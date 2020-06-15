package com.ztobbal.kiestcho.Modele;

public class Chat {
    private String expediteur;
    private String destinataire;
    private String message;
    private boolean vu;

    public Chat(String expediteur, String destinataire, String message, boolean vu) {
        this.expediteur = expediteur;
        this.destinataire = destinataire;
        this.message = message;
        this.vu = vu;
    }

    public Chat(){

    }

    public String getExpediteur() {
        return expediteur;
    }

    public void setExpediteur(String expediteur) {
        this.expediteur = expediteur;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isVu() {
        return vu;
    }

    public void setVu(boolean vu) {
        this.vu = vu;
    }
}
