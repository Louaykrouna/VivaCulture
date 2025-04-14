package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import tn.esprit.entities.Categorie;
import tn.esprit.services.ServiceCategorie;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AjouterCategorieController implements Initializable {

    @FXML
    private TextField txtFNom;
    @FXML
    private TextField txtFDescription;
    @FXML
    private TextField txtFStatut;
    @FXML
    private TextField txtFurl_image;

    private final ServiceCategorie serviceCategorie;
    private ValidationSupport validationSupport;
    private AfficherCategoriesController afficherController;

    public AjouterCategorieController() {
        try {
            serviceCategorie = new ServiceCategorie();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion à la base de données", e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validationSupport = new ValidationSupport();

        // Contrôle sur le nom : obligatoire et maximum 10 caractères.
        validationSupport.registerValidator(txtFNom, Validator.createPredicateValidator((String s) -> s != null && !s.isEmpty() && s.length() <= 10,
                "Le nom doit être renseigné et contenir au maximum 10 caractères"));

        // Contrôle sur la description : obligatoire et maximum 15 caractères.
        validationSupport.registerValidator(txtFDescription, Validator.createPredicateValidator(
                (String s) -> s != null && !s.isEmpty() && s.length() <= 15,
                "La description doit être renseignée et contenir au maximum 15 caractères"));

        // Contrôle sur le statut : obligatoire.
        validationSupport.registerValidator(txtFStatut, Validator.createEmptyValidator("Le statut est requis"));

        // Contrôle sur l'URL de l'image : obligatoire.
        validationSupport.registerValidator(txtFurl_image, Validator.createEmptyValidator("L'URL de l'image est requise"));
    }

    @FXML
    private void ajouterCategorie() {
        // Si la validation échoue, afficher une alerte contenant tous les messages d'erreur.
        if (validationSupport.isInvalid()) {
            ValidationResult result = validationSupport.getValidationResult();
            StringBuilder erreurs = new StringBuilder();
            for (ValidationMessage message : result.getMessages()) {
                erreurs.append("• ").append(message.getText()).append("\n");
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de validation");
            alert.setHeaderText("Veuillez corriger les erreurs suivantes :");
            alert.setContentText(erreurs.toString());
            alert.showAndWait();
            return;
        }

        String nom = txtFNom.getText();
        String description = txtFDescription.getText();
        String statut = txtFStatut.getText();
        String urlImage = txtFurl_image.getText();

        // Création de l'objet catégorie et ajout en base
        Categorie c = new Categorie(nom, description, statut, urlImage);
        serviceCategorie.ajouterCategorie(c);
        System.out.println("✅ Catégorie ajoutée !");

        txtFNom.clear();
        txtFDescription.clear();
        txtFStatut.clear();
        txtFurl_image.clear();

        // Rafraîchir l'affichage si le contrôleur liste est présent
        if (afficherController != null) {
            afficherController.refresh();
        }

        // Fermer la fenêtre
        Stage stage = (Stage) txtFNom.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void retourListe() {
        // Fermer la fenêtre d'ajout
        Stage stage = (Stage) txtFNom.getScene().getWindow();
        stage.close();
    }

    public void setAfficherController(AfficherCategoriesController controller) {
        this.afficherController = controller;
    }
}
