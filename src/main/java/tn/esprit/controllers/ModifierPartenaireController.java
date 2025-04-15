package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import tn.esprit.entities.Partenaire;
import tn.esprit.services.PartenaireService;

public class ModifierPartenaireController {

    @FXML
    private TextField idPField, nomPField, emailPField, telephoneField, typePField;

    private Partenaire partenaire;

    public void setPartenaire(Partenaire partenaire) {
        this.partenaire = partenaire;

        // Populate UI fields with partenaire data
        if (partenaire != null) {
            idPField.setText(partenaire.getIdP());
            nomPField.setText(partenaire.getNomP());
            emailPField.setText(partenaire.getEmailP());
            telephoneField.setText(partenaire.getTelephone());
            typePField.setText(partenaire.getTypeP());
        }
    }

    // Add save/update logic here if needed
    @FXML
    private void savePartenaire() {
        if (partenaire != null) {
            partenaire.setIdP(idPField.getText());
            partenaire.setNomP(nomPField.getText());
            partenaire.setEmailP(emailPField.getText());
            partenaire.setTelephone(telephoneField.getText());
            partenaire.setTypeP(typePField.getText());

            // Call your service to update it in the database
            try {
                PartenaireService service = new PartenaireService();
                service.modifier(partenaire); // Assuming you have a modifier method

                // Optional: Show success alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Partenaire modifié avec succès !");
                alert.showAndWait();

                // Close the window
                idPField.getScene().getWindow().hide();

            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Erreur lors de la modification : " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

}
