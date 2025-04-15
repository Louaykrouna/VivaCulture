package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import tn.esprit.entities.Collaboration;
import tn.esprit.entities.Partenaire;
import tn.esprit.services.CollaborationService;
import tn.esprit.services.PartenaireService;

import java.sql.SQLException;
import java.util.List;

public class AjouterPartenaireController {

    @FXML
    private TextField idPField;
    @FXML
    private TextField nomPField;
    @FXML
    private TextField emailPField;
    @FXML
    private TextField telephoneField;
    @FXML
    private TextField typePField;
    @FXML
    private ComboBox<Integer> combofield;

    private final PartenaireService partenaireService = new PartenaireService();
    private final CollaborationService collaborationService = new CollaborationService();

    @FXML
    public void ajouterPartenaire() {
        try {
            Integer selectedcollaboration = combofield.getValue();
            String idP = idPField.getText();
            String nomP = nomPField.getText();
            String emailP = emailPField.getText();
            String telephone = telephoneField.getText();
            String typeP = typePField.getText();

            // === Validation ===

            // Vérifier que l'ID est positif
            if (idP == null || idP.trim().isEmpty()) {
                showAlert("Erreur", "L'ID du partenaire ne peut pas être vide !");
                return;
            }

            try {
                int id = Integer.parseInt(idP);
                if (id <= 0) {
                    showAlert("Erreur", "L'ID doit être un nombre positif !");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Erreur", "L'ID doit être un nombre valide !");
                return;
            }

            // Vérification du nom
            if (nomP == null || nomP.trim().isEmpty()) {
                showAlert("Erreur", "Le nom du partenaire ne peut pas être vide !");
                return;
            }

            // Vérification de l'email
            if (emailP == null || emailP.trim().isEmpty()) {
                showAlert("Erreur", "L'email du partenaire ne peut pas être vide !");
                return;
            }



            // Vérifier que l'email contient un '@'
            if (!emailP.contains("@")) {
                showAlert("Erreur", "L'email doit contenir un caractère '@' !");
                return;
            }

            // Vérification de l'existence de l'email
            if (partenaireService.existsByEmail(emailP)) {
                showAlert("Erreur", "Un partenaire avec cet email existe déjà !");
                return;
            }

            // Vérification du téléphone
            if (telephone == null || telephone.trim().isEmpty()) {
                showAlert("Erreur", "Le numéro de téléphone ne peut pas être vide !");
                return;
            }

            if (!telephone.matches("\\d{8}")) {
                showAlert("Erreur", "Le numéro de téléphone est invalide ! Il doit contenir exactement 8 chiffres.");
                return;
            }

            // Vérification du type
            if (typeP == null || typeP.trim().isEmpty()) {
                showAlert("Erreur", "Le type de partenaire ne peut pas être vide !");
                return;
            }

            List<String> typesValid = List.of("sponsor", "organisme", "association");
            if (!typesValid.contains(typeP.toLowerCase())) {
                showAlert("Erreur", "Type de partenaire invalide ! Valeurs possibles : sponsor, organisme, association.");
                return;
            }

            // === Ajout ===
            Partenaire partenaire = new Partenaire();
            partenaire.setcollaboration_id(selectedcollaboration);
            partenaire.setNomP(nomP);
            partenaire.setEmailP(emailP);
            partenaire.setTelephone(telephone);
            partenaire.setTypeP(typeP);

            partenaireService.ajouter(partenaire);

            showAlert("Succès", "✅ Partenaire ajouté avec succès !");
            clearFields();
            navigateToAfficherPartenaire();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "⚠️ Une erreur SQL est survenue : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "⚠️ Une erreur est survenue : " + e.getMessage());
        }
    }

    private void clearFields() {
        idPField.clear();
        nomPField.clear();
        emailPField.clear();
        telephoneField.clear();
        typePField.clear();

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private void navigateToAfficherPartenaire() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherpartenaire.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) idPField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur de navigation", "⚠️ Impossible d'ouvrir la page de liste des partenaires.");
        }
    }
    private void chargerCollaboration() throws SQLException {
        List<Collaboration> collaborations = collaborationService.afficher();

        // Extraire uniquement les IDs
        ObservableList<Integer> ids = FXCollections.observableArrayList();
        for (Collaboration c : collaborations) {
            ids.add(c.getId());
        }

        combofield.setItems(ids);
    }

    @FXML
    public void initialize() {
        try {
            chargerCollaboration();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les partenaires.");
        }
    }


}
