// src/main/java/tn/esprit/controllers/ModifierCategorieController.java
package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import tn.esprit.entities.Categorie;
import tn.esprit.services.ServiceCategorie;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class ModifierCategorieController implements Initializable {

    @FXML private TextField txtNom;
    @FXML private TextField txtDescription;
    @FXML private TextField txtStatut;
    @FXML private TextField txtUrlImage;
    @FXML private Button btnBrowseImage;

    // Labels d’erreur inline
    @FXML private Label lblNomError;
    @FXML private Label lblDescriptionError;
    @FXML private Label lblStatutError;
    @FXML private Label lblImageError;

    private Categorie categorieToUpdate;
    private AfficherCategoriesController afficherController;
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
        // style des labels d’erreur
        lblNomError.setStyle("-fx-text-fill: red;");
        lblDescriptionError.setStyle("-fx-text-fill: red;");
        lblStatutError.setStyle("-fx-text-fill: red;");
        lblImageError.setStyle("-fx-text-fill: red;");

        validationSupport = new ValidationSupport();

        Predicate<String> nomPredicate = s -> s != null && !s.trim().isEmpty() && s.length() <= 10;
        Predicate<String> descPredicate = s -> s != null && !s.trim().isEmpty() && s.length() <= 100;
        Predicate<String> statutPredicate = s -> s != null && !s.trim().isEmpty();
        Predicate<String> urlPredicate = s -> s != null && !s.trim().isEmpty();

        validationSupport.registerValidator(
                txtNom,
                Validator.createPredicateValidator(nomPredicate,
                        "Le nom doit être renseigné et ≤ 10 caractères")
        );
        validationSupport.registerValidator(
                txtDescription,
                Validator.createPredicateValidator(descPredicate,
                        "La description doit être renseignée et ≤ 15 caractères")
        );
        validationSupport.registerValidator(
                txtStatut,
                Validator.createPredicateValidator(statutPredicate,
                        "Le statut est requis")
        );
        validationSupport.registerValidator(
                txtUrlImage,
                Validator.createPredicateValidator(urlPredicate,
                        "L'image est requise")
        );

        // écouteurs pour mise à jour inline
        txtNom.textProperty().addListener((obs, oldV, newV) -> validateField(txtNom, lblNomError));
        txtDescription.textProperty().addListener((obs, oldV, newV) -> validateField(txtDescription, lblDescriptionError));
        txtStatut.textProperty().addListener((obs, oldV, newV) -> validateField(txtStatut, lblStatutError));
        txtUrlImage.textProperty().addListener((obs, oldV, newV) -> validateField(txtUrlImage, lblImageError));

        // Handler du bouton Parcourir
        btnBrowseImage.setOnAction(evt -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Sélectionner une image");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File file = chooser.showOpenDialog(txtNom.getScene().getWindow());
            if (file != null) {
                try {
                    Path imagesDir = Paths.get("src/main/resources/images");
                    if (Files.notExists(imagesDir)) {
                        Files.createDirectories(imagesDir);
                    }
                    Path target = imagesDir.resolve(file.getName());
                    Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
                    txtUrlImage.setText(target.toString());
                } catch (IOException e) {
                    // on conserve l’Alert pour erreur de copie, c’est autre chose
                    new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.ERROR,
                            "Impossible de copier l’image : " + e.getMessage()
                    ).showAndWait();
                }
            }
        });
    }

    public void initData(Categorie c) {
        this.categorieToUpdate = c;
        txtNom.setText(c.getNom());
        txtDescription.setText(c.getDescription());
        txtStatut.setText(c.getStatut());
        txtUrlImage.setText(c.getUrl_image());
    }

    public void setAfficherController(AfficherCategoriesController controller) {
        this.afficherController = controller;
    }

    @FXML
    private void enregistrerModification() {
        // mise à jour des messages inline
        validateField(txtNom, lblNomError);
        validateField(txtDescription, lblDescriptionError);
        validateField(txtStatut, lblStatutError);
        validateField(txtUrlImage, lblImageError);

        // si un seul contrôle est invalide, on stoppe
        if (validationSupport.isInvalid()) {
            return;
        }

        // mise à jour de l’entité
        categorieToUpdate.setNom(txtNom.getText());
        categorieToUpdate.setDescription(txtDescription.getText());
        categorieToUpdate.setStatut(txtStatut.getText());
        categorieToUpdate.setUrl_image(txtUrlImage.getText());

        try {
            serviceCategorie.updateCategorie(categorieToUpdate);
            if (afficherController != null) {
                afficherController.refresh();
            }
            ((Stage) txtNom.getScene().getWindow()).close();
        } catch (SQLException ex) {
            new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR,
                    "Erreur lors de la mise à jour en base : " + ex.getMessage()
            ).showAndWait();
        }
    }

    private void validateField(Control control, Label label) {
        String msg = validationSupport.getValidationResult().getMessages().stream()
                .filter(m -> m.getTarget() == control)
                .map(ValidationMessage::getText)
                .findFirst()
                .orElse("");
        label.setText(msg);
    }

}
