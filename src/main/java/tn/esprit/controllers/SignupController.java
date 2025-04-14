package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt; // Import for password hashing
import tn.esprit.entities.User;
import tn.esprit.services.ServiceUser;

import java.io.IOException;
import java.sql.SQLException;

public class SignupController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneField;

    @FXML
    private ComboBox<String> genderField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField companyNameField;

    @FXML
    private TextField matriculeField;

    @FXML
    private ComboBox<String> roleField;

    @FXML
    private ComboBox<String> statusField;

    private final ServiceUser serviceUser = new ServiceUser();

    // Initialize ComboBoxes
    @FXML
    public void initialize() {
        genderField.getItems().addAll("Male", "Female", "Other");
        roleField.getItems().addAll("Admin", "User");
        statusField.getItems().addAll("Active", "Inactive");
    }

    // Handle Sign Up Button Click
    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            // Validate input fields
            if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
                    emailField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                    phoneField.getText().isEmpty() || genderField.getValue() == null ||
                    addressField.getText().isEmpty() || roleField.getValue() == null ||
                    statusField.getValue() == null) {
                showAlert("Error", "Please fill in all required fields.");
                return;
            }

            // Validate email format
            if (!isValidEmail(emailField.getText())) {
                showAlert("Invalid Input", "Please enter a valid email address.");
                return;
            }

            // Validate phone number
            int phone;
            try {
                phone = Integer.parseInt(phoneField.getText());
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid phone number.");
                return;
            }

            // Create a new User object with the 'isBanned' field
            User newUser = new User(
                    0, // ID will be auto-generated
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    passwordField.getText(), // Store the hashed password
                    phone,
                    genderField.getValue(),
                    addressField.getText(),
                    companyNameField.getText(),
                    matriculeField.getText(),
                    roleField.getValue(),
                    statusField.getValue(),
                    "", // Image (optional, can be updated later)
                    "Pending", // Organizer request status
                    false // isBanned (default value: false)
            );

            // Save the user to the database
            serviceUser.ajouter(newUser);
            showAlert("Success", "Account created successfully!");

            // Navigate back to the Sign In page
            navigateToSignIn(event);

        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            showAlert("Database Error", "An unexpected error occurred while creating the account. Please try again later.");
        }
    }

    // Handle Sign In Hyperlink Click
    @FXML
    private void handleSignInLink(ActionEvent event) {
        navigateToSignIn(event);
    }

    // Utility Method to Show Alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Utility Method to Navigate to Sign In Page
    @FXML
    private void navigateToSignIn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signin.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event source
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(root));
            stage.setTitle("VivaCulture - Sign In");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Sign In page.");
        }
    }

    // Email Validation Method
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}