package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.entities.Evenement;
import tn.esprit.services.ServiceEvenement;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import tn.esprit.utils.QRCodeUtil;

import java.io.File;
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
        vboxEvenements.setSpacing(30);
        vboxEvenements.setPadding(new Insets(20));

        // → Relire les événements depuis la base à chaque appel
        List<Evenement> evenements;

            evenements = serviceEvenement.afficherEvenements();

        for (Evenement e : evenements) {
            HBox hbox = new HBox(20);
            hbox.setPadding(new Insets(15));
            hbox.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 6;");

            // -- Titre --
            Label titre = new Label(e.getTitre());
            titre.setStyle("-fx-text-fill: blue; -fx-underline: true; -fx-cursor: hand;");
            titre.setOnMouseEntered(ev -> titre.setStyle("-fx-text-fill: red; -fx-underline: true; -fx-cursor: hand;"));
            titre.setOnMouseExited(ev -> titre.setStyle("-fx-text-fill: blue; -fx-underline: true; -fx-cursor: hand;"));
            titre.setOnMouseClicked(ev -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/DetailsEvenement.fxml"));
                    AnchorPane root = loader.load();
                    DetailsEvenementController ctrl = loader.getController();
                    ctrl.setEvenement(e);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Détails de l'événement");
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            // On laisse le titre grandir si besoin
            HBox.setHgrow(titre, Priority.ALWAYS);

            // -- Autres champs --
            Label typeEvenement = new Label("Type: " + e.getTypeEvenementNom());
            Label lieu         = new Label("Lieu: " + e.getLieu());
            Label nbrPlaces    = new Label("Places: " + e.getNombrePlaces());
            Label dateDebut    = new Label("Début: " + e.getDateDebut());
            Label dateFin      = new Label("Fin: " + e.getDateFin());
            Label statut       = new Label("Statut: " + e.getStatut());
            Label longitude       = new Label("Longitude: " + e.getLongitude());
            Label latitude       = new Label("Latitude: " + e.getLatitude());



            // On donne à ces labels une largeur fixe ou modérée
            for (Label lab : new Label[]{typeEvenement, lieu, nbrPlaces, dateDebut, dateFin, statut}) {
                lab.setPrefWidth(100);
                lab.setMaxWidth(100);
                HBox.setHgrow(lab, Priority.NEVER);
            }

            // -- Label image limité à 120px, texte à la ligne si trop long --
            ImageView imageView = new ImageView();
            try {
                // charge depuis le chemin absolu stocké
                Image img = new Image(new File(e.getUrlImage()).toURI().toString(),
                        /* requestedWidth */100,
                        /* requestedHeight */80,
                        true,  // preserveRatio
                        true   // smooth
                );
                imageView.setImage(img);
            } catch (Exception ex) {
                // si le fichier est introuvable, on peut laisser vide ou mettre un placeholder
                System.err.println("Impossible de charger l'image : " + ex.getMessage());
            }
            // → Après imageView ou à l’endroit souhaité dans le HBox
// 1) Préparer les données de l’événement

            String qrData = "type d'event :  "+ e.getTypeEvenementNom()

                    + "\nTitre :  " + e.getTitre()
                    + "\nLieu :  " + e.getLieu()
                    + "\ndate Debut :  " + e.getDateDebut()
                    + "\ndate Fin :  " + e.getDateFin()
                    + "\nNombre de Places :  "   + e.getNombrePlaces();


// 2) Générer le QR-Code
            Image qrCodeImage = QRCodeUtil.generateQRCodeImage(qrData, 100, 100);
            ImageView qrImageView = new ImageView(qrCodeImage);
            qrImageView.setFitWidth(100);
            qrImageView.setFitHeight(100);

// 3) Ajouter l’ImageView QR-Code au HBox
            hbox.getChildren().add(qrImageView);


            // -- Bouton Modifier --
            Button btnModifier = new Button("Modifier");
            btnModifier.setStyle(
                    "-fx-background-color: orange; -fx-text-fill: white; -fx-font-weight: bold; " +
                            "-fx-pref-width: 170px; -fx-pref-height: 38px; -fx-background-radius: 4;");
            btnModifier.setOnAction(ev -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/ModifierEvenement.fxml"));
                    AnchorPane root = loader.load();
                    ModifierEvenementController ctrl = loader.getController();
                    ctrl.setEvenement(e);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Modifier l'événement");
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            // -- Bouton Supprimer --
            Button btnSupprimer = new Button("Supprimer");
            btnSupprimer.setStyle(
                    "-fx-background-color: #ffeb3b; -fx-text-fill: white; -fx-font-weight: bold; " +
                            "-fx-pref-width: 170px; -fx-pref-height: 38px; -fx-background-radius: 4;");
            btnSupprimer.setOnAction(ev -> {
                serviceEvenement.supprimerEvenement(e.getId());
                refresh();
            });

            // Ajout dans l'hbox
            // … vos labels et imageView créés au-dessus …

// 1) Créer un espaceur extensible
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

// 2) Ajouter tous les nœuds : labels…, puis l’espaceur, puis les boutons
            hbox.getChildren().addAll(
                    titre,
                    typeEvenement,
                    lieu,
                    nbrPlaces,
                    dateDebut,
                    dateFin,
                    statut,
                    imageView,
                    longitude,
                    latitude,
                    spacer,          // ← pousse tout ce qui suit à droite
                    btnModifier,
                    btnSupprimer
            );

            VBox.setMargin(hbox, new Insets(10, 0, 10, 0));

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
            AjouterEvenementController ctrl = loader.getController();
            ctrl.setOnEvenementAjoute(this::refresh);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un événement");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
