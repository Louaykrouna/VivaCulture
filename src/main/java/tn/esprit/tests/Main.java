package tn.esprit.tests;

import tn.esprit.entities.reservation;
import tn.esprit.entities.Ticket;
import tn.esprit.service.Servicereservation;
import tn.esprit.service.ServiceTicket;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Servicereservation sr = new Servicereservation();
        ServiceTicket serviceTicket = new ServiceTicket();

        try {
            // ==== AJOUT ====
            Date dateReservation = new Date(); // date actuelle
            Date heure = new Date(); // heure actuelle

            reservation r = new reservation("Hackathon IA", 3, dateReservation, heure, "participant@example.com");
            sr.ajouter(r);
            System.out.println("Réservation ajoutée avec ID : " + r.getId());

            int reservationId = r.getId();

            Ticket ticket1 = new Ticket("Ali", "Ben Ali", 12345678, new Date(), "ali@example.com", 100, reservationId);
            serviceTicket.ajouter(ticket1);
            System.out.println("Ticket ajouté pour la réservation ID " + reservationId);

            // ==== AFFICHAGE ====
            System.out.println("Liste des réservations :");
            List<reservation> reservations = sr.afficher();
            for (reservation res : reservations) {
                System.out.println(res);
            }

            // ==== MODIFICATION ====
            if (!reservations.isEmpty()) {
                reservation firstReservation = reservations.get(0); // Modifier la première

                System.out.println("Avant modification : " + firstReservation);

                // Changement de quelques champs
                firstReservation.setNom_evenement("Nouveau nom d'événement");
                firstReservation.setNombre_tickets(10);
                firstReservation.setEmail("nouveau.email@example.com");

                sr.modifier(firstReservation);

                System.out.println("Après modification : " + sr.afficher().get(0));
            } else {
                System.out.println("Aucune réservation à modifier.");
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

