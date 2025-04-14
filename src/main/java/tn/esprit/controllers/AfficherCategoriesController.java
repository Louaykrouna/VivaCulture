package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.entities.Categorie;
import tn.esprit.services.ServiceCategorie;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherCategoriesController {

    @FXML
    private VBox vboxCategories;

    private final ServiceCategorie serviceCategorie;

    public AfficherCategoriesController() {
        try {
            serviceCategorie = new ServiceCategorie();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        refresh(); // Utilise refresh ici
    }

    // 🔄 Nouvelle méthode publique pour rafraîchir l'affichage
    public void refresh() {
        vboxCategories.getChildren().clear();
        List<Categorie> categories = serviceCategorie.afficherCategories();

        for (Categorie c : categories) {
            HBox hbox = new HBox(10);
            Label nom = new Label("Nom: " + c.getNom());
            nom.setStyle("-fx-text-fill: blue; -fx-cursor: hand;");
            nom.setOnMouseEntered(e -> nom.setStyle("-fx-text-fill: red; -fx-cursor: hand;"));
            nom.setOnMouseExited(e -> nom.setStyle("-fx-text-fill: blue; -fx-cursor: hand;"));

            nom.setOnMouseClicked(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/categorie/DetailsCategorie.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));

                    DetailsCategorieController controller = loader.getController();
                    controller.initData(c);
                    controller.setAfficherController(this); // pour bouton retour si besoin

                    stage.setTitle("Détails de la catégorie");
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            Label desc = new Label(" | Description: " + c.getDescription());
            Label statut = new Label(" | Statut: " + c.getStatut());
            Label image = new Label(" | Image: " + c.getUrl_image());

            Button btnUpdate = new Button("Modifier");
            Button btnDelete = new Button("Supprimer");

            // Bouton Supprimer
            btnDelete.setOnAction(e -> {
                serviceCategorie.supprimerCategorie(c.getId());
                refresh(); // 🔁 Actualise la liste
            });

            // Bouton Modifier
            btnUpdate.setOnAction(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/categorie/ModifierCategorie.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));

                    // Passage de la catégorie au contrôleur
                    ModifierCategorieController controller = loader.getController();
                    controller.initData(c); // Donne la catégorie à modifier
                    controller.setAfficherController(this); // Donne ce contrôleur pour callback

                    stage.setTitle("Modifier Catégorie");
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            hbox.getChildren().addAll(nom, desc, statut,image, btnUpdate, btnDelete);
            vboxCategories.getChildren().add(hbox);
        }
    }
    @FXML
    private void ajouterCategorie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/categorie/AjouterCategorie.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            // Optionnel : passer ce contrôleur à AjouterCategorieController pour rafraîchir après ajout
            AjouterCategorieController controller = loader.getController();
            controller.setAfficherController(this); // nécessite un setter côté ajouter

            stage.setTitle("Ajouter une Catégorie");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void allerVersTypesEvenement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/type_evenement/AfficherTypeEvenement.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Liste des Types d'Événements");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            System.out.println("❌ Erreur ouverture AfficherTypeEvenement.fxml : " + e.getMessage());
        }
    }


}
