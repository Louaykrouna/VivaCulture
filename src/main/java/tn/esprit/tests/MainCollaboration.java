package tn.esprit.tests;

import tn.esprit.services.CollaborationService;
import tn.esprit.entities.Collaboration;

import java.sql.SQLException;
import java.time.LocalDate;

public class MainCollaboration {
    public static void main(String[] args) {
        CollaborationService cs = new CollaborationService();

        try {
            // Create a new Collaboration object with proper LocalDate values
            Collaboration newCol = new Collaboration(
                    "Gaith",                   // nom_c
                    "Partnership",             // type
                    LocalDate.of(2025, 4, 20), // date_sig
                    LocalDate.of(2026, 4, 20), // date_ex
                    "Active"                   // status
            );

            // Add the new Collaboration to the database
            cs.ajouter(newCol);
            System.out.println("Ajout réussi !");

            // Optional: Display all collaborations
            System.out.println(cs.recuperer());

        } catch (SQLException e) {
            System.out.println("Erreur SQL: " + e.getMessage());
        }
    }
}
