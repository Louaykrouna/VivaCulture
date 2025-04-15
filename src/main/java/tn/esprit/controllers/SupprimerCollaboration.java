package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import tn.esprit.entities.Collaboration;
import tn.esprit.services.CollaborationService;

import java.sql.SQLException;

public class SupprimerCollaboration {

    @FXML
    private TableView<Collaboration> tableView;

    @FXML
    private TableColumn<Collaboration, String> colNom;

    @FXML
    private TableColumn<Collaboration, String> colType;

    @FXML
    private TableColumn<Collaboration, String> colStatus;

    @FXML
    private TableColumn<Collaboration, String> colDateSignature;

    @FXML
    private TableColumn<Collaboration, String> colDateExpiration;

    @FXML
    private Button deleteButton;

    private final CollaborationService service = new CollaborationService();

    @FXML
    private void initialize() throws SQLException {
        // Link table columns with entity properties
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomC"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDateSignature.setCellValueFactory(new PropertyValueFactory<>("date_sig"));
        colDateExpiration.setCellValueFactory(new PropertyValueFactory<>("date_ex"));

        // Load data into table (assuming getAll returns a List<Collaboration>)
        tableView.getItems().setAll(service.afficher());
    }



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
