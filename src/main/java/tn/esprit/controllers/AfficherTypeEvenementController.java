package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.entities.TypeEvenement;
import tn.esprit.services.ServiceTypeEvenement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherTypeEvenementController {

    @FXML
    private VBox vboxTypes;

    private final ServiceTypeEvenement serviceTypeEvenement;

    public AfficherTypeEvenementController() {
        try {
            serviceTypeEvenement = new ServiceTypeEvenement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        refresh();
    }

    public void refresh() {
        vboxTypes.getChildren().clear();
        List<TypeEvenement> types = serviceTypeEvenement.afficherTypeEvenements();

        for (TypeEvenement t : types) {
            HBox hbox = new HBox(10);

            // Nom cliquable et stylisé
            Label nom = new Label(t.getNom());
            nom.setStyle("-fx-text-fill: blue; -fx-underline: true; -fx-cursor: hand;");
            nom.setOnMouseEntered(event -> nom.setStyle("-fx-text-fill: red; -fx-underline: true; -fx-cursor: hand;"));
            nom.setOnMouseExited(event -> nom.setStyle("-fx-text-fill: blue; -fx-underline: true; -fx-cursor: hand;"));
            nom.setOnMouseClicked(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/type_evenement/DetailsTypeEvenement.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));

                    DetailsTypeEvenementController controller = loader.getController();
                    controller.initData(t);
                    controller.setAfficherController(this);

                    stage.setTitle("Détails Type d'Événement");
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            Label cat = new Label("📁 Catégorie: " + t.getCategorieNom());
            Label desc = new Label(" | Description: " + t.getDescription());
            Label image = new Label(" | Image: " + t.getUrlImage());

            Button btnModifier = new Button("Modifier");
            btnModifier.setOnAction(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/type_evenement/ModifierTypeEvenement.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));

                    ModifierTypeEvenementController modifierController = loader.getController();
                    modifierController.initData(t);
                    modifierController.setAfficherController(this);

                    stage.setTitle("Modifier Type d'Événement");
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            Button btnSupprimer = new Button("Supprimer");
            btnSupprimer.setOnAction(e -> {
                serviceTypeEvenement.supprimerTypeEvenement(t.getId());
                refresh();
            });

            hbox.getChildren().addAll(nom, cat, desc, image, btnModifier, btnSupprimer);
            hbox.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: #f9f9f9;");
            hbox.setSpacing(10);
            vboxTypes.getChildren().add(hbox);
        }

        // Ajouter bouton "Ajouter"
        Button btnAjouter = new Button("Ajouter un type d'événement");
        btnAjouter.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/type_evenement/AjouterTypeEvenement.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));

                AjouterTypeEvenementController ajouterController = loader.getController();
                ajouterController.setAfficherController(this);

                stage.setTitle("Ajouter un Type d'Événement");
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        vboxTypes.getChildren().add(btnAjouter);
    }

    @FXML
    private void handleRetour() {
        Stage stage = (Stage) vboxTypes.getScene().getWindow();
        stage.close();
    }

    public void actualiserListe() {
        refresh();
    }

    @FXML
    private void handleListeEvenements() {
        try {
            // Charger le FXML de la liste des événements
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/AfficherEvenement.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            // Ouvrir la fenêtre des événements
            stage.setTitle("Liste des Événements");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
