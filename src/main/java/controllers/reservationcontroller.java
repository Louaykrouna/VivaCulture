package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.reservation;
import tn.esprit.service.Servicereservation;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class reservationcontroller {

    @FXML
    private DatePicker DateDT;

    @FXML
    private TextField EmailR;

    @FXML
    private TextField Hour;

    @FXML
    private TextField Nmbr;

    @FXML
    private TextField NomEV;

    @FXML
    private Button btnAjouterTicket;

    @FXML
    void initialize() {
        // Désactiver les dates passées
        DateDT.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });
    }

    @FXML
    void Ajouterreservation(ActionEvent event) {
        String nomEvenement = NomEV.getText().trim();
        String nombreTicketsStr = Nmbr.getText().trim();
        String heureStr = Hour.getText().trim();
        String email = EmailR.getText().trim();

        resetFieldStyles();

        if (nomEvenement.isEmpty() || nombreTicketsStr.isEmpty() || DateDT.getValue() == null || heureStr.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            if (nomEvenement.isEmpty()) NomEV.setStyle("-fx-border-color: red;");
            if (nombreTicketsStr.isEmpty()) Nmbr.setStyle("-fx-border-color: red;");
            if (DateDT.getValue() == null) DateDT.setStyle("-fx-border-color: red;");
            if (heureStr.isEmpty()) Hour.setStyle("-fx-border-color: red;");
            if (email.isEmpty()) EmailR.setStyle("-fx-border-color: red;");
            return;
        }

        int nombreTickets;
        try {
            nombreTickets = Integer.parseInt(nombreTicketsStr);
            if (nombreTickets <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le nombre de tickets doit être un entier positif.");
                Nmbr.setStyle("-fx-border-color: red;");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer un nombre valide pour les tickets.");
            Nmbr.setStyle("-fx-border-color: red;");
            return;
        }

        java.sql.Time heure;
        try {
            if (!heureStr.matches("\\d{2}:\\d{2}")) {
                throw new IllegalArgumentException("Format de l'heure invalide.");
            }
            heure = java.sql.Time.valueOf(heureStr + ":00");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Heure invalide", "Veuillez entrer une heure valide au format HH:mm.");
            Hour.setStyle("-fx-border-color: red;");
            return;
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert(Alert.AlertType.ERROR, "Email invalide", "Veuillez entrer une adresse email valide.");
            EmailR.setStyle("-fx-border-color: red;");
            return;
        }

        try {
            java.sql.Date dateReservation = java.sql.Date.valueOf(DateDT.getValue());

            // ✅ Vérification date non dépassée
            if (dateReservation.before(new java.sql.Date(System.currentTimeMillis()))) {
                showAlert(Alert.AlertType.ERROR, "Date invalide", "La date de réservation ne peut pas être dans le passé.");
                DateDT.setStyle("-fx-border-color: red;");
                return;
            }

            reservation r = new reservation(nomEvenement, nombreTickets, dateReservation, heure, email);
            Servicereservation servicereservation = new Servicereservation();
            servicereservation.ajouter(r);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation ajoutée avec succès !");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservationInfo.fxml"));
            Parent parent = loader.load();
            reservationInfocontroller controller = loader.getController();

            controller.setNbrDisplay(String.valueOf(nombreTickets));
            controller.setDateDisplay(dateReservation.toLocalDate());
            controller.setEmailDisplay(email);
            controller.setEventDisplay(nomEvenement);
            controller.setHeureDisplay(heure.toString());

            NomEV.getScene().setRoot(parent);

        } catch (SQLException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }
    }

    @FXML
    public void Afficherreservation(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReservationView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'affichage des réservations : " + getClass().getResource("/AfficherReservationView.fxml"));
        }
    }

    @FXML
    void goToAjouterTicket(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ajouterticket.fxml"));
            Parent root = loader.load();
            btnAjouterTicket.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible d’accéder à l’interface d’ajout de ticket");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private void resetFieldStyles() {
        NomEV.setStyle(null);
        Nmbr.setStyle(null);
        DateDT.setStyle(null);
        Hour.setStyle(null);
        EmailR.setStyle(null);
    }
}
