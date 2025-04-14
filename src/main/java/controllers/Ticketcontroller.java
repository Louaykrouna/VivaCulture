package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import tn.esprit.entities.Ticket;
import tn.esprit.service.ServiceTicket;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class Ticketcontroller {

    @FXML
    private Button ajouterBtn;
    @FXML
    private Button btnAfficher;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfNom;
    @FXML
    private TextField tfPrenom;
    @FXML
    private TextField tfPrix;
    @FXML
    private TextField tfReservationId;
    @FXML
    private TextField tfTel;

    @FXML
    void handleAjouter(ActionEvent event) {

        String nom = tfNom.getText().trim();
        String prenom = tfPrenom.getText().trim();
        String email = tfEmail.getText().trim();
        String telStr = tfTel.getText().trim();
        String prixStr = tfPrix.getText().trim();
        String reservationIdStr = tfReservationId.getText().trim();

        // Vérification des champs vides
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || telStr.isEmpty() || prixStr.isEmpty() || reservationIdStr.isEmpty()) {
            showAlert("Champs vides", "Tous les champs sont obligatoires.");
            return;
        }

        // Vérification de l'email
        if (!email.matches("^(.+)@(.+)$")) {
            showAlert("Email invalide", "Veuillez saisir une adresse email valide.");
            return;
        }

        int numeroTelephone;
        int prix;
        int reservationId;

        try {
            numeroTelephone = Integer.parseInt(telStr);
            prix = Integer.parseInt(prixStr);
            reservationId = Integer.parseInt(reservationIdStr);

            if (numeroTelephone <= 0 || prix <= 0 || reservationId <= 0) {
                showAlert("Valeur invalide", "Les valeurs numériques doivent être positives.");
                return;
            }

        } catch (NumberFormatException e) {
            showAlert("Erreur de format", "Téléphone, prix et ID réservation doivent être des nombres.");
            return;
        }

        try {
            Ticket ticket = new Ticket(nom, prenom, numeroTelephone, new Date(), email, prix, reservationId);
            ServiceTicket serviceTicket = new ServiceTicket();
            serviceTicket.ajouter(ticket);

            showAlertInfo("Succès", "Le ticket a été ajouté avec succès !");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ticketInfo.fxml"));
            Parent parent = loader.load();

            ticketInfocontroller controller = loader.getController();
            controller.setNomTK(nom);
            controller.setPrenomTK(prenom);
            controller.setEmailTK(email);
            controller.setNumTK(String.valueOf(numeroTelephone));
            controller.setPrixTK(String.valueOf(prix));
            controller.setIDRTK(String.valueOf(reservationId));
            controller.setDateTK(new java.sql.Date(new Date().getTime()).toLocalDate());

            tfNom.getScene().setRoot(parent);

        } catch (SQLException | IOException e) {
            showAlert("Erreur", "Erreur : " + e.getMessage());
        }
    }

    @FXML
    void handleAfficherTickets(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherTicket.fxml"));
            Parent root = loader.load();
            btnAfficher.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.show();
    }

    private void showAlertInfo(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.show();
    }
}
