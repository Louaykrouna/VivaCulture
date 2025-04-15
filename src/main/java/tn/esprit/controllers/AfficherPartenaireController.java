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
import tn.esprit.entities.Partenaire;
import tn.esprit.services.PartenaireService;

import java.sql.SQLException;

public class AfficherPartenaireController {

    @FXML
    private ListView<Partenaire> lvPartenaire;



    @FXML
    public void initialize() {
        chargerDonnees();
        Platform.runLater(() -> {
            if (lvPartenaire.getScene() != null) {
                lvPartenaire.getScene().getStylesheets()
                        .add(getClass().getResource("/styles.css").toExternalForm());
            }
        });
    }

    private void chargerDonnees() {
        try {
            PartenaireService service = new PartenaireService();
            ObservableList<Partenaire> data = FXCollections.observableArrayList(service.afficher());

            lvPartenaire.setItems(data);

            lvPartenaire.setCellFactory(new Callback<>() {
                @Override
                public ListCell<Partenaire> call(ListView<Partenaire> param) {
                    return new ListCell<>() {
                        final HBox hbox = new HBox();
                        final VBox infoBox = new VBox();
                        final Label idLabel = new Label();
                        final Label nomLabel = new Label();
                        final Label emailLabel = new Label();
                        final Label phoneLabel = new Label();
                        final Label typeLabel = new Label();
                        final Label collabIdLabel = new Label();
                        final Button modifyBtn = new Button("Modifier");
                        final Button deleteBtn = new Button("Supprimer");
                        final HBox buttonBox = new HBox(5, modifyBtn, deleteBtn);

                        {
                            infoBox.getChildren().addAll(idLabel, nomLabel, emailLabel, phoneLabel, typeLabel, collabIdLabel);
                            hbox.getChildren().addAll(infoBox, buttonBox);
                            hbox.setSpacing(10);
                            HBox.setHgrow(infoBox, Priority.ALWAYS);

                            modifyBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                            deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                            modifyBtn.setOnAction(event -> {
                                Partenaire p = getItem();
                                if (p != null) {
                                    modifierPartenaire(p);
                                }
                            });

                            deleteBtn.setOnAction(event -> {
                                Partenaire p = getItem();
                                if (p != null) {
                                    supprimerPartenaire(p);
                                }
                            });
                        }

                        @Override
                        protected void updateItem(Partenaire item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setGraphic(null);
                            } else {
                                idLabel.setText("ID: " + item.getIdP());
                                nomLabel.setText("Nom: " + item.getNomP());
                                emailLabel.setText("Email: " + item.getEmailP());
                                phoneLabel.setText("Téléphone: " + item.getTelephone());
                                typeLabel.setText("Type: " + item.getTypeP());
                                collabIdLabel.setText("ID Collaboration: " + item.getcollaboration_id());
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

    private void supprimerPartenaire(Partenaire partenaire) {
        try {
            PartenaireService service = new PartenaireService();
            service.supprimer(partenaire);

            lvPartenaire.getItems().remove(partenaire);
            showAlert("Succès", "Partenaire supprimé avec succès", Alert.AlertType.INFORMATION);

        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la suppression: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void modifierPartenaire(Partenaire partenaire) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierPartenaire.fxml"));
            Parent root = loader.load();

            ModifierPartenaireController controller = loader.getController();
            controller.setPartenaire(partenaire);

            Stage stage = new Stage();
            stage.setTitle("Modifier Partenaire");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            chargerDonnees();

        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void ajouterPartenaire(ActionEvent actionEvent) {
        try {
            Stage currentStage = (Stage) lvPartenaire.getScene().getWindow();
            currentStage.close();

            // Load the "AjouterPartenaire.fxml" view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPartenaire.fxml"));
            Parent root = loader.load();

            // Open the new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Partenaire");
            stage.show();

        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir l'interface d'ajout", Alert.AlertType.ERROR);
        }
    }

    public void refreshList() {
        chargerDonnees();
    }
}
