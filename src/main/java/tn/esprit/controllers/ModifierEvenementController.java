package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import tn.esprit.entities.Evenement;
import tn.esprit.entities.TypeEvenement;
import tn.esprit.services.ServiceEvenement;
import tn.esprit.services.ServiceTypeEvenement;

import java.net.URL;
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
    @FXML private ComboBox<TypeEvenement> cbTypeEvenement;

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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        validationSupport = new ValidationSupport();

        // Validation du titre : obligatoire et maximum 50 caractères.
        validationSupport.registerValidator(tfTitre, Validator.createPredicateValidator(
                s -> {
                    if (s == null) return false;
                    String val = (String) s;
                    return !val.trim().isEmpty() && val.length() <= 50;
                },
                "Le titre est requis et doit contenir au maximum 50 caractères"
        ));

        // Validation de la description : obligatoire et maximum 200 caractères.
        validationSupport.registerValidator(tfDescription, Validator.createPredicateValidator(
                s -> {
                    if (s == null) return false;
                    String val = (String) s;
                    return !val.trim().isEmpty() && val.length() <= 200;
                },
                "La description est requise et doit contenir au maximum 200 caractères"
        ));

        // Validation du lieu : obligatoire.
        validationSupport.registerValidator(tfLieu, Validator.createPredicateValidator(
                s -> {
                    if (s == null) return false;
                    String val = (String) s;
                    return !val.trim().isEmpty();
                },
                "Le lieu est requis"
        ));

        // Validation du nombre de places : entier positif.
        validationSupport.registerValidator(tfNombrePlaces, Validator.createPredicateValidator(
                s -> {
                    if (s == null) return false;
                    try {
                        int nb = Integer.parseInt(((String) s).trim());
                        return nb > 0;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "Le nombre de places doit être un entier positif"
        ));

        // Validation de la date de début : obligatoire.
        validationSupport.registerValidator(dpDateDebut, Validator.createEmptyValidator("La date de début est requise"));

        // Validation de la date de fin : obligatoire et postérieure ou égale à la date de début.
        validationSupport.registerValidator(dpDateFin, Validator.createPredicateValidator(
                fin -> {
                    if (fin == null) return false;
                    LocalDate debut = dpDateDebut.getValue();
                    return debut != null && !((LocalDate) fin).isBefore(debut);
                },
                "La date de fin est requise et doit être postérieure ou égale à la date de début"
        ));

        // Validation de l'URL de l'image : obligatoire.
        validationSupport.registerValidator(tfUrlImage, Validator.createPredicateValidator(
                s -> {
                    if (s == null) return false;
                    String val = (String) s;
                    return !val.trim().isEmpty();
                },
                "L'URL de l'image est requise"
        ));

        // Validation du mail : obligatoire et doit correspondre à un format d'email simple.
        validationSupport.registerValidator(tfMail, Validator.createPredicateValidator(
                s -> {
                    if (s == null) return false;
                    String val = (String) s;
                    return val.trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
                },
                "Un email valide est requis"
        ));

        // Validation de la ComboBox (sélection d'un type d'événement).
        validationSupport.registerValidator(cbTypeEvenement, Validator.createPredicateValidator(
                t -> t != null,
                "Veuillez sélectionner un type d'événement"
        ));
    }

    public void setEvenement(Evenement evenement) {
        this.evenementAModifier = evenement;

        // Pré-remplir les champs avec les données existantes
        tfTitre.setText(evenement.getTitre());
        tfDescription.setText(evenement.getDescription());
        tfLieu.setText(evenement.getLieu());
        tfNombrePlaces.setText(String.valueOf(evenement.getNombrePlaces()));
        dpDateDebut.setValue(evenement.getDateDebut());
        dpDateFin.setValue(evenement.getDateFin());
        tfStatut.setText(evenement.getStatut());
        tfUrlImage.setText(evenement.getUrlImage());
        tfMail.setText(evenement.getMail());

        try {
            List<TypeEvenement> types = serviceTypeEvenement.afficherTypeEvenements();
            cbTypeEvenement.getItems().addAll(types);
            cbTypeEvenement.setConverter(new javafx.util.StringConverter<>() {
                @Override
                public String toString(TypeEvenement object) {
                    return object != null ? object.getNom() : "";
                }
                @Override
                public TypeEvenement fromString(String string) {
                    return cbTypeEvenement.getItems().stream()
                            .filter(t -> t.getNom().equals(string))
                            .findFirst().orElse(null);
                }
            });
            // Sélectionner le type d'événement correspondant
            TypeEvenement typeSelectionne = serviceTypeEvenement.getTypeEvenementByNom(evenement.getTypeEvenementNom());
            cbTypeEvenement.setValue(typeSelectionne);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void modifierEvenement() {
        // Si la validation échoue, construire et afficher une alerte contenant tous les messages d'erreur.
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

        // Construction de l'objet Evenement avec les nouvelles valeurs
        Evenement evenement = new Evenement();
        evenement.setId(evenementAModifier.getId());
        evenement.setTitre(tfTitre.getText());
        evenement.setDescription(tfDescription.getText());
        evenement.setLieu(tfLieu.getText());
        evenement.setNombrePlaces(Integer.parseInt(tfNombrePlaces.getText().trim()));
        evenement.setDateDebut(dpDateDebut.getValue());
        evenement.setDateFin(dpDateFin.getValue());
        evenement.setStatut(tfStatut.getText());
        evenement.setUrlImage(tfUrlImage.getText());
        evenement.setMail(tfMail.getText());
        evenement.setTypeEvenementId(cbTypeEvenement.getValue().getId());

        serviceEvenement.modifierEvenement(evenement);

        Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
        confirmation.setTitle("Modification réussie");
        confirmation.setHeaderText(null);
        confirmation.setContentText("✅ Événement modifié avec succès !");
        confirmation.showAndWait();

        Stage stage = (Stage) tfTitre.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void annulerModification() {
        Stage stage = (Stage) tfTitre.getScene().getWindow();
        stage.close();
    }
}
