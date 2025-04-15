package tn.esprit.services;

import tn.esprit.entities.Collaboration;
import tn.esprit.tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CollaborationService implements IService<Collaboration> {
    private final Connection connection;

    public CollaborationService() {
        connection = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Collaboration c) throws SQLException {
        String query = "INSERT INTO collaboration (nom_c, type, date_sig, date_ex, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, c.getNomC());
            pst.setString(2, c.getType());
            pst.setDate(3, Date.valueOf(c.getDateSig()));
            pst.setDate(4, Date.valueOf(c.getDateEx()));
            pst.setString(5, c.getStatus());
            pst.executeUpdate();
        }
    }


    public void supprimer(Collaboration collab) throws SQLException {
        String query = "DELETE FROM collaboration WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, collab.getId());  // Utilisez l'ID de la collaboration pour la supprimer
            pst.executeUpdate();
            System.out.println("Collaboration supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression: " + e.getMessage());
            throw e;  // Propager l'exception pour qu'elle soit gérée dans le contrôleur
        }
    }





    public void modifier(Collaboration c) throws SQLException {
        String query = "UPDATE collaboration SET nom_c = ?, type = ?, status = ?, date_sig = ?, date_ex = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, c.getNomC());
            pst.setString(2, c.getType());
            pst.setString(3, c.getStatus());
            pst.setDate(4, Date.valueOf(c.getDateSig()));  // Convert LocalDate to SQL Date
            pst.setDate(5, Date.valueOf(c.getDateEx()));  // Convert LocalDate to SQL Date
            pst.setInt(6, c.getId());  // Assure-toi que l'objet c a un ID valide

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }










    @Override
    public List<Collaboration> recuperer() throws SQLException {
        List<Collaboration> collaborations = new ArrayList<>();
        String query = "SELECT * FROM collaboration";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Collaboration c = new Collaboration();
                c.setId(rs.getInt("id"));
                c.setNomC(rs.getString("nom_c"));
                c.setType(rs.getString("type"));
                c.setDateSig(rs.getDate("date_sig").toLocalDate());
                c.setDateEx(rs.getDate("date_ex").toLocalDate());
                c.setStatus(rs.getString("status"));
                collaborations.add(c);
            }
        }

        return collaborations;
    }

    public List<Collaboration> afficher() throws SQLException {
        return recuperer(); // Utilise la méthode déjà correcte
    }

}
