package tn.esprit.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args); // Lance l'application JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // ✏️ Tu peux changer ici le nom du fichier FXML à afficher (ex: AjouterCategorie.fxml)
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/categorie/AfficherCategorie.fxml"));

            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("Gestion des Catégories");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("❌ Erreur au chargement du fichier FXML : " + e.getMessage());
        }
    }
}
