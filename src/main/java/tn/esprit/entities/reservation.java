package tn.esprit.entities;

import java.util.Date;

public class reservation
{
    private int id;
    private String nom_evenement;
    private int nombre_tickets ;
    private Date date_reservation ;
    private Date heure ;
    private String email ;


    public reservation() {
    }

    public reservation(int id, String nom_evenement, int nombre_tickets, Date date_reservation, Date heure, String email) {
        this.id = id;
        this.nom_evenement = nom_evenement;
        this.nombre_tickets = nombre_tickets;
        this.date_reservation = date_reservation;
        this.heure = heure;
        this.email = email;
    }

    public reservation(String nom_evenement, int nombre_tickets, Date date_reservation, Date heure, String email) {
        this.nom_evenement = nom_evenement;
        this.nombre_tickets = nombre_tickets;
        this.date_reservation = date_reservation;
        this.heure = heure;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_evenement() {
        return nom_evenement;
    }

    public void setNom_evenement(String nom_evenement) {
        this.nom_evenement = nom_evenement;
    }

    public int getNombre_tickets() {
        return nombre_tickets;
    }

    public void setNombre_tickets(int nombre_tickets) {
        this.nombre_tickets = nombre_tickets;
    }

    public Date getDate_reservation() {
        return date_reservation;
    }

    public void setDate_reservation(Date date_reservation) {
        this.date_reservation = date_reservation;
    }

    public Date getHeure() {
        return heure;
    }

    public void setHeure(Date heure) {
        this.heure = heure;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "reservation{" +
                "id=" + id +
                ", nom_evenement='" + nom_evenement + '\'' +
                ", nombre_tickets=" + nombre_tickets +
                ", date_reservation=" + date_reservation +
                ", heure=" + heure +
                ", email='" + email + '\'' +
                '}';
    }
}
