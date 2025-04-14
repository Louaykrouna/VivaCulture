package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.services.ServiceUser;
import javafx.scene.image.Image;

import java.io.IOException;
import java.sql.SQLException;

public class DashboardController {

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private TableColumn<User, String> statusColumn;

    private final ServiceUser serviceUser = new ServiceUser();

    private ObservableList<User> userList = FXCollections.observableArrayList();

    private User currentUser;

    // Set the logged-in user
    public void setUser(User user) {
        this.currentUser = user;
        System.out.println("Logged-in user set in DashboardController: " + currentUser);
        // You can update UI elements here with user data if needed
    }

    @FXML
    private TableColumn<User, Void> imageColumn; // New column for images

    @FXML
    private void initialize() {
        try {
            loadUsers();
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while loading users: " + e.getMessage());
        }

        // Set up table columns
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        roleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        // Set up the image column
        imageColumn.setCellFactory(param -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    User user = getTableView().getItems().get(getIndex());
                    String imagePath = user.getImage();
                    if (imagePath != null && !imagePath.isEmpty()) {
                        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
                        imageView.setImage(image);
                        imageView.setFitWidth(50); // Set image size
                        imageView.setFitHeight(50);
                        setGraphic(imageView);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private void loadUsers() throws SQLException {
        userList.clear();
        userList.addAll(serviceUser.recuperer());
        userTable.setItems(userList);
    }

    @FXML
    private void addUser() {
        // Create a dialog to collect user details
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add New User");
        dialog.setHeaderText("Enter the details of the new user:");

        // Set the button types
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Create the form fields
        TextField nomField = new TextField();
        nomField.setPromptText("Nom");

        TextField prenomField = new TextField();
        prenomField.setPromptText("Prenom");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        TextField numTelField = new TextField();
        numTelField.setPromptText("Numéro de Téléphone");

        TextField sexeField = new TextField();
        sexeField.setPromptText("Sexe");

        TextField addressField = new TextField();
        addressField.setPromptText("Address");

        TextField companyNameField = new TextField();
        companyNameField.setPromptText("Company Name");

        TextField matriculeField = new TextField();
        matriculeField.setPromptText("Matricule");

        TextField roleField = new TextField();
        roleField.setPromptText("Role");

        TextField statusField = new TextField();
        statusField.setPromptText("Status");

        TextField imageField = new TextField();
        imageField.setPromptText("Image URL");

        TextField organizerRequestStatusField = new TextField();
        organizerRequestStatusField.setPromptText("Organizer Request Status");

        // Layout the form
        GridPane grid = new GridPane();
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prenom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Password:"), 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(new Label("Numéro de Téléphone:"), 0, 4);
        grid.add(numTelField, 1, 4);
        grid.add(new Label("Sexe:"), 0, 5);
        grid.add(sexeField, 1, 5);
        grid.add(new Label("Address:"), 0, 6);
        grid.add(addressField, 1, 6);
        grid.add(new Label("Company Name:"), 0, 7);
        grid.add(companyNameField, 1, 7);
        grid.add(new Label("Matricule:"), 0, 8);
        grid.add(matriculeField, 1, 8);
        grid.add(new Label("Role:"), 0, 9);
        grid.add(roleField, 1, 9);
        grid.add(new Label("Status:"), 0, 10);
        grid.add(statusField, 1, 10);
        grid.add(new Label("Image URL:"), 0, 11);
        grid.add(imageField, 1, 11);
        grid.add(new Label("Organizer Request Status:"), 0, 12);
        grid.add(organizerRequestStatusField, 1, 12);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a User object when the Add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                try {
                    int numTel = Integer.parseInt(numTelField.getText());
                    return new User(
                            0, // ID will be auto-generated
                            nomField.getText(),
                            prenomField.getText(),
                            emailField.getText(),
                            passwordField.getText(),
                            numTel,
                            sexeField.getText(),
                            addressField.getText(),
                            companyNameField.getText(),
                            matriculeField.getText(),
                            roleField.getText(),
                            statusField.getText(),
                            imageField.getText(),
                            organizerRequestStatusField.getText(),
                            false // Default value for isBanned
                    );
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter a valid phone number.");
                }
            }
            return null;
        });

        // Show the dialog and wait for the user to add the new user
        dialog.showAndWait().ifPresent(newUser -> {
            try {
                serviceUser.ajouter(newUser);
                userList.add(newUser); // Update the table view
                showAlert("Success", "User added successfully!");
            } catch (SQLException e) {
                showAlert("Database Error", "An error occurred while adding the user: " + e.getMessage());
            }
        });
    }

    @FXML
    private void updateUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Error", "Please select a user to update.");
            return;
        }

        // Create a dialog to edit user details
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Update User");
        dialog.setHeaderText("Edit the details of the selected user:");

        // Set the button types
        ButtonType updateButton = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButton, ButtonType.CANCEL);

        // Create the form fields with the current user's data pre-filled
        TextField nomField = new TextField(selectedUser.getNom());
        nomField.setPromptText("Nom");

        TextField prenomField = new TextField(selectedUser.getPrenom());
        prenomField.setPromptText("Prenom");

        TextField emailField = new TextField(selectedUser.getEmail());
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password (leave blank to keep current)");

        TextField numTelField = new TextField(String.valueOf(selectedUser.getNumTel()));
        numTelField.setPromptText("Numéro de Téléphone");

        TextField sexeField = new TextField(selectedUser.getSexe());
        sexeField.setPromptText("Sexe");

        TextField addressField = new TextField(selectedUser.getAddress());
        addressField.setPromptText("Address");

        TextField companyNameField = new TextField(selectedUser.getCompanyName());
        companyNameField.setPromptText("Company Name");

        TextField matriculeField = new TextField(selectedUser.getMatricule());
        matriculeField.setPromptText("Matricule");

        TextField roleField = new TextField(selectedUser.getRole());
        roleField.setPromptText("Role");

        TextField statusField = new TextField(selectedUser.getStatus());
        statusField.setPromptText("Status");

        TextField imageField = new TextField(selectedUser.getImage());
        imageField.setPromptText("Image URL");

        TextField organizerRequestStatusField = new TextField(selectedUser.getOrganizerRequestStatus());
        organizerRequestStatusField.setPromptText("Organizer Request Status");

        // Layout the form
        GridPane grid = new GridPane();
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prenom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Password:"), 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(new Label("Numéro de Téléphone:"), 0, 4);
        grid.add(numTelField, 1, 4);
        grid.add(new Label("Sexe:"), 0, 5);
        grid.add(sexeField, 1, 5);
        grid.add(new Label("Address:"), 0, 6);
        grid.add(addressField, 1, 6);
        grid.add(new Label("Company Name:"), 0, 7);
        grid.add(companyNameField, 1, 7);
        grid.add(new Label("Matricule:"), 0, 8);
        grid.add(matriculeField, 1, 8);
        grid.add(new Label("Role:"), 0, 9);
        grid.add(roleField, 1, 9);
        grid.add(new Label("Status:"), 0, 10);
        grid.add(statusField, 1, 10);
        grid.add(new Label("Image URL:"), 0, 11);
        grid.add(imageField, 1, 11);
        grid.add(new Label("Organizer Request Status:"), 0, 12);
        grid.add(organizerRequestStatusField, 1, 12);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a User object when the Update button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButton) {
                try {
                    int numTel = Integer.parseInt(numTelField.getText());
                    String newPassword = passwordField.getText().isEmpty() ? selectedUser.getPassword() : passwordField.getText();
                    return new User(
                            selectedUser.getId(), // Keep the same ID
                            nomField.getText(),
                            prenomField.getText(),
                            emailField.getText(),
                            newPassword,
                            numTel,
                            sexeField.getText(),
                            addressField.getText(),
                            companyNameField.getText(),
                            matriculeField.getText(),
                            roleField.getText(),
                            statusField.getText(),
                            imageField.getText(),
                            organizerRequestStatusField.getText(),
                            selectedUser.isBanned() // Keep the banned status unchanged
                    );
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter a valid phone number.");
                }
            }
            return null;
        });

        // Show the dialog and wait for the user to update the selected user
        dialog.showAndWait().ifPresent(updatedUser -> {
            try {
                serviceUser.modifier(updatedUser.getId(), updatedUser); // Update in the database
                loadUsers(); // Refresh the table view
                showAlert("Success", "User updated successfully!");
            } catch (SQLException e) {
                showAlert("Database Error", "An error occurred while updating the user: " + e.getMessage());
            }
        });
    }

    @FXML
    private void deleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Error", "Please select a user to delete.");
            return;
        }
        try {
            serviceUser.supprimer(selectedUser);
            userList.remove(selectedUser);
            showAlert("Success", "User deleted successfully!");
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while deleting the user: " + e.getMessage());
        }
    }

    @FXML
    private void banUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Error", "Please select a user to ban.");
            return;
        }
        try {
            serviceUser.banUser(selectedUser.getId());
            selectedUser.setBanned(true);
            showAlert("Success", "User banned successfully!");
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while banning the user: " + e.getMessage());
        }
    }
    @FXML
    private void unbanUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Error", "Please select a user to unban.");
            return;
        }
        try {
            serviceUser.unbanUser(selectedUser.getId());
            selectedUser.setBanned(false); // Update the local user object
            showAlert("Success", "User unbanned successfully!");
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while unbanning the user: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void logout(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("VivaCulture - Sign In");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Sign In page.");
        }
    }

    public void returnToHome(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
            Parent root = loader.load();

            // Pass the logged-in user to the HomeController
            HomeController homeController = loader.getController();
            homeController.setUser(currentUser);

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("VivaCulture - Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Home page.");
        }
    }
}