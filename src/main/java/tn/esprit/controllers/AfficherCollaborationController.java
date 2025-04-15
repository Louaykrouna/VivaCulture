package tn.esprit.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.esprit.entities.Collaboration;
import tn.esprit.services.CollaborationService;

import java.sql.SQLException;

public class AfficherCollaborationController {
    @FXML
    private ListView<Collaboration> lvCollaboration;

    @FXML
    public void initialize() {
        chargerDonnees();
        Platform.runLater(() -> {
            if (lvCollaboration.getScene() != null) {
                lvCollaboration.getScene().getStylesheets()
                        .add(getClass().getResource("/styles.css").toExternalForm());
            }
        });
    }

    private void chargerDonnees() {
        try {
            CollaborationService service = new CollaborationService();
            ObservableList<Collaboration> data = FXCollections.observableArrayList(service.afficher());

            lvCollaboration.setItems(data);

            lvCollaboration.setCellFactory(new Callback<>() {
                @Override
                public ListCell<Collaboration> call(ListView<Collaboration> param) {
                    return new ListCell<>() {
                        final HBox hbox = new HBox();
                        final VBox infoBox = new VBox();
                        final Label nomLabel = new Label();
                        final Label typeLabel = new Label();
                        final Label statusLabel = new Label();
                        final Label dateSigLabel = new Label();
                        final Label dateExLabel = new Label();
                        final Button modifyBtn = new Button("Modifier");
                        final Button deleteBtn = new Button("Supprimer");
                        final HBox buttonBox = new HBox(5, modifyBtn, deleteBtn);

                        {
                            infoBox.getChildren().addAll(nomLabel, typeLabel, statusLabel, dateSigLabel, dateExLabel);
                            hbox.getChildren().addAll(infoBox, buttonBox);
                            hbox.setSpacing(10);
                            HBox.setHgrow(infoBox, Priority.ALWAYS);

                            modifyBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                            deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                            modifyBtn.setOnAction(event -> {
                                Collaboration collab = getItem();
                                if (collab != null) {
                                    modifierCollaboration(collab);
                                }
                            });

                            deleteBtn.setOnAction(event -> {
                                Collaboration collab = getItem();
                                if (collab != null) {
                                    supprimerCollaboration(collab);
                                }
                            });
                        }

                        @Override
                        protected void updateItem(Collaboration item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setGraphic(null);
                            } else {
                                nomLabel.setText("Nom: " + item.getNomC());
                                typeLabel.setText("Type: " + item.getType());
                                statusLabel.setText("Statut: " + item.getStatus());
                                dateSigLabel.setText("Date Signature: " + item.getDateSig());
                                dateExLabel.setText("Date Expiration: " + item.getDateEx());
                                setGraphic(hbox);
                            }
                        }
                    };
                }
            });

        } catch (SQLException e) {
            showAlert("Erreur", "Erreur de chargement: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void supprimerCollaboration(Collaboration collab) {
        try {
            CollaborationService service = new CollaborationService();
            service.supprimer(collab);
            lvCollaboration.getItems().remove(collab);
            showAlert("Succès", "Collaboration supprimée avec succès", Alert.AlertType.INFORMATION);

        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la suppression: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void modifierCollaboration(Collaboration collab) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCollaboration.fxml"));
            Parent root = loader.load();

            ModifierCollaborationController controller = loader.getController();
            controller.setCollaboration(collab);

            Stage stage = new Stage();
            stage.setTitle("Modifier Collaboration");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            chargerDonnees();

        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void ajouterCollaboration(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addCollaboration.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Collaboration");
            stage.showAndWait();

            chargerDonnees(); // refresh list après ajout

        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir l'interface d'ajout", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void retour(ActionEvent actionEvent) {
        try {
            Stage currentStage = (Stage) lvCollaboration.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addcollaboration.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Collaboration");
            stage.show();

        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir l'interface d'ajout", Alert.AlertType.ERROR);
        }
    }

    public void refreshTable() {
        chargerDonnees();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
