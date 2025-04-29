// src/main/java/tn/esprit/controllers/AjouterEvenementController.java
package tn.esprit.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javafx.scene.text.Text;
import tn.esprit.entities.Evenement;
import tn.esprit.entities.TypeEvenement;
import tn.esprit.services.ServiceEvenement;
import tn.esprit.services.ServiceTypeEvenement;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class AjouterEvenementController implements Initializable {

    // Labels d’erreur inline (cachés par défaut)
    @FXML private Label lblTitreError;
    @FXML private Label lblTypeError;
    @FXML private Label lblDescriptionError;
    @FXML private Label lblLieuError;
    @FXML private Label lblPlacesError;
    @FXML private Label lblDateDebutError;
    @FXML private Label lblDateFinError;
    @FXML private Label lblLongitudeError;
    @FXML private Label lblLatitudeError;
    @FXML private Label lblUrlImageError;
    @FXML private Label lblMailError;

    // Champs existants
    @FXML private TextField tfTitre;
    @FXML private TextField tfDescription;
    @FXML private TextField tfLieu;
    @FXML private TextField tfNombrePlaces;
    @FXML private DatePicker dpDateDebut;
    @FXML private DatePicker dpDateFin;
    @FXML private TextField tfLongitude;
    @FXML private TextField tfLatitude;
    @FXML private TextField tfUrlImage;
    @FXML private TextField tfMail;
    @FXML private ComboBox<TypeEvenement> cbTypeEvenement;
    @FXML private Button btnBrowseImage;

    private Runnable onEvenementAjoute;
    private final ServiceTypeEvenement serviceTypeEvenement;
    private ValidationSupport validationSupport;

    // NOUVEAUX CHAMPS POUR LA MÉTÉO
    @FXML private Button btnMeteo;
    @FXML private Text weatherInfo;
    // pour conserver l’objet courant

    // URL de l’API MetaWeather
    private final String cityAPI    = "https://www.metaweather.com/api/location/search/?query=";
    private final String weatherAPI = "http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&appid=%s";

    private final String API_KEY = "bcc9ba15d67eaa0396e6ab86dc628e81";

    public AjouterEvenementController() {
        try {
            serviceTypeEvenement = new ServiceTypeEvenement();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur initialisation ServiceTypeEvenement", e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validationSupport = new ValidationSupport();

        // style des labels d’erreur
        String red = "-fx-text-fill: red;";
        lblTitreError.setStyle(red);
        lblTypeError.setStyle(red);
        lblDescriptionError.setStyle(red);
        lblLieuError.setStyle(red);
        lblPlacesError.setStyle(red);
        lblDateDebutError.setStyle(red);
        lblDateFinError.setStyle(red);
        lblLongitudeError.setStyle(red);
        lblLatitudeError.setStyle(red);
        lblUrlImageError.setStyle(red);
        lblMailError.setStyle(red);

        // prédicats réutilisables
        Predicate<String> nonVide = s -> s != null && !s.trim().isEmpty();
        Predicate<String> entierPositif = s -> {
            try { return Integer.parseInt(s.trim()) > 0; }
            catch (Exception ex) { return false; }
        };
        Predicate<String> doubleValide = s -> {
            try { Double.parseDouble(s.trim()); return true; }
            catch (Exception ex) { return false; }
        };

        // enregistrement des règles
        validationSupport.registerValidator(tfTitre,
                Validator.createPredicateValidator(
                        (String s) -> nonVide.test(s) && s.length() <= 50,
                        "Le titre est requis et ≤ 50 caractères"
                ));
        validationSupport.registerValidator(cbTypeEvenement,
                Validator.createPredicateValidator(
                        (TypeEvenement t) -> t != null,
                        "Le type est requis"
                ));
        validationSupport.registerValidator(tfDescription,
                Validator.createPredicateValidator(
                        (String s) -> nonVide.test(s) && s.length() <= 200,
                        "La description est requise et ≤ 200 caractères"
                ));
        validationSupport.registerValidator(tfLieu,
                Validator.createEmptyValidator("Le lieu est requis"));
        validationSupport.registerValidator(tfNombrePlaces,
                Validator.createPredicateValidator(
                        entierPositif,
                        "Le nombre de places doit être un entier positif"
                ));
        validationSupport.registerValidator(dpDateDebut,
                Validator.createEmptyValidator("La date de début est requise"));
        validationSupport.registerValidator(dpDateFin,
                Validator.createPredicateValidator(
                        (LocalDate fin) -> {
                            LocalDate debut = dpDateDebut.getValue();
                            return fin != null && debut != null && !fin.isBefore(debut);
                        },
                        "La date de fin doit être ≥ date de début"
                ));
        validationSupport.registerValidator(tfLongitude,
                Validator.createPredicateValidator(
                        doubleValide,
                        "Longitude invalide"
                ));
        validationSupport.registerValidator(tfLatitude,
                Validator.createPredicateValidator(
                        doubleValide,
                        "Latitude invalide"
                ));
        validationSupport.registerValidator(tfUrlImage,
                Validator.createEmptyValidator("L'URL de l'image est requise"));
        validationSupport.registerValidator(tfMail,
                Validator.createPredicateValidator(
                        (String m) -> m != null && m.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"),
                        "Email invalide"
                ));

        // listeners pour n’afficher le message QUE quand on quitte le champ
        tfTitre.focusedProperty().addListener((obs, oldF, newF) -> {
            if (!newF) validateField(tfTitre, lblTitreError);
        });
        cbTypeEvenement.focusedProperty().addListener((obs, oldF, newF) -> {
            if (!newF) validateField(cbTypeEvenement, lblTypeError);
        });
        tfDescription.focusedProperty().addListener((obs, oldF, newF) -> {
            if (!newF) validateField(tfDescription, lblDescriptionError);
        });
        tfLieu.focusedProperty().addListener((obs, oldF, newF) -> {
            if (!newF) validateField(tfLieu, lblLieuError);
        });
        tfNombrePlaces.focusedProperty().addListener((obs, oldF, newF) -> {
            if (!newF) validateField(tfNombrePlaces, lblPlacesError);
        });
        dpDateDebut.focusedProperty().addListener((obs, oldF, newF) -> {
            if (!newF) validateField(dpDateDebut, lblDateDebutError);
        });
        dpDateFin.focusedProperty().addListener((obs, oldF, newF) -> {
            if (!newF) validateField(dpDateFin, lblDateFinError);
        });
        tfLongitude.focusedProperty().addListener((obs, oldF, newF) -> {
            if (!newF) validateField(tfLongitude, lblLongitudeError);
        });
        tfLatitude.focusedProperty().addListener((obs, oldF, newF) -> {
            if (!newF) validateField(tfLatitude, lblLatitudeError);
        });
        tfUrlImage.focusedProperty().addListener((obs, oldF, newF) -> {
            if (!newF) validateField(tfUrlImage, lblUrlImageError);
        });
        tfMail.focusedProperty().addListener((obs, oldF, newF) -> {
            if (!newF) validateField(tfMail, lblMailError);
        });

        // Parcourir image (inchangé)
        btnBrowseImage.setOnAction(evt -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Sélectionner une image");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png","*.jpg","*.jpeg","*.gif")
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
                    new Alert(
                            Alert.AlertType.ERROR,
                            "Impossible de copier l’image : " + e.getMessage()
                    ).showAndWait();
                }
            }
        });

        // Remplir la ComboBox (inchangé)
        List<TypeEvenement> types = serviceTypeEvenement.afficherTypeEvenements();
        cbTypeEvenement.getItems().addAll(types);
        cbTypeEvenement.setCellFactory(param -> new ListCell<TypeEvenement>() {
            @Override
            protected void updateItem(TypeEvenement item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom());
            }
        });
        cbTypeEvenement.setButtonCell(cbTypeEvenement.getCellFactory().call(null));
    }

    public void setOnEvenementAjoute(Runnable r) {
        this.onEvenementAjoute = r;
    }

    @FXML
    public void ajouterEvenement() {
        // valider tous les champs à la soumission
        tfTitre.requestFocus();   // pour déclencher focus lost sur champs précédents
        btnBrowseImage.requestFocus();
        validateField(tfTitre, lblTitreError);
        validateField(cbTypeEvenement, lblTypeError);
        validateField(tfDescription, lblDescriptionError);
        validateField(tfLieu, lblLieuError);
        validateField(tfNombrePlaces, lblPlacesError);
        validateField(dpDateDebut, lblDateDebutError);
        validateField(dpDateFin, lblDateFinError);
        validateField(tfLongitude, lblLongitudeError);
        validateField(tfLatitude, lblLatitudeError);
        validateField(tfUrlImage, lblUrlImageError);
        validateField(tfMail, lblMailError);

        if (validationSupport.isInvalid()) {
            return;
        }

        Evenement e = new Evenement();
        TypeEvenement sel = cbTypeEvenement.getValue();
        e.setTypeEvenementId(sel.getId());
        e.setTitre(tfTitre.getText());
        e.setDescription(tfDescription.getText());
        e.setLieu(tfLieu.getText());
        e.setNombrePlaces(Integer.parseInt(tfNombrePlaces.getText().trim()));
        e.setDateDebut(dpDateDebut.getValue());
        e.setDateFin(dpDateFin.getValue());
        e.setStatut("en attente");
        e.setUrlImage(tfUrlImage.getText());
        e.setMail(tfMail.getText());
        e.setLongitude(Double.parseDouble(tfLongitude.getText().trim()));
        e.setLatitude(Double.parseDouble(tfLatitude.getText().trim()));

        try {
            new ServiceEvenement().ajouterEvenement(e);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if (onEvenementAjoute != null) onEvenementAjoute.run();
        ((Stage) tfTitre.getScene().getWindow()).close();
    }

    private void validateField(Control control, Label label) {
        String msg = validationSupport.getValidationResult().getMessages().stream()
                .filter(m -> m.getTarget() == control)
                .map(ValidationMessage::getText)
                .findFirst().orElse("");
        label.setText(msg);
        // n’affiche le label QUE si erreur
        boolean erreur = !msg.isEmpty();
        label.setVisible(erreur);
        label.setManaged(erreur);
    }

    @FXML
    private void handleVoirMeteo() {
        String city = tfLieu.getText().trim();
        LocalDate targetDate = dpDateDebut.getValue(); // Date de début

        if (city.isEmpty() || targetDate == null) {
            showAlert("Erreur", "Veuillez saisir un lieu et une date de début.");
            return;
        }

        Task<JSONObject> task = new Task<>() {
            @Override
            protected JSONObject call() throws Exception {
                return getWeatherForDate(city, targetDate);
            }
        };

        task.setOnSucceeded(e -> {
            JSONObject weather = task.getValue();
            if (weather != null) {
                double min = (Double) weather.get("temp_min");
                double current = (Double) weather.get("temp");
                double max = (Double) weather.get("temp_max");
                weatherInfo.setText(String.format("Min: %.1f°C\nActuelle: %.1f°C\nMax: %.1f°C", min, current, max));
            } else {
                weatherInfo.setText("Données non disponibles.");
            }
        });

        task.setOnFailed(e -> {
            weatherInfo.setText("Erreur de connexion.");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    // Nouvelle méthode pour récupérer les données par date
    private JSONObject getWeatherForDate(String city, LocalDate targetDate) throws Exception {
        String url = String.format(weatherAPI, city, API_KEY);
        tn.esprit.utils.APIConnector connector = new tn.esprit.utils.APIConnector(url);
        JSONObject response = connector.getJSONObject("");

        if (response == null || !response.containsKey("list")) {
            throw new Exception("Ville ou API indisponible.");
        }

        JSONArray forecasts = (JSONArray) response.get("list");
        for (Object item : forecasts) {
            JSONObject forecast = (JSONObject) item;
            String dateStr = (String) forecast.get("dt_txt");
            LocalDate forecastDate = LocalDate.parse(dateStr.substring(0, 10));

            if (forecastDate.isEqual(targetDate)) {
                JSONObject main = (JSONObject) forecast.get("main");
                return main;
            }
        }

        throw new Exception("Aucune donnée pour cette date.");
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
