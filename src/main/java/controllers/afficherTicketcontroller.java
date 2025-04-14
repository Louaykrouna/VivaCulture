package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.entities.Ticket;
import tn.esprit.service.ServiceTicket;

import java.io.IOException;
import java.sql.SQLException;

public class afficherTicketcontroller {

    @FXML
    private ListView<Ticket> listTickets;

    // Méthode d'initialisation pour charger les tickets au démarrage
    public void initialize() {
        loadTickets();
    }

    // Chargement des tickets depuis la base de données
    private void loadTickets() {
        ServiceTicket serviceTicket = new ServiceTicket();
        try {
            listTickets.getItems().setAll(serviceTicket.afficher());
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Personnalisation des cellules de la ListView
        listTickets.setCellFactory(param -> new ListCell<Ticket>() {
            @Override
            protected void updateItem(Ticket ticket, boolean empty) {
                super.updateItem(ticket, empty);
                if (empty || ticket == null) {
                    setGraphic(null);
                } else {
                    // Création d'un HBox pour afficher les informations et boutons du ticket
                    HBox hbox = new HBox(10);
                    VBox vbox = new VBox(5);

                    // Affichage du nom, de l'email et du prix
                    vbox.getChildren().addAll(
                            new Label(ticket.getNom() + " " + ticket.getPrenom()),
                            new Label(ticket.getEmail()),
                            new Label(ticket.getPrix() + " DT")
                    );

                    // Boutons Modifier, Supprimer et Ajouter Réservation
                    Button btnModifier = new Button("Modifier");
                    Button btnSupprimer = new Button("Supprimer");

                    // Action pour la suppression
                    btnSupprimer.setOnAction(e -> handleSupprimer(ticket));
                    // Action pour la modification
                    btnModifier.setOnAction(e -> handleModifier(ticket));
                    hbox.getChildren().addAll(vbox, btnModifier, btnSupprimer);
                    setGraphic(hbox);
                }
            }
        });
    }

    // Suppression d'un ticket
    private void handleSupprimer(Ticket ticket) {
        try {
            ServiceTicket serviceTicket = new ServiceTicket();
            serviceTicket.supprimer(ticket.getId());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Suppression");
            alert.setContentText("Ticket supprimé !");
            alert.show();

            listTickets.getItems().remove(ticket);
            listTickets.refresh();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur : " + e.getMessage());
            alert.show();
        }
    }

    // Modification d'un ticket
    private void handleModifier(Ticket ticket) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierticket.fxml"));
            Parent root = loader.load();

            modifierticketcontroller controller = loader.getController();
            controller.setTicket(ticket);

            Stage stage = new Stage();
            stage.setTitle("Modifier le ticket");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadTickets();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Impossible d'ouvrir la fenêtre de modification.");
            alert.showAndWait();
        }
    }


}
