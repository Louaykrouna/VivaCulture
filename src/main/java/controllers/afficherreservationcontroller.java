package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tn.esprit.entities.reservation;
import tn.esprit.service.Servicereservation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class afficherreservationcontroller {

    @FXML
    private ListView<String> listViewReservation;

    @FXML
    private Button btnRetour;  // Bouton "Retour" défini dans le FXML

    private final Servicereservation service = new Servicereservation();
    public static final Map<String, Integer> listOfReservation = new HashMap<>();

    @FXML
    public void initialize() {
        System.out.println("INITIALIZE LISTVIEW CALLED");
        try {
            List<reservation> reservations = service.afficher();
            ObservableList<String> observableList = FXCollections.observableArrayList();

            for (reservation r : reservations) {
                String reservationInfo = "Événement: " + r.getNom_evenement() +
                        ", Tickets: " + r.getNombre_tickets() +
                        ", Date: " + r.getDate_reservation() +
                        ", Heure: " + r.getHeure() +
                        ", Email: " + r.getEmail();

                listOfReservation.put(reservationInfo, r.getId());
                observableList.add(reservationInfo);
            }

            listViewReservation.setItems(observableList);

            listViewReservation.setCellFactory(param -> new ListCell<>() {
                private final VBox cardBox = new VBox();
                private final Text titre = new Text();
                private final Text infos = new Text();
                private final HBox buttonBox = new HBox();
                private final Button modifyButton = new Button("Modifier");
                private final Button deleteButton = new Button("Supprimer");

                {
                    cardBox.setSpacing(8);
                    cardBox.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-radius: 10; " +
                            "-fx-border-color: #dddddd; -fx-padding: 15; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);");

                    titre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                    infos.setStyle("-fx-font-size: 12px;");

                    buttonBox.setSpacing(10);
                    modifyButton.setStyle("-fx-background-color: #ffc107; -fx-text-fill: white; -fx-background-radius: 5;");
                    deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-background-radius: 5;");

                    buttonBox.getChildren().addAll(modifyButton, deleteButton);
                    cardBox.getChildren().addAll(titre, infos, buttonBox);

                    deleteButton.setOnAction(event -> {
                        String item = getItem();
                        getListView().getItems().remove(item);
                        try {
                            service.deteteReservationAndTicket(listOfReservation.get(item));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    modifyButton.setOnAction(event -> {
                        String item = getItem();
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/deleteOrmodifyReservation.fxml"));
                            Stage stage = new Stage(StageStyle.DECORATED);
                            stage.setScene(new Scene(loader.load()));
                            deleteOrmodifyReservationcontroller deletecontroller = loader.getController();
                            deletecontroller.setData(item);
                            stage.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        String[] parts = item.split(",", 2);
                        titre.setText(parts[0]);
                        infos.setText(parts.length > 1 ? parts[1].trim() : "");
                        setGraphic(cardBox);
                    }
                }
            });

            // Optionnel : vous pouvez aussi définir une action lors de la sélection de l'élément
            listViewReservation.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (newValue == null) return;
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/deleteOrmodifyReservation.fxml"));
                        Stage stage = new Stage(StageStyle.DECORATED);
                        stage.setScene(new Scene(loader.load()));
                        deleteOrmodifyReservationcontroller deletecontroller = loader.getController();
                        deletecontroller.setData(newValue);
                        stage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération des réservations: " + e.getMessage());
        }
    }

    // Méthode pour le bouton Retour qui charge l'interface Ajouterreservation.fxml
    @FXML
    private void handleRetour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ajouterreservation.fxml"));
            Parent parent = loader.load();
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setScene(new Scene(parent));
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Impossible de charger l'interface Ajouterreservation: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
