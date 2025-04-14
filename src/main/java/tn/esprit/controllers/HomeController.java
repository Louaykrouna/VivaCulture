package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.entities.User;

import java.io.IOException;

public class HomeController {

    @FXML
    private ImageView profileIcon;

    @FXML
    private Button dashboardButton; // Reference to the dashboard button

    private User currentUser;

    // Set the logged-in user
    public void setUser(User user) {
        this.currentUser = user;
        System.out.println("Logged-in user: " + currentUser);

        // Show the dashboard button only if the user is an admin
        if (currentUser != null && "Admin".equals(currentUser.getRole())) {
            dashboardButton.setVisible(true);
            dashboardButton.setManaged(true); // Ensure the button takes up space in the layout
        }
    }

    // Navigate to Profile Page
    @FXML
    private void navigateToProfile(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent root = loader.load();

            // Pass the logged-in user to the ProfileController
            ProfileController profileController = loader.getController();
            profileController.setUser(currentUser);

            Stage stage = (Stage) profileIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("VivaCulture - Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Failed to load the Profile page.");
        }
    }

    // Navigate to Dashboard
    @FXML
    private void navigateToDashboard(ActionEvent event) {
        if (!"Admin".equals(currentUser.getRole())) {
            showAlert("Access Denied", "Only Admins can access the dashboard.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            Parent root = loader.load();

            // Pass the logged-in user to the DashboardController
            DashboardController dashboardController = loader.getController();
            dashboardController.setUser(currentUser);

            Stage stage = (Stage) profileIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("VivaCulture - Admin Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Dashboard page.");
        }
    }

    // Utility Method to Show Alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void logout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("VivaCulture - Sign In");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Sign In page.");
        }
    }
}