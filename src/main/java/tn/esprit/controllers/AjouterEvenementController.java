package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
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

public class AjouterEvenementController implements Initializable {

    @FXML private TextField tfTitre;
    @FXML private TextField tfDescription;
    @FXML private TextField tfLieu;
    @FXML private TextField tfNombrePlaces;
    @FXML private DatePicker dpDateDebut;
    @FXML private DatePicker dpDateFin;
    @FXML private TextField tfUrlImage;
    @FXML private TextField tfMail;
    @FXML private ComboBox<TypeEvenement> cbTypeEvenement;

    private Runnable onEvenementAjoute;
    private final ServiceTypeEvenement serviceTypeEvenement;
    private ValidationSupport validationSupport;

    public AjouterEvenementController() {
        try {
            serviceTypeEvenement = new ServiceTypeEvenement();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'initialisation du service TypeEvenement", e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialiser ValidationSupport pour gérer les règles de validation
        validationSupport = new ValidationSupport();

        // Contrôle sur le titre : obligatoire et maximum 50 caractères.
        validationSupport.registerValidator(tfTitre, Validator.createPredicateValidator(
                (String s) -> s != null && !s.trim().isEmpty() && s.length() <= 50,
                "Le titre est requis et doit contenir au maximum 50 caractères"));

        // Contrôle sur la description : obligatoire et maximum 200 caractères.
        validationSupport.registerValidator(tfDescription, Validator.createPredicateValidator(
                (String s) -> s != null && !s.trim().isEmpty() && s.length() <= 200,
                "La description est requise et doit contenir au maximum 200 caractères"));

        // Contrôle sur le lieu : obligatoire.
        validationSupport.registerValidator(tfLieu, Validator.createEmptyValidator("Le lieu est requis"));

        // Contrôle sur le nombre de places : obligatoire et doit être un entier positif.
        validationSupport.registerValidator(tfNombrePlaces, Validator.createPredicateValidator(
                (String s) -> {
                    try {
                        int nb = Integer.parseInt(s.trim());
                        return nb > 0;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "Le nombre de places est requis et doit être un entier positif"));

        // Contrôle sur la date de début : obligatoire.
        validationSupport.registerValidator(dpDateDebut, Validator.createEmptyValidator("La date de début est requise"));

        // Contrôle sur la date de fin : obligatoire et doit être postérieure ou égale à la date de début.
        validationSupport.registerValidator(dpDateFin, Validator.createPredicateValidator(
                (LocalDate fin) -> {
                    LocalDate debut = dpDateDebut.getValue();
                    if (fin == null || debut == null) return false;
                    return !fin.isBefore(debut);
                },
                "La date de fin est requise et doit être postérieure ou égale à la date de début"));

        // Contrôle sur l'URL de l'image : obligatoire.
        validationSupport.registerValidator(tfUrlImage, Validator.createEmptyValidator("L'URL de l'image est requise"));

        // Contrôle sur le mail : obligatoire et doit correspondre à un format simple d'email.
        validationSupport.registerValidator(tfMail, Validator.createPredicateValidator(
                (String mail) -> mail != null && mail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"),
                "Un email valide est requis"));

        // Contrôle sur la ComboBox pour que l'utilisateur sélectionne un type d'événement.
        validationSupport.registerValidator(cbTypeEvenement, Validator.createPredicateValidator(
                (TypeEvenement t) -> t != null,
                "Veuillez sélectionner un type d'événement"));

        // Initialisation de la ComboBox avec les types d'événements
        List<TypeEvenement> types = serviceTypeEvenement.afficherTypeEvenements();
        cbTypeEvenement.getItems().addAll(types);
        cbTypeEvenement.setCellFactory(param -> new ListCell<TypeEvenement>() {
            @Override
            protected void updateItem(TypeEvenement item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : item.getNom());
            }
        });
        cbTypeEvenement.setButtonCell(new ListCell<TypeEvenement>() {
            @Override
            protected void updateItem(TypeEvenement item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : item.getNom());
            }
        });
    }

    // Setter pour le callback qui s'exécute après l'ajout de l'événement
    public void setOnEvenementAjoute(Runnable r) {
        this.onEvenementAjoute = r;
    }

    @FXML
    public void ajouterEvenement() {
        // Vérifier si la saisie respecte toutes les contraintes grâce à ValidationSupport
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

        // Construction de l'objet Evenement à partir des champs renseignés
        Evenement e = new Evenement();
        TypeEvenement selectedType = cbTypeEvenement.getValue();
        e.setTypeEvenementId(selectedType.getId());
        e.setTitre(tfTitre.getText());
        e.setDescription(tfDescription.getText());
        e.setLieu(tfLieu.getText());
        e.setNombrePlaces(Integer.parseInt(tfNombrePlaces.getText().trim()));
        e.setDateDebut(dpDateDebut.getValue());
        e.setDateFin(dpDateFin.getValue());
        e.setStatut("en attente"); // On peut fixer le statut par défaut, ici "en attente"
        e.setUrlImage(tfUrlImage.getText());
        e.setMail(tfMail.getText());

        try {
            ServiceEvenement service = new ServiceEvenement();
            service.ajouterEvenement(e);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Exécuter le callback s'il est défini
        if (onEvenementAjoute != null) {
            onEvenementAjoute.run();
        }

        // Fermer la fenêtre d'ajout de l'événement
        Stage stage = (Stage) tfTitre.getScene().getWindow();
        stage.close();
    }
}
