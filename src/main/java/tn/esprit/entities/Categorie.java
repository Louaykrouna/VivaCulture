package tn.esprit.entities;

import java.util.ArrayList;
import java.util.List;

public class Categorie {
    private int id;
    private String nom;
    private String description;
    private String statut;
    private String url_image;

    // Liste des types d'événements associés
    private List<TypeEvenement> typeEvenements = new ArrayList<>();

    public Categorie() {
    }

    public Categorie(String nom, String description, String statut, String url_image) {
        this.nom = nom;
        this.description = description;
        this.statut = statut;
        this.url_image = url_image;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public String getStatut() {
        return statut;
    }

    public String getUrl_image() {
        return url_image;
    }

    public List<TypeEvenement> getTypeEvenements() {
        return typeEvenements;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public void setTypeEvenements(List<TypeEvenement> typeEvenements) {
        this.typeEvenements = typeEvenements;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", statut='" + statut + '\'' +
                ", url_image='" + url_image + '\'' +
                ", typeEvenements=" + typeEvenements +
                '}';
    }

}
