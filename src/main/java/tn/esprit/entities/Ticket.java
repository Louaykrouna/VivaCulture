package tn.esprit.entities;

import java.util.Date;

public class Ticket {
   private int id ;

   private String nom ;
   private String prenom ;
   private int numero_telephone ;
   private String email ;
   private Date date ;
   private int prix ;
   private int reservation_id ;

   public Ticket() {}

    public Ticket(int id, String nom, String prenom, int numero_telephone, String email, Date date, int prix, int reservation_id) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.numero_telephone = numero_telephone;
        this.email = email;
        this.date = date;
        this.prix = prix;
        this.reservation_id = reservation_id;
    }

    public Ticket(String nom, String prenom, int numero_telephone, Date date, String email, int prix, int reservation_id) {
        this.nom = nom;
        this.prenom = prenom;
        this.numero_telephone = numero_telephone;
        this.date = date;
        this.email = email;
        this.prix = prix;
        this.reservation_id = reservation_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getNumero_telephone() {
        return numero_telephone;
    }

    public void setNumero_telephone(int numero_telephone) {
        this.numero_telephone = numero_telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(int reservation_id) {
        this.reservation_id = reservation_id;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", numero_telephone=" + numero_telephone +
                ", email='" + email + '\'' +
                ", date=" + date +
                ", prix=" + prix +
                ", reservation_id=" + reservation_id +
                '}';
    }
}
