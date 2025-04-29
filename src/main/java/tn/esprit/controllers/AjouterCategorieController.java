// src/main/java/tn/esprit/controllers/AjouterCategorieController.java
package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Control;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.validation.Severity;
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

public class AjouterCategorieController implements Initializable {

    @FXML private TextField txtFNom;
    @FXML private TextField txtFDescription;
    @FXML private ComboBox<String> comboStatut;
    @FXML private TextField txtFurl_image;
    @FXML private Button btnBrowseImage;

    // Labels d’erreur inline
    @FXML private Label lblNomError;
    @FXML private Label lblDescError;
    @FXML private Label lblStatutError;
    @FXML private Label lblImageError;

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

        // style des labels d’erreur
        lblNomError.setStyle("-fx-text-fill: red;");
        lblDescError.setStyle("-fx-text-fill: red;");
        lblStatutError.setStyle("-fx-text-fill: red;");
        lblImageError.setStyle("-fx-text-fill: red;");

        validationSupport = new ValidationSupport();

        Predicate<String> nomPredicate = s -> s != null && !s.trim().isEmpty() && s.length() <= 10;
        Predicate<String> descPredicate = s -> s != null && !s.trim().isEmpty() && s.length() <= 100;

        validationSupport.registerValidator(
                txtFNom,
                Validator.createPredicateValidator(nomPredicate,
                        "Le nom doit être renseigné et ≤ 10 caractères")
        );
        validationSupport.registerValidator(
                txtFDescription,
                Validator.createPredicateValidator(descPredicate,
                        "La description doit être renseignée et ≤ 15 caractères")
        );
        validationSupport.registerValidator(
                comboStatut,
                Validator.createPredicateValidator(
                        statut -> statut != null && (statut.equals("Actif") || statut.equals("Non actif")),
                        "Le statut doit être sélectionné"
                )
        );
        validationSupport.registerValidator(
                txtFurl_image,
                Validator.createEmptyValidator("L'URL de l'image est requise")
        );

        // écouteurs pour mise à jour inline
        txtFNom.textProperty().addListener((o, ov, nv) -> validateField(txtFNom, lblNomError));
        txtFDescription.textProperty().addListener((o, ov, nv) -> validateField(txtFDescription, lblDescError));
        comboStatut.valueProperty().addListener((o, ov, nv) -> validateField(comboStatut, lblStatutError));
        txtFurl_image.textProperty().addListener((o, ov, nv) -> validateField(txtFurl_image, lblImageError));

        // Parcourir image
        btnBrowseImage.setOnAction(evt -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Sélectionner une image");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File file = chooser.showOpenDialog(txtFNom.getScene().getWindow());
            if (file != null) {
                try {
                    Path imagesDir = Paths.get("src/main/resources/images");
                    if (Files.notExists(imagesDir)) Files.createDirectories(imagesDir);
                    Path target = imagesDir.resolve(file.getName());
                    Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
                    txtFurl_image.setText(target.toString());
                } catch (IOException e) {
                    new Alert(Alert.AlertType.ERROR,
                            "Impossible de copier l’image : " + e.getMessage())
                            .showAndWait();
                }
            }
        });
    }

    @FXML
    private void ajouterCategorie() {
        // valider tous les champs
        validateField(txtFNom, lblNomError);
        validateField(txtFDescription, lblDescError);
        validateField(comboStatut, lblStatutError);
        validateField(txtFurl_image, lblImageError);

        // si l’un des labels d’erreur est visible / non vide, on stoppe
        if (!lblNomError.getText().isEmpty() ||
                !lblDescError.getText().isEmpty() ||
                !lblStatutError.getText().isEmpty() ||
                !lblImageError.getText().isEmpty()) {
            return;
        }

        Categorie c = new Categorie(
                txtFNom.getText(),
                txtFDescription.getText(),
                comboStatut.getValue(),
                txtFurl_image.getText()
        );
        serviceCategorie.ajouterCategorie(c);

        if (afficherController != null) afficherController.refresh();
        ((Stage) txtFNom.getScene().getWindow()).close();
    }

    @FXML
    private void retourListe() {
        ((Stage) txtFNom.getScene().getWindow()).close();
    }

    public void setAfficherController(AfficherCategoriesController controller) {
        this.afficherController = controller;
    }

    private void validateField(Control control, Label label) {
        String msg = validationSupport
                .getValidationResult()
                .getMessages().stream()
                // on ne conserve que les messages de sévérité ERROR
                .filter(m -> m.getSeverity() == Severity.ERROR)
                // puis seulement ceux qui ciblent ce contrôle
                .filter(m -> m.getTarget() == control)
                .map(ValidationMessage::getText)
                .findFirst()
                .orElse("");
        label.setText(msg);
    }
}
