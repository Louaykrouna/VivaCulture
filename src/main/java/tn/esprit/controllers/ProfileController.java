package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import tn.esprit.entities.User;
import tn.esprit.services.ServiceUser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class ProfileController {

    @FXML
    private Hyperlink backToHomeLink;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private ImageView profilePictureView;

    @FXML
    private PasswordField oldPasswordField; // Field for old password

    @FXML
    private PasswordField newPasswordField; // Field for new password

    private User currentUser;

    private final ServiceUser serviceUser = new ServiceUser();

    // Set the logged-in user
    public void setUser(User user) {
        this.currentUser = user;

        // Populate fields with user data
        nameField.setText(user.getNom() + " " + user.getPrenom());
        emailField.setText(user.getEmail());
        phoneField.setText(String.valueOf(user.getNumTel()));

        // Load the profile picture if available
        String imagePath = user.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Image image = new Image(imagePath);
                profilePictureView.setImage(image);
            } catch (Exception e) {
                System.out.println("Failed to load profile picture: " + e.getMessage());
            }
        }
    }

    // Handle navigation back to the Home Page
    @FXML
    private void navigateToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
            Parent root = loader.load();

            HomeController homeController = loader.getController();
            homeController.setUser(currentUser);

            Stage stage = (Stage) backToHomeLink.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("VivaCulture - Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Home page.");
        }
    }

    // Handle Update Profile
    @FXML
    private void handleUpdateProfile() {
        try {
            // Retrieve updated values from the UI fields
            String fullName = nameField.getText();
            String[] names = fullName.split(" ", 2); // Split into first name and last name
            String firstName = names.length > 0 ? names[0] : "";
            String lastName = names.length > 1 ? names[1] : "";

            String email = emailField.getText();
            int phone = Integer.parseInt(phoneField.getText());

            // Validate required fields
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneField.getText().isEmpty()) {
                showAlert("Error", "Please fill in all required fields.");
                return;
            }

            // Check if the user wants to change the password
            String oldPassword = oldPasswordField.getText();
            String newPassword = newPasswordField.getText();

            if (!oldPassword.isEmpty() || !newPassword.isEmpty()) {
                // Verify the old password
                if (!BCrypt.checkpw(oldPassword, currentUser.getPassword())) {
                    showAlert("Error", "Old password is incorrect.");
                    return;
                }

                // Update the password (plain text, will be hashed in modifier)
                currentUser.setPassword(newPassword);
            }

            // Update the user object
            currentUser.setNom(firstName);
            currentUser.setPrenom(lastName);
            currentUser.setEmail(email);
            currentUser.setNumTel(phone);

            // Update the user in the database
            serviceUser.modifier(currentUser.getId(), currentUser);

            // Show success message
            showAlert("Success", "Profile updated successfully!");
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid phone number format.");
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while updating the profile: " + e.getMessage());
        }
    }

    // Handle Profile Picture Upload
    @FXML
    private void handleUploadPicture() {
        // Open a file chooser to select an image
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Create a dedicated directory for images if it doesn't exist
                File uploadsDir = new File("src/main/resources/images");
                if (!uploadsDir.exists()) {
                    boolean success = uploadsDir.mkdirs();
                    if (!success) {
                        showAlert("Error", "Failed to create directory: " + uploadsDir.getAbsolutePath());
                        return;
                    }
                }

                // Move the file to the uploads directory
                File destFile = new File(uploadsDir, selectedFile.getName());
                if (!selectedFile.renameTo(destFile)) {
                    // If rename fails, copy the file manually
                    java.nio.file.Files.copy(selectedFile.toPath(), destFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                }

                // Save the relative path to the user object
                String relativePath = "/images/" + selectedFile.getName();
                currentUser.setImage(relativePath);

                // Load the image into the ImageView
                URL resource = getClass().getResource(relativePath);
                if (resource == null) {
                    System.out.println("Resource path: " + relativePath); // Debugging line
                    showAlert("Error", "Failed to locate the image in the resources.");
                    return;
                }
                Image image = new Image(resource.toExternalForm());
                profilePictureView.setImage(image);

                // Update the user in the database with the new image path
                serviceUser.modifier(currentUser.getId(), currentUser);

                showAlert("Success", "Profile picture updated successfully!");
            } catch (Exception e) {
                showAlert("Error", "Failed to load the selected image: " + e.getMessage());
            }
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