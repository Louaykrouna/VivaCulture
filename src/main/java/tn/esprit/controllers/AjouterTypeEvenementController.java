package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import tn.esprit.entities.TypeEvenement;
import tn.esprit.services.ServiceCategorie;
import tn.esprit.services.ServiceTypeEvenement;

import java.sql.SQLException;
import java.util.List;

public class AjouterTypeEvenementController {

    @FXML
    private ComboBox<String> comboBoxCategorie;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtUrlImage;

    @FXML
    private TextField txtDescription;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnRetour;

    private final ServiceTypeEvenement serviceTypeEvenement;
    private final ServiceCategorie serviceCategorie;

    private AfficherTypeEvenementController afficherController;

    private ValidationSupport validationSupport;

    public AjouterTypeEvenementController() {
        try {
            serviceTypeEvenement = new ServiceTypeEvenement();
            serviceCategorie = new ServiceCategorie();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setAfficherController(AfficherTypeEvenementController afficherController) {
        this.afficherController = afficherController;
    }

    @FXML
    public void initialize() {
        List<String> categories = serviceCategorie.afficherCategoriesNoms();
        comboBoxCategorie.getItems().addAll(categories);
        setupValidation();
        validationSupport.initInitialDecoration(); // Affiche les erreurs dès le démarrage
    }

    private void setupValidation() {
        validationSupport = new ValidationSupport();

        validationSupport.registerValidator(
                txtNom, true, Validator.createEmptyValidator("⚠ Le nom est requis"));

        validationSupport.registerValidator(
                txtUrlImage, true, Validator.createPredicateValidator(
                        url -> url != null && url.toString().matches("https?://.+"),
                        "⚠ L'URL doit être valide (commence par http:// ou https://)"));

        validationSupport.registerValidator(
                txtDescription, true, Validator.createEmptyValidator("⚠ La description est requise"));

        validationSupport.registerValidator(
                comboBoxCategorie, true, Validator.createEmptyValidator("⚠ La catégorie est requise"));
    }

    @FXML
    private void handleAjouter() {
        // 🛠 Forcer l'affichage des erreurs de tous les champs
        validationSupport.redecorate();

        if (validationSupport.isInvalid()) {
            StringBuilder errors = new StringBuilder();
            validationSupport.getValidationResult().getMessages().forEach(msg ->
                    errors.append("• ").append(msg.getText()).append("\n")
            );

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreurs de validation");
            alert.setHeaderText("Veuillez corriger les erreurs suivantes :");
            alert.setContentText(errors.toString());
            alert.showAndWait();
            return;
        }

        String categorieNom = comboBoxCategorie.getValue();
        String nom = txtNom.getText();
        String urlImage = txtUrlImage.getText();
        String description = txtDescription.getText();

        int categorieId = serviceCategorie.getCategorieIdByNom(categorieNom);
        TypeEvenement type = new TypeEvenement(categorieId, nom, urlImage, description);

        serviceTypeEvenement.ajouterTypeEvenement(type);

        if (afficherController != null) {
            afficherController.refresh();
        }

        handleRetour();
    }

    @FXML
    private void handleRetour() {
        Stage stage = (Stage) btnRetour.getScene().getWindow();
        stage.close();
    }
}
