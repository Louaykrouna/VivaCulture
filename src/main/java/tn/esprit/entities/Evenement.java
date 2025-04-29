package tn.esprit.entities;

import java.time.LocalDate;

public class Evenement {
    private int id;
    private int typeEvenementId;
    private String titre;
    private String description;
    private String lieu;
    private int nombrePlaces;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String statut;
    private String urlImage;
    private String mail;
    private double longitude;
    private double latitude;

    private String typeEvenementNom; // Pour affichage avec jointure

    public Evenement() {}

    public Evenement(int id, int typeEvenementId, String titre, String description, String lieu,
                     int nombrePlaces, LocalDate dateDebut, LocalDate dateFin,
                     String statut, String urlImage, String mail, double latitude, double longitude) {
        this.id = id;
        this.typeEvenementId = typeEvenementId;
        this.titre = titre;
        this.description = description;
        this.lieu = lieu;
        this.nombrePlaces = nombrePlaces;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.urlImage = urlImage;
        this.mail = mail;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters et Setters...

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTypeEvenementId() { return typeEvenementId; }
    public void setTypeEvenementId(int typeEvenementId) { this.typeEvenementId = typeEvenementId; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public int getNombrePlaces() { return nombrePlaces; }
    public void setNombrePlaces(int nombrePlaces) { this.nombrePlaces = nombrePlaces; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getUrlImage() { return urlImage; }
    public void setUrlImage(String urlImage) { this.urlImage = urlImage; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getTypeEvenementNom() { return typeEvenementNom; }
    public void setTypeEvenementNom(String typeEvenementNom) { this.typeEvenementNom = typeEvenementNom; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
}
