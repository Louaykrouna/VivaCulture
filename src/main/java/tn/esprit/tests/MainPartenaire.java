package tn.esprit.tests;

import tn.esprit.entities.Partenaire;
import tn.esprit.services.PartenaireService;

import java.sql.SQLException;

public class MainPartenaire {
    public static void main(String[] args) {
        PartenaireService partenaireService = new PartenaireService();

        // Exemple d'un partenaire
        Partenaire p = new Partenaire();
        p.setIdP("P123");
        p.setNomP("PartenaireX");
        p.setEmailP("x@example.com");
        p.setTelephone("12345678");
        p.setTypeP("Institution");

        // Tester les fonctions
        try {
            //partenaireService.ajouter(p);
            //partenaireService.supprimer(p);
            //partenaireService.modifier(1, "PartenaireY");
            System.out.println(partenaireService.recuperer());
        } catch (SQLException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
}
