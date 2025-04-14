package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.entities.Ticket;
import tn.esprit.entities.reservation;
import tn.esprit.service.Servicereservation;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;

public class deleteOrmodifyReservationcontroller {

    @FXML
    private TextField tfNomEvenement;

    @FXML
    private TextField tfNombreTickets;

    @FXML
    private DatePicker dpDate;

    @FXML
    private TextField tfHeure;

    @FXML
    private TextField tfEmail;

    @FXML
    private Button supprimer;

    @FXML
    private Button modifier;

    private int currentReservationId;
    private final Servicereservation service = new Servicereservation();

    public deleteOrmodifyReservationcontroller() {
    }

    // Remplir les champs avec les données sélectionnées
    public void setData(String reservationInfo) {
        try {
            String[] parts = reservationInfo.split(", ");
            String nomEvenement = parts[0].split(": ")[1];
            int nombreTickets = Integer.parseInt(parts[1].split(": ")[1]);
            LocalDate date = LocalDate.parse(parts[2].split(": ")[1]);
            String heure = parts[3].split(": ")[1];
            String email = parts[4].split(": ")[1];

            tfNomEvenement.setText(nomEvenement);
            tfNombreTickets.setText(String.valueOf(nombreTickets));
            dpDate.setValue(date);
            tfHeure.setText(heure);
            tfEmail.setText(email);

            currentReservationId = controllers.afficherreservationcontroller.listOfReservation.get(reservationInfo);
        } catch (Exception e) {
            System.out.println("Erreur parsing setData: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        modifier.setOnAction(event -> {
            try {
                String nom = tfNomEvenement.getText();
                int tickets = Integer.parseInt(tfNombreTickets.getText());
                LocalDate date = dpDate.getValue();
                String heure = tfHeure.getText();
                String email = tfEmail.getText();

                reservation updated = new reservation();
                updated.setId(currentReservationId);
                updated.setNom_evenement(nom);
                updated.setNombre_tickets(tickets);
                updated.setDate_reservation(Date.valueOf(date));
                updated.setHeure(Time.valueOf(heure));
                updated.setEmail(email);

                service.modifier(updated);

                // Fermer la fenêtre
                ((Stage) modifier.getScene().getWindow()).close();
            } catch (Exception e) {
                System.out.println("Erreur modification : " + e.getMessage());
            }
        });

        supprimer.setOnAction(event -> {
            try {
                service.deteteReservationAndTicket(currentReservationId);
                ((Stage) supprimer.getScene().getWindow()).close();
            } catch (SQLException e) {
                System.out.println("Erreur suppression : " + e.getMessage());
            }
        });
    }

    public void setTicket(Ticket ticket) {

    }
}
