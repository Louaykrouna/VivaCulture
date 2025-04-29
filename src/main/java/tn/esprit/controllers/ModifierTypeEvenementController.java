package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import tn.esprit.entities.TypeEvenement;
import tn.esprit.services.ServiceCategorie;
import tn.esprit.services.ServiceTypeEvenement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;

public class ModifierTypeEvenementController {

    @FXML
    private ComboBox<String> comboBoxCategorie;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtUrlImage;

    @FXML
    private TextField txtDescription;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnRetour;

    private final ServiceTypeEvenement serviceTypeEvenement;
    private final ServiceCategorie serviceCategorie;

    private TypeEvenement typeEvenement;
    private AfficherTypeEvenementController afficherController;

    // Pour la validation ControlsFX
    private ValidationSupport validationSupport;

    @FXML
    private Button btnBrowseImage;

    public ModifierTypeEvenementController() {
        try {
            serviceTypeEvenement = new ServiceTypeEvenement();
            serviceCategorie = new ServiceCategorie();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cette méthode est appelée automatiquement après le chargement du FXML.
     * On initialise ici le système de validation.
     */
    @FXML
    public void initialize() {
        setupValidation();
        // Affiche immédiatement les décorations d’erreur sur les champs vides
        validationSupport.initInitialDecoration();
        // ------------ ajouté ------------
        btnBrowseImage.setOnAction(evt -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Sélectionner une image");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File file = chooser.showOpenDialog(comboBoxCategorie.getScene().getWindow());
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
                    new Alert(Alert.AlertType.ERROR,
                            "Impossible de copier l’image : " + e.getMessage())
                            .showAndWait();
                }
            }
        });
    }

    /**
     * Injecte le controller parent pour pouvoir rafraîchir la liste après modification.
     */
    public void setAfficherController(AfficherTypeEvenementController controller) {
        this.afficherController = controller;
    }

    /**
     * Appelée par le code qui ouvre ce dialog pour pré-remplir les champs.
     */
    public void initData(TypeEvenement type) {
        this.typeEvenement = type;

        // Remplir la liste des catégories
        List<String> categories = serviceCategorie.afficherCategoriesNoms();
        comboBoxCategorie.getItems().setAll(categories);

        // Positionner les valeurs existantes
        comboBoxCategorie.setValue(serviceCategorie.getCategorieNomById(type.getCategorieId()));
        txtNom.setText(type.getNom());
        txtUrlImage.setText(type.getUrlImage());
        txtDescription.setText(type.getDescription());
    }

    /**
     * Configure les règles de validation pour chaque champ.
     */
    private void setupValidation() {
        validationSupport = new ValidationSupport();

        validationSupport.registerValidator(
                comboBoxCategorie,
                true,
                Validator.createEmptyValidator("⚠ La catégorie est requise")
        );

        validationSupport.registerValidator(
                txtNom,
                true,
                Validator.createEmptyValidator("⚠ Le nom est requis")
        );



        validationSupport.registerValidator(
                txtDescription,
                true,
                Validator.createEmptyValidator("⚠ La description est requise")
        );
    }

    /**
     * Gère le clic sur "Modifier" : on force la validation, on affiche un Alert en cas d’erreurs,
     * sinon on met à jour l’objet et la base.
     */
    @FXML
    private void handleModifier() {
        // Force l'affichage des décorations d'erreur
        validationSupport.redecorate();

        if (validationSupport.isInvalid()) {
            // Collecte et affiche toutes les erreurs dans une Alert
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

        // Si tout est valide, on met à jour l'objet TypeEvenement
        String categorieNom = comboBoxCategorie.getValue();
        int categorieId = serviceCategorie.getCategorieIdByNom(categorieNom);

        typeEvenement.setCategorieId(categorieId);
        typeEvenement.setNom(txtNom.getText());
        typeEvenement.setUrlImage(txtUrlImage.getText());
        typeEvenement.setDescription(txtDescription.getText());

        serviceTypeEvenement.modifierTypeEvenement(typeEvenement);

        // Rafraîchir la liste dans le controller parent, si présent
        if (afficherController != null) {
            afficherController.actualiserListe();
        }

        handleRetour();
    }

    /** Ferme la fenêtre. */
    @FXML
    private void handleRetour() {
        Stage stage = (Stage) btnRetour.getScene().getWindow();
        stage.close();
    }
}
