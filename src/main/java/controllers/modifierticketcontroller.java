package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.entities.Ticket;
import tn.esprit.service.ServiceTicket;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class modifierticketcontroller {

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private TextField txtNumeroTelephone;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPrix;

    @FXML
    private TextField txtReservationId;

    private Ticket ticket;

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
        txtNom.setText(ticket.getNom());
        txtPrenom.setText(ticket.getPrenom());
        txtNumeroTelephone.setText(String.valueOf(ticket.getNumero_telephone()));
        // Conversion en utilisant Instant.ofEpochMilli pour éviter l'UnsupportedOperationException
        LocalDate localDate = Instant.ofEpochMilli(ticket.getDate().getTime())
                .atZone(ZoneId.systemDefault()).toLocalDate();
        datePicker.setValue(localDate);
        txtEmail.setText(ticket.getEmail());
        txtPrix.setText(String.valueOf(ticket.getPrix()));
        txtReservationId.setText(String.valueOf(ticket.getReservation_id()));
    }

    @FXML
    private void handleEnregistrer() {
        ticket.setNom(txtNom.getText());
        ticket.setPrenom(txtPrenom.getText());
        try {
            ticket.setNumero_telephone(Integer.parseInt(txtNumeroTelephone.getText()));
        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "Le numéro de téléphone doit être un entier.");
            return;
        }
        LocalDate localDate = datePicker.getValue();
        if (localDate != null) {
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            ticket.setDate(date);
        } else {
            showAlert("Erreur de saisie", "La date est obligatoire.");
            return;
        }
        ticket.setEmail(txtEmail.getText());
        try {
            ticket.setPrix(Integer.parseInt(txtPrix.getText()));
        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "Le prix doit être un entier.");
            return;
        }
        try {
            ticket.setReservation_id(Integer.parseInt(txtReservationId.getText()));
        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "L'ID de réservation doit être un entier.");
            return;
        }

        ServiceTicket serviceTicket = new ServiceTicket();
        try {
            serviceTicket.modifier(ticket);
            showAlert("Modification", "Ticket modifié avec succès !");
            closeStage();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la modification : " + e.getMessage());
        }
    }

    @FXML
    private void handleAnnuler() {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) txtNom.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
