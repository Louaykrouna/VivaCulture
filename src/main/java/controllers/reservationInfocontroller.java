package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.time.LocalDate;

public class reservationInfocontroller {

    @FXML
    private TextField NbrDisplay;

    @FXML
    private DatePicker dateDisplay;

    @FXML
    private TextField emailDisplay;

    @FXML
    private TextField eventDisplay;

    @FXML
    private TextField heureDisplay;

    @FXML
    private Button btnRetour;

    public void setNbrDisplay(String nbr) {
        this.NbrDisplay.setText(nbr);
    }

    public void setDateDisplay(LocalDate date) {
        this.dateDisplay.setValue(date);
    }

    public void setEmailDisplay(String email) {
        this.emailDisplay.setText(email);
    }

    public void setEventDisplay(String event) {
        this.eventDisplay.setText(event);
    }

    public void setHeureDisplay(String heure) {
        this.heureDisplay.setText(heure);
    }

    @FXML
    void handleRetour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterreservation.fxml"));
            Parent root = loader.load();
            btnRetour.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de revenir à l'ajout");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }
}
