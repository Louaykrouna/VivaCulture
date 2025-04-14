package tn.esprit.service;

import tn.esprit.entities.Ticket;
import tn.esprit.entities.reservation;
import tn.esprit.tools.myDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Servicereservation implements IService<reservation> {

    Connection cnx;

    public Servicereservation() {
        cnx = myDatabase.getInstance().getMyConnection();
    }

    @Override
    public void ajouter(reservation r) throws SQLException {
        String sql = "INSERT INTO reservation (nom_evenement, nombre_tickets, date_reservation, heure, email) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // Ajouter RETURN_GENERATED_KEYS pour récupérer l'ID généré
        ps.setString(1, r.getNom_evenement());
        ps.setInt(2, r.getNombre_tickets());
        ps.setDate(3, new java.sql.Date(r.getDate_reservation().getTime()));
        ps.setTime(4, new java.sql.Time(r.getHeure().getTime()));
        ps.setString(5, r.getEmail());
        ps.executeUpdate();

        // Récupérer l'ID généré
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            r.setId(rs.getInt(1)); // Récupère l'ID généré
        }

        System.out.println("Réservation ajoutée avec l'ID : " + r.getId());
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM reservation WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();

        System.out.println("Réservation supprimée !");
    }
   public int getIdTicket(int id) throws SQLException {
       List<Ticket> tickets = new ArrayList<>();
       String sql = "SELECT * FROM ticket";
       Statement statement = cnx.createStatement();
       ResultSet rs = statement.executeQuery(sql);

       while (rs.next()) {
           Ticket r = new Ticket();
           r.setId(rs.getInt("id"));
           r.setReservation_id(rs.getInt("reservation_id"));
           tickets.add(r);
       }
       int ticketId = 0;
       for(Ticket ticket : tickets) {
           if(ticket.getReservation_id() == id) {
               ticketId = ticket.getId();
           }
       }
return ticketId;
   }
    public void setIdReservationNullAndDeleteTicket(int idTicket) throws SQLException {
       String sql  = "update Ticket SET reservation_id = null where  id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, Integer.toString(idTicket));
        ps.executeUpdate();

    }
    public void deteteTicket(int id) throws SQLException {
        String sql = "DELETE FROM ticket WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();

        System.out.println("Ticket supprimée !");
    }

    public void deteteReservation(int id) throws SQLException {
        String sql = "DELETE FROM reservation WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();

        System.out.println("reservation supprimée !");
    }
    public void deteteReservationAndTicket(int idReservation) throws SQLException {

        int idTicket  =  getIdTicket(idReservation);
        setIdReservationNullAndDeleteTicket(idTicket);
        deteteTicket(idTicket);
        deteteReservation(idReservation);
     }
    @Override
    public void modifier(reservation r) throws SQLException {
        String sql = "UPDATE reservation SET nom_evenement = ?, nombre_tickets = ?, date_reservation = ?, heure = ?, email = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, r.getNom_evenement());
        ps.setInt(2, r.getNombre_tickets());
        ps.setDate(3, new java.sql.Date(r.getDate_reservation().getTime()));
        ps.setTime(4, new java.sql.Time(r.getHeure().getTime()));
        ps.setString(5, r.getEmail());
        ps.setInt(6, r.getId());
        ps.executeUpdate();
        System.out.println("Réservation modifiée !");
    }

    @Override
    public List<reservation> afficher() throws SQLException {
        List<reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation";
        Statement statement = cnx.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            reservation r = new reservation();
            r.setId(rs.getInt("id"));
            r.setNom_evenement(rs.getString("nom_evenement"));
            r.setNombre_tickets(rs.getInt("nombre_tickets"));
            r.setDate_reservation(rs.getDate("date_reservation"));
            r.setHeure(rs.getTime("heure"));
            r.setEmail(rs.getString("email"));

            reservations.add(r);
        }
        System.out.println("Nombre de réservations récupérées : " + reservations.size());

        // Affichage du contenu des réservations (facultatif, pour vérifier leur structure)
        for (reservation r : reservations) {
            System.out.println("Réservation : " + r.getNom_evenement() + ", Tickets : " + r.getNombre_tickets() +
                    ", Date : " + r.getDate_reservation() + ", Heure : " + r.getHeure() + ", Email : " + r.getEmail());
        }


        return reservations;
    }
}
