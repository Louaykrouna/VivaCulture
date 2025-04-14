package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.time.LocalDate;

public class ticketInfocontroller {

    @FXML
    private TextField IDRTK;

    @FXML
    private TextField NomTK;

    @FXML
    private DatePicker dateTK;

    @FXML
    private TextField emailTK;

    @FXML
    private TextField numTK;

    @FXML
    private TextField prenomTK;

    @FXML
    private TextField prixTK;

    public void setIDRTK(String IDRTK) {
        this.IDRTK.setText(IDRTK);
    }

    public void setNomTK(String nomTK) {
        this.NomTK.setText(nomTK);
    }

    public void setDateTK(LocalDate dateTK) {
        this.dateTK.setValue(dateTK);
    }

    public void setEmailTK(String emailTK) {
        this.emailTK.setText(emailTK);
    }

    public void setNumTK(String numTK) {
        this.numTK.setText(numTK);
    }

    public void setPrenomTK(String prenomTK) {
        this.prenomTK.setText(prenomTK);
    }

    public void setPrixTK(String prixTK) {
        this.prixTK.setText(prixTK);
    }
    @FXML
    void retourVersAjouterTicket(ActionEvent event) {
        try {
            Parent ajouterTicketView = FXMLLoader.load(getClass().getResource("/ajouterTicket.fxml"));
            Scene scene = new Scene(ajouterTicketView);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
