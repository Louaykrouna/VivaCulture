package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.esprit.entities.TypeEvenement;

import java.io.IOException;

public class DetailsTypeEvenementController {

    @FXML
    private Label labelNom;

    @FXML
    private Label labelDescription;

    @FXML
    private Label labelImage;

    @FXML
    private Label labelCategorie;

    @FXML
    private Button btnRetour;

    @FXML
    private Button btnModifier;

    private TypeEvenement typeEvenement;
    private AfficherTypeEvenementController afficherController;

    public void initData(TypeEvenement t) {
        this.typeEvenement = t;
        labelNom.setText("Nom : " + t.getNom());
        labelDescription.setText("Description : " + t.getDescription());
        labelImage.setText("Image : " + t.getUrlImage());
        labelCategorie.setText("Catégorie : " + t.getCategorieNom());
    }

    public void setAfficherController(AfficherTypeEvenementController controller) {
        this.afficherController = controller;
    }

    @FXML
    private void handleRetour() {
        Stage stage = (Stage) btnRetour.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void modifierTypeEvenement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/type_evenement/ModifierTypeEvenement.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            ModifierTypeEvenementController controller = loader.getController();
            controller.initData(typeEvenement);
            controller.setAfficherController(afficherController);

            stage.setTitle("Modifier Type d'Événement");
            stage.show();

            // Fermer la fenêtre de détails
            Stage currentStage = (Stage) btnModifier.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
