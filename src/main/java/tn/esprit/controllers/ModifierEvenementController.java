package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import tn.esprit.entities.Evenement;
import tn.esprit.entities.TypeEvenement;
import tn.esprit.services.ServiceEvenement;
import tn.esprit.services.ServiceTypeEvenement;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ModifierEvenementController implements Initializable {

    @FXML private TextField tfTitre;
    @FXML private TextField tfDescription;
    @FXML private TextField tfLieu;
    @FXML private TextField tfNombrePlaces;
    @FXML private DatePicker dpDateDebut;
    @FXML private DatePicker dpDateFin;
    @FXML private TextField tfStatut;
    @FXML private TextField tfUrlImage;
    @FXML private TextField tfMail;
    @FXML private TextField tfLongitude;
    @FXML private TextField tfLatitude;
    @FXML private ComboBox<TypeEvenement> cbTypeEvenement;
    @FXML private Button btnBrowseImage;

    private Evenement evenementAModifier;
    private final ServiceEvenement serviceEvenement;
    private final ServiceTypeEvenement serviceTypeEvenement;
    private ValidationSupport validationSupport;

    public ModifierEvenementController() {
        try {
            serviceEvenement = new ServiceEvenement();
            serviceTypeEvenement = new ServiceTypeEvenement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        validationSupport = new ValidationSupport();

        // ... tes autres validateurs ...

        // Latitude : -90 → 90
        validationSupport.registerValidator(tfLatitude, Validator.createPredicateValidator(
                (String s) -> {
                    try {
                        double lat = Double.parseDouble(s.trim());
                        return lat >= -90 && lat <= 90;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "La latitude doit être un nombre compris entre -90 et 90"));

        // Longitude : -180 → 180
        validationSupport.registerValidator(tfLongitude, Validator.createPredicateValidator(
                (String s) -> {
                    try {
                        double lon = Double.parseDouble(s.trim());
                        return lon >= -180 && lon <= 180;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "La longitude doit être un nombre compris entre -180 et 180"));

        // browse image, ComboBox, etc.
        btnBrowseImage.setOnAction(evt -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Sélectionner une image");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File file = chooser.showOpenDialog(tfTitre.getScene().getWindow());
            if (file != null) {
                try {
                    Path imagesDir = Paths.get("src/main/resources/images");
                    if (Files.notExists(imagesDir)) Files.createDirectories(imagesDir);
                    Path target = imagesDir.resolve(file.getName());
                    Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
                    tfUrlImage.setText(target.toString());
                } catch (IOException e) {
                    new Alert(Alert.AlertType.ERROR,
                            "Impossible de copier l’image : " + e.getMessage())
                            .showAndWait();
                }
            }
        });
    }

    public void setEvenement(Evenement evenement) {
        this.evenementAModifier = evenement;

        // Pré-remplissage des champs existants
        tfTitre.setText(evenement.getTitre());
        tfDescription.setText(evenement.getDescription());
        tfLieu.setText(evenement.getLieu());
        tfNombrePlaces.setText(String.valueOf(evenement.getNombrePlaces()));
        dpDateDebut.setValue(evenement.getDateDebut());
        dpDateFin.setValue(evenement.getDateFin());
        tfStatut.setText(evenement.getStatut());
        tfUrlImage.setText(evenement.getUrlImage());
        tfMail.setText(evenement.getMail());
        // ⚙️ On pré-remplit aussi longitude et latitude
        tfLongitude.setText(String.valueOf(evenement.getLongitude()));
        tfLatitude.setText(String.valueOf(evenement.getLatitude()));

        try {
            List<TypeEvenement> types = serviceTypeEvenement.afficherTypeEvenements();
            cbTypeEvenement.getItems().addAll(types);
            cbTypeEvenement.setConverter(new javafx.util.StringConverter<>() {
                @Override public String toString(TypeEvenement obj) { return obj != null ? obj.getNom() : ""; }
                @Override public TypeEvenement fromString(String str) {
                    return cbTypeEvenement.getItems().stream()
                            .filter(t -> t.getNom().equals(str))
                            .findFirst().orElse(null);
                }
            });
            TypeEvenement sel = serviceTypeEvenement
                    .getTypeEvenementByNom(evenement.getTypeEvenementNom());
            cbTypeEvenement.setValue(sel);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void modifierEvenement() {
        if (validationSupport.isInvalid()) {
            ValidationResult r = validationSupport.getValidationResult();
            StringBuilder errs = new StringBuilder();
            for (ValidationMessage m : r.getMessages()) {
                errs.append("• ").append(m.getText()).append("\n");
            }
            new Alert(Alert.AlertType.ERROR,
                    "Veuillez corriger les erreurs suivantes :\n" + errs.toString())
                    .showAndWait();
            return;
        }

        Evenement e = new Evenement();
        e.setId(evenementAModifier.getId());
        e.setTitre(tfTitre.getText());
        e.setDescription(tfDescription.getText());
        e.setLieu(tfLieu.getText());
        e.setNombrePlaces(Integer.parseInt(tfNombrePlaces.getText().trim()));
        e.setDateDebut(dpDateDebut.getValue());
        e.setDateFin(dpDateFin.getValue());
        e.setStatut(tfStatut.getText());
        e.setUrlImage(tfUrlImage.getText());
        e.setMail(tfMail.getText());
        e.setTypeEvenementId(cbTypeEvenement.getValue().getId());
        // ⚙️ On prend en compte les nouveaux doubles
        e.setLongitude(Double.parseDouble(tfLongitude.getText().trim()));
        e.setLatitude(Double.parseDouble(tfLatitude.getText().trim()));

        serviceEvenement.modifierEvenement(e);

        new Alert(Alert.AlertType.INFORMATION,
                "✅ Événement modifié avec succès !")
                .showAndWait();

        ((Stage) tfTitre.getScene().getWindow()).close();
    }

    @FXML
    public void annulerModification() {
        ((Stage) tfTitre.getScene().getWindow()).close();
    }
}
