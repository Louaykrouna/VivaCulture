package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import tn.esprit.entities.Categorie;

import java.io.IOException;

public class DetailsCategorieController {

    @FXML private Label lblNom;
    @FXML private Label lblDescription;
    @FXML private Label lblStatut;
    @FXML private Label lblImage;

    private Categorie categorie;
    private AfficherCategoriesController afficherController;

    public void initData(Categorie c) {
        this.categorie = c;
        lblNom.setText("Nom : " + c.getNom());
        lblDescription.setText("Description : " + c.getDescription());
        lblStatut.setText("Statut : " + c.getStatut());
        lblImage.setText("Image URL : " + c.getUrl_image());
    }

    public void setAfficherController(AfficherCategoriesController controller) {
        this.afficherController = controller;
    }

    @FXML
    private void retourListe() {
        Stage stage = (Stage) lblNom.getScene().getWindow();
        stage.close(); // Ferme les détails, retourne à la liste
    }

    @FXML
    private void modifierCategorie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/categorie/ModifierCategorie.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            ModifierCategorieController controller = loader.getController();
            controller.initData(categorie);
            controller.setAfficherController(afficherController);

            stage.setTitle("Modifier Catégorie");
            stage.show();

            // Ferme la fenêtre de détails
            Stage currentStage = (Stage) lblNom.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
