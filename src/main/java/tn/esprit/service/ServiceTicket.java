package tn.esprit.service;

import tn.esprit.entities.Ticket;
import tn.esprit.service.IService;
import tn.esprit.tools.myDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceTicket implements IService<Ticket> {

    Connection cnx;

    public ServiceTicket() {
        cnx = myDatabase.getInstance().getMyConnection();
    }

    @Override
    public void ajouter(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO ticket (nom, prenom, numero_telephone, email, date, prix, reservation_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, ticket.getNom());
        ps.setString(2, ticket.getPrenom());
        ps.setInt(3, ticket.getNumero_telephone());
        ps.setString(4, ticket.getEmail());
        ps.setDate(5, new java.sql.Date(ticket.getDate().getTime()));
        ps.setInt(6, ticket.getPrix());
        ps.setInt(7, ticket.getReservation_id()); // Association à la réservation via reservation_id
        ps.executeUpdate();

        // Récupération de l'id généré, si besoin
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            ticket.setId(generatedKeys.getInt(1));
        }
        System.out.println("Ticket ajouté !");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM ticket WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("Ticket supprimé !");
    }

    @Override
    public void modifier(Ticket ticket) throws SQLException {
        String sql = "UPDATE ticket SET nom = ?, prenom = ?, numero_telephone = ?, email = ?, date = ?, prix = ?, reservation_id = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, ticket.getNom());
        ps.setString(2, ticket.getPrenom());
        ps.setInt(3, ticket.getNumero_telephone());
        ps.setString(4, ticket.getEmail());
        ps.setDate(5, new java.sql.Date(ticket.getDate().getTime()));
        ps.setInt(6, ticket.getPrix());
        ps.setInt(7, ticket.getReservation_id());
        ps.setInt(8, ticket.getId()); // Utilise l’identifiant pour cibler l’enregistrement
        ps.executeUpdate();
        System.out.println("Ticket modifié !");
    }

    @Override
    public List<Ticket> afficher() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String req = "SELECT * FROM ticket";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Ticket t = new Ticket(
                    rs.getInt("id"),             // Récupération de l'id
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getInt("numero_telephone"),
                    rs.getString("email"),
                    rs.getDate("date"),
                    rs.getInt("prix"),
                    rs.getInt("reservation_id")
            );
            tickets.add(t);
        }
        return tickets;
    }
}
