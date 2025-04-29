// src/main/java/tn/esprit/controllers/AjouterTypeEvenementController.java
package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import tn.esprit.entities.TypeEvenement;
import tn.esprit.services.ServiceCategorie;
import tn.esprit.services.ServiceTypeEvenement;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;

public class AjouterTypeEvenementController {

    @FXML private ComboBox<String> comboBoxCategorie;
    @FXML private TextField txtNom;
    @FXML private TextField txtUrlImage;
    @FXML private TextField txtDescription;
    @FXML private Button btnBrowseImage;
    @FXML private Button btnAjouter;
    @FXML private Button btnRetour;

    // Labels d’erreur inline
    @FXML private Label lblCategorieError;
    @FXML private Label lblNomError;
    @FXML private Label lblImageError;
    @FXML private Label lblDescriptionError;

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
        // peupler la combo
        List<String> categories = serviceCategorie.afficherCategoriesNoms();
        comboBoxCategorie.getItems().addAll(categories);

        // style et hide par défaut
        String red = "-fx-text-fill: red;";
        lblCategorieError.setStyle(red);
        lblNomError.setStyle(red);
        lblImageError.setStyle(red);
        lblDescriptionError.setStyle(red);

        lblCategorieError.setVisible(false); lblCategorieError.setManaged(false);
        lblNomError.setVisible(false);      lblNomError.setManaged(false);
        lblImageError.setVisible(false);    lblImageError.setManaged(false);
        lblDescriptionError.setVisible(false); lblDescriptionError.setManaged(false);

        setupValidation();

        // écouteurs pour update inline
        comboBoxCategorie.valueProperty().addListener((o,ov,nv) -> validateField(comboBoxCategorie, lblCategorieError));
        txtNom.textProperty().addListener((o,ov,nv)            -> validateField(txtNom, lblNomError));
        txtUrlImage.textProperty().addListener((o,ov,nv)       -> validateField(txtUrlImage, lblImageError));
        txtDescription.textProperty().addListener((o,ov,nv)    -> validateField(txtDescription, lblDescriptionError));

        // bouton Parcourir
        btnBrowseImage.setOnAction(evt -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Sélectionner une image");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.jpeg","*.gif"));
            File file = chooser.showOpenDialog(comboBoxCategorie.getScene().getWindow());
            if (file != null) {
                try {
                    Path imagesDir = Paths.get("src/main/resources/images");
                    if (Files.notExists(imagesDir)) Files.createDirectories(imagesDir);
                    Path target = imagesDir.resolve(file.getName());
                    Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
                    txtUrlImage.setText(target.toString());
                } catch (IOException e) {
                    new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.ERROR,
                            "Impossible de copier l’image : " + e.getMessage()
                    ).showAndWait();
                }
            }
        });
    }

    private void setupValidation() {
        validationSupport = new ValidationSupport();
        Predicate<String> nonVide = s -> s != null && !s.trim().isEmpty();

        validationSupport.registerValidator(
                comboBoxCategorie, true,
                Validator.createPredicateValidator(
                        s -> nonVide.test((String)s),
                        "⚠ La catégorie est requise"
                )
        );
        validationSupport.registerValidator(
                txtNom, true,
                Validator.createPredicateValidator(
                        nonVide,
                        "⚠ Le nom est requis"
                )
        );
        validationSupport.registerValidator(
                txtUrlImage, true,
                Validator.createPredicateValidator(
                        nonVide,
                        "⚠ L'URL de l'image est requise"
                )
        );
        validationSupport.registerValidator(
                txtDescription, true,
                Validator.createPredicateValidator(
                        nonVide,
                        "⚠ La description est requise"
                )
        );
    }

    @FXML
    private void handleAjouter() {
        // forcer mise à jour
        validateField(comboBoxCategorie, lblCategorieError);
        validateField(txtNom, lblNomError);
        validateField(txtUrlImage, lblImageError);
        validateField(txtDescription, lblDescriptionError);

        // si invalide, on stoppe
        if (validationSupport.isInvalid()) {
            return;
        }

        String catNom = comboBoxCategorie.getValue();
        int catId = serviceCategorie.getCategorieIdByNom(catNom);
        TypeEvenement type = new TypeEvenement(
                catId,
                txtNom.getText(),
                txtUrlImage.getText(),
                txtDescription.getText()
        );
        serviceTypeEvenement.ajouterTypeEvenement(type);
        if (afficherController != null) afficherController.refresh();
        handleRetour();
    }

    @FXML
    private void handleRetour() {
        Stage stage = (Stage) btnRetour.getScene().getWindow();
        stage.close();
    }

    private void validateField(Control control, Label label) {
        String msg = validationSupport.getValidationResult().getMessages().stream()
                .filter(m -> m.getTarget() == control)
                .map(ValidationMessage::getText)
                .findFirst()
                .orElse("");
        label.setText(msg);
        boolean erreur = !msg.isEmpty();
        label.setVisible(erreur);
        label.setManaged(erreur);
    }
}
