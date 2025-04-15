package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.Collaboration;
import tn.esprit.services.CollaborationService;

import java.sql.SQLException;
import java.time.LocalDate;

public class ModifierCollaborationController {

    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfType;

    @FXML
    private TextField tfStatus;

    @FXML
    private DatePicker dpDateSig;

    @FXML
    private DatePicker dpDateEx;

    private Collaboration collaboration;

    private final CollaborationService service = new CollaborationService();

    public void setCollaboration(Collaboration c) {
        this.collaboration = c;
        tfNom.setText(c.getNomC());
        tfType.setText(c.getType());
        tfStatus.setText(c.getStatus());
        dpDateSig.setValue(c.getDateSig());
        dpDateEx.setValue(c.getDateEx());
    }

    @FXML
    private void handleSave() {
        if (collaboration == null) return;

        // Récupérer les nouvelles valeurs
        collaboration.setNomC(tfNom.getText());
        collaboration.setType(tfType.getText());
        collaboration.setStatus(tfStatus.getText());
        collaboration.setDateSig(dpDateSig.getValue());
        collaboration.setDateEx(dpDateEx.getValue());

        try {
            service.modifier(collaboration); // Correction ici pour passer l'objet collaboration
            closeWindow();
        } catch (SQLException e) {
            showAlert("Erreur", "Échec de la mise à jour : " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }


    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) tfNom.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
