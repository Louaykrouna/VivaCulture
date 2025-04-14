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

public class ModifierCategorieController implements Initializable {

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtStatut;
    @FXML
    private TextField txtUrlImage;

    private Categorie categorieToUpdate;
    private AfficherCategoriesController afficherController; // référence vers le contrôleur parent

    private final ServiceCategorie serviceCategorie;
    private ValidationSupport validationSupport;

    public ModifierCategorieController() {
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
        validationSupport.registerValidator(txtNom, Validator.createPredicateValidator(
                (String s) -> s != null && !s.isEmpty() && s.length() <= 10,
                "Le nom doit être renseigné et contenir au maximum 10 caractères"));

        // Contrôle sur la description : obligatoire et maximum 15 caractères.
        validationSupport.registerValidator(txtDescription, Validator.createPredicateValidator(
                (String s) -> s != null && !s.isEmpty() && s.length() <= 15,
                "La description doit être renseignée et contenir au maximum 15 caractères"));

        // Contrôle sur le statut : obligatoire.
        validationSupport.registerValidator(txtStatut, Validator.createEmptyValidator("Le statut est requis"));

        // Contrôle sur l'URL de l'image : obligatoire.
        validationSupport.registerValidator(txtUrlImage, Validator.createEmptyValidator("L'URL de l'image est requise"));
    }

    /**
     * Cette méthode permet d'initialiser le contrôleur avec les données existantes.
     */
    public void initData(Categorie c) {
        this.categorieToUpdate = c;

        txtNom.setText(c.getNom());
        txtDescription.setText(c.getDescription());
        txtStatut.setText(c.getStatut());
        txtUrlImage.setText(c.getUrl_image());
    }

    /**
     * Setter pour recevoir le contrôleur parent (affichage de la liste)
     */
    public void setAfficherController(AfficherCategoriesController controller) {
        this.afficherController = controller;
    }

    @FXML
    private void enregistrerModification() {
        // Vérifier si les champs respectent les contraintes
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

        // Mettre à jour l'objet catégorie avec les données saisies
        categorieToUpdate.setNom(txtNom.getText());
        categorieToUpdate.setDescription(txtDescription.getText());
        categorieToUpdate.setStatut(txtStatut.getText());
        categorieToUpdate.setUrl_image(txtUrlImage.getText());

        // Effectuer la mise à jour en base
        serviceCategorie.updateCategorie(categorieToUpdate);

        // Rafraîchir l'affichage de la liste si le contrôleur parent est présent
        if (afficherController != null) {
            afficherController.refresh();
        }

        // Fermer la fenêtre de modification
        Stage stage = (Stage) txtNom.getScene().getWindow();
        stage.close();
    }
}
