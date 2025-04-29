package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.entities.TypeEvenement;
import tn.esprit.services.ServiceTypeEvenement;

import java.io.File;
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

    @FXML
    public void refresh() {
        vboxTypes.getChildren().clear();


            List<TypeEvenement> types = serviceTypeEvenement.afficherTypeEvenements();

            for (TypeEvenement t : types) {
                HBox ligne = new HBox(15);
                ligne.setAlignment(Pos.CENTER_LEFT);
                ligne.setStyle(
                        "-fx-padding: 10; " +
                                "-fx-background-color: #fffbea; " +
                                "-fx-border-color: #FFA726; " +
                                "-fx-border-width: 1.5; " +
                                "-fx-border-radius: 5; " +
                                "-fx-background-radius: 5;"
                );

                // 1) VBox infos
                VBox vboxInfos = new VBox(4);
                vboxInfos.setAlignment(Pos.TOP_LEFT);

                // Label cliquable pour le nom
                Label nom = new Label(t.getNom());
                nom.setStyle("-fx-font-size:16px; -fx-font-weight:bold; -fx-text-fill:#FFA726; -fx-underline:true; -fx-cursor:hand;");
                nom.setOnMouseEntered(evt -> nom.setStyle("-fx-font-size:16px; -fx-font-weight:bold; -fx-text-fill:red; -fx-underline:true; -fx-cursor:hand;"));
                nom.setOnMouseExited(evt -> nom.setStyle("-fx-font-size:16px; -fx-font-weight:bold; -fx-text-fill:#FFA726; -fx-underline:true; -fx-cursor:hand;"));
                nom.setOnMouseClicked(evt -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("/type_evenement/DetailsTypeEvenement.fxml")
                        );
                        Parent root = loader.load();  // évite le ClassCastException
                        DetailsTypeEvenementController ctrl = loader.getController();
                        ctrl.initData(t);
                        ctrl.setAfficherController(this);
                        Stage st = new Stage();
                        st.setScene(new Scene(root));
                        st.setTitle("Détails Type d'Événement");
                        st.show();
                    } catch (IOException ex) {
                        new Alert(Alert.AlertType.ERROR,
                                "Impossible d'ouvrir le détail : " + ex.getMessage()
                        ).showAndWait();
                    }
                });


                Label cat = new Label("📁 Catégorie : " + t.getCategorieNom());
                Label desc = new Label(t.getDescription());
                desc.setWrapText(true);
                ImageView iv = new ImageView();
                try {
                    Image img = new Image(new File(t.getUrlImage()).toURI().toString(),
                            100, 80, true, true);
                    iv.setImage(img);
                } catch (Exception ex) {
                    System.err.println("Image non chargée : " + ex.getMessage());
                }
                vboxInfos.getChildren().addAll(nom, cat, desc, iv);

                // 2) Spacer
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                // 3) VBox boutons (à droite)
                VBox vboxBtns = new VBox(8);
                vboxBtns.setAlignment(Pos.TOP_RIGHT);

                Button btnModifier = new Button("Modifier");
                btnModifier.setStyle(
                        "-fx-background-color: #FFA726; " +
                                "-fx-text-fill: white; -fx-font-weight: bold; " +
                                "-fx-pref-width:100px;"
                );
                btnModifier.setOnAction(e -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("/type_evenement/ModifierTypeEvenement.fxml")
                        );
                        Stage st = new Stage();
                        st.setScene(new Scene(loader.load()));
                        ModifierTypeEvenementController ctl = loader.getController();
                        ctl.initData(t);
                        ctl.setAfficherController(this);
                        st.setTitle("Modifier Type d'Événement");
                        st.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                Button btnSupprimer = new Button("Supprimer");
                btnSupprimer.setStyle(
                        "-fx-background-color: #FFD54F; " +
                                "-fx-text-fill: white; -fx-font-weight: bold; " +
                                "-fx-pref-width:100px;"
                );
                btnSupprimer.setOnAction(e -> {
                    serviceTypeEvenement.supprimerTypeEvenement(t.getId());
                    refresh();
                });

                vboxBtns.getChildren().addAll(btnModifier, btnSupprimer);

                // 4) Assemblage
                ligne.getChildren().addAll(vboxInfos, spacer, vboxBtns);
                vboxTypes.getChildren().add(ligne);
            }

            // (Optionnel) bouton Ajouter
            Button btnAjouter = new Button("➕ Ajouter un type d'événement");
            btnAjouter.setStyle(
                    "-fx-background-color: #FFA726; -fx-text-fill: white; " +
                            "-fx-font-weight: bold; -fx-pref-width:200px;"
            );
            btnAjouter.setOnAction(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/type_evenement/AjouterTypeEvenement.fxml")
                    );
                    Stage st = new Stage();
                    st.setScene(new Scene(loader.load()));
                    AjouterTypeEvenementController ctl = loader.getController();
                    ctl.setAfficherController(this);
                    st.setTitle("Ajouter un Type d'Événement");
                    st.show();
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
