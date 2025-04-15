package tn.esprit.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.io.IOException;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file from resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherpartenaire.fxml"));  // Adjusted path for correct FXML loading
            Parent root = loader.load();

            // Set the title and scene
            primaryStage.setTitle("Ajouter une Collaboration");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading FXML: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);  // Launch the JavaFX application
    }
}
