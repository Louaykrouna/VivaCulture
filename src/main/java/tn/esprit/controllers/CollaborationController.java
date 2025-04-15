package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import tn.esprit.entities.Collaboration;
import tn.esprit.services.CollaborationService;

import java.sql.SQLException;
import java.time.LocalDate;

public class CollaborationController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField statusField;

    @FXML
    private TextField dateSigField;

    @FXML
    private TextField dateExpField;

    @FXML
    private Button themeToggleBtn;

    private final CollaborationService service = new CollaborationService();

    @FXML
    public void ajouterCollaboration() {
        try {
            String nom = nomField.getText();
            String type = typeField.getText();
            String status = statusField.getText().toLowerCase().trim();
            String dateSigText = dateSigField.getText();
            String dateExpText = dateExpField.getText();

            // === Vérification des champs vides ===
            if (nom == null || nom.trim().isEmpty()) {
                showAlert("Erreur", "Le nom ne peut pas être vide.");
                return;
            }

            if (type == null || type.trim().isEmpty()) {
                showAlert("Erreur", "Le type ne peut pas être vide.");
                return;
            }

            if (status == null || status.trim().isEmpty()) {
                showAlert("Erreur", "Le statut ne peut pas être vide.");
                return;
            }

            // === Statut autorisé ===
            if (!status.equals("active") && !status.equals("expirer")) {
                showAlert("Erreur", "Le statut doit être soit 'active' soit 'expirer'.");
                return;
            }

            // === Dates ===
            if (dateSigText == null || dateSigText.trim().isEmpty()) {
                showAlert("Erreur", "La date de signature est requise.");
                return;
            }

            if (dateExpText == null || dateExpText.trim().isEmpty()) {
                showAlert("Erreur", "La date d'expiration est requise.");
                return;
            }

            LocalDate dateSig;
            LocalDate dateExp;
            try {
                dateSig = LocalDate.parse(dateSigText);
                dateExp = LocalDate.parse(dateExpText);
            } catch (Exception e) {
                showAlert("Erreur", "Les dates doivent être au format : yyyy-MM-dd");
                return;
            }

            // === Comparaison des dates ===
            if (!dateExp.isAfter(dateSig)) {
                showAlert("Erreur", "La date d'expiration doit être postérieure à la date de signature.");
                return;
            }

            // === Ajout Collaboration ===
            Collaboration collaboration = new Collaboration(nom, type, dateSig, dateExp, status);
            service.ajouter(collaboration);

            showAlert("Succès", "✅ Collaboration ajoutée avec succès !");
            clearFields();
            navigateToaffichercollaboration();

        } catch (SQLException e) {
            showAlert("Erreur SQL", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "⚠️ Une erreur inattendue est survenue.");
        }
    }

    private void clearFields() {
        nomField.clear();
        typeField.clear();
        statusField.clear();
        dateSigField.clear();
        dateExpField.clear();
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private void navigateToaffichercollaboration() {
        try {
            // Load the 'affichercollaboration.fxml' page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/affichercollaboration.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) nomField.getScene().getWindow();

            // Set the new scene to display the list of collaborations
            stage.setScene(new Scene(root));

            // Force a refresh of the TableView
            AfficherCollaborationController controller = loader.getController();
            controller.refreshTable();  // Call the refresh method to refresh the table

            // Show the new scene
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while switching scenes.");
        }
    }


}



