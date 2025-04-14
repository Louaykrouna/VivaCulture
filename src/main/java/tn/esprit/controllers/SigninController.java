package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import tn.esprit.entities.User;
import tn.esprit.services.ServiceUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SigninController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private final ServiceUser serviceUser = new ServiceUser();

    // Handle Sign In Button Click
    @FXML
    private void handleSignIn(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        // Authenticate user using the database
        try {
            User authenticatedUser = authenticateUser(email, password);
            if (authenticatedUser != null) {
                showAlert("Success", "Signed in successfully!");
                navigateToHome(event, authenticatedUser); // Navigate to the Home page
            } else {
                showAlert("Error", "Invalid email or password.");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while accessing the database.");
        }
    }

    // Authenticate User Logic
    private User authenticateUser(String email, String enteredPassword) throws SQLException {
        for (User user : serviceUser.recuperer()) {
            System.out.println("Checking user: " + user.getEmail() + ", Banned: " + user.isBanned());

            if (user.getEmail().equalsIgnoreCase(email)) { // Case-insensitive comparison

                // Verify the password using BCrypt
                if (BCrypt.checkpw(enteredPassword, user.getPassword())) {
                    if (user.isBanned()) {
                        System.out.println("Account is banned.");
                        showAlert("Account Banned", "Your account has been banned. Please contact support.");
                        return null;
                    }
                    System.out.println("Login successful for user: " + user);
                    return user; // Successful authentication
                } else {
                    System.out.println("Password does not match.");
                }
            }
        }
        System.out.println("out.");

        return null;
    }

    // Navigate to Home Page
    private void navigateToHome(ActionEvent event, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
            Parent root = loader.load();

            // Pass the logged-in user to the HomeController
            HomeController homeController = loader.getController();
            homeController.setUser(user);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("VivaCulture - Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Home page.");
        }
    }

    // Handle Sign Up Hyperlink Click
    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            // Load the sign-up page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signup.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Sign Up - VivaCulture");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the sign-up page.");
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
}