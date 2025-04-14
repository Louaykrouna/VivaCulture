package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.esprit.entities.Evenement;
import tn.esprit.services.ServiceEvenement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherEvenementController {

    @FXML
    private VBox vboxEvenements;

    private final ServiceEvenement serviceEvenement;

    public AfficherEvenementController() {
        try {
            serviceEvenement = new ServiceEvenement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        refresh();
    }

    public void refresh() {
        vboxEvenements.getChildren().clear();
        List<Evenement> evenements = serviceEvenement.afficherEvenements();

        for (Evenement e : evenements) {
            HBox hbox = new HBox(10);

            // Label pour le titre (cliquable pour afficher le détail)
            Label titre = new Label(e.getTitre());
            titre.setStyle("-fx-text-fill: blue; -fx-underline: true; -fx-cursor: hand;");
            titre.setOnMouseEntered(ev -> titre.setStyle("-fx-text-fill: red; -fx-underline: true; -fx-cursor: hand;"));
            titre.setOnMouseExited(ev -> titre.setStyle("-fx-text-fill: blue; -fx-underline: true; -fx-cursor: hand;"));
            titre.setOnMouseClicked(ev -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/DetailsEvenement.fxml"));
                    VBox root = loader.load();

                    DetailsEvenementController controller = loader.getController();
                    controller.setEvenement(e);

                    Stage stage = new Stage();
                    stage.setTitle("Détails de l'événement");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            Label typeEvenement = new Label("Type: " + e.getTypeEvenementNom());
            Label lieu = new Label("Lieu: " + e.getLieu());
            Label nbrPlaces = new Label("Nbr de places: " + e.getNombrePlaces());
            Label dateDebut = new Label("Début: " + e.getDateDebut());
            Label dateFin = new Label("Fin: " + e.getDateFin());
            Label statut = new Label("Statut: " + e.getStatut());
            Label image = new Label("Image: " + e.getUrlImage());

            // Bouton pour supprimer l'événement
            Button btnSupprimer = new Button("Supprimer");
            btnSupprimer.setOnAction(event -> {
                serviceEvenement.supprimerEvenement(e.getId());
                refresh();
            });

            // Bouton pour modifier l'événement
            Button btnModifier = new Button("Modifier");
            btnModifier.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/ModifierEvenement.fxml"));
                    // Comme le nœud racine dans ModifierEvenement.fxml est un AnchorPane, on le récupère ainsi :
                    AnchorPane root = loader.load();

                    ModifierEvenementController controller = loader.getController();
                    controller.setEvenement(e);

                    Stage stage = new Stage();
                    stage.setTitle("Modifier l'événement");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            hbox.getChildren().addAll(titre, typeEvenement, lieu, nbrPlaces, dateDebut, dateFin, statut, image, btnSupprimer, btnModifier);
            hbox.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: #f9f9f9;");
            vboxEvenements.getChildren().add(hbox);
        }
    }

    @FXML
    private void handleRetour() {
        Stage stage = (Stage) vboxEvenements.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleAjouterEvenement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/AjouterEvenement.fxml"));
            VBox root = loader.load();

            AjouterEvenementController controller = loader.getController();
            controller.setOnEvenementAjoute(this::refresh);

            Stage stage = new Stage();
            stage.setTitle("Ajouter un événement");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
