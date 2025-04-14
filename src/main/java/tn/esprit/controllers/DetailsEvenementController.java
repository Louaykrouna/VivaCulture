package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.entities.Evenement;

import java.io.IOException;

public class DetailsEvenementController {

    @FXML
    private Label lblTitre;
    @FXML
    private Label lblDescription;
    @FXML
    private Label lblLieu;
    @FXML
    private Label lblDateDebut;
    @FXML
    private Label lblDateFin;
    @FXML
    private Label lblStatut;
    @FXML
    private Label lblUrlImage;
    @FXML
    private Label lblMail;

    private Evenement evenement;

    public void setEvenement(Evenement e) {
        this.evenement = e;
        lblTitre.setText("Titre: " + e.getTitre());
        lblDescription.setText("Description: " + e.getDescription());
        lblLieu.setText("Lieu: " + e.getLieu());
        lblDateDebut.setText("Date début: " + e.getDateDebut());
        lblDateFin.setText("Date fin: " + e.getDateFin());
        lblStatut.setText("Statut: " + e.getStatut());
        lblUrlImage.setText("Image URL: " + e.getUrlImage());
        lblMail.setText("Mail: " + e.getMail());
    }

    @FXML
    private void handleRetour() {
        Stage stage = (Stage) lblTitre.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleAjouterEvenement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/AjouterEvenement.fxml"));
            VBox root = loader.load();

            AjouterEvenementController controller = loader.getController();
            controller.setOnEvenementAjoute(() -> {
                // Ferme la fenêtre de détails après ajout
                Stage stage = (Stage) lblTitre.getScene().getWindow();
                stage.close();
            });

            Stage stage = new Stage();
            stage.setTitle("Ajouter un événement");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
