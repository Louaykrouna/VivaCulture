package tn.esprit.services;

import tn.esprit.entities.Evenement;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement {
    private final Connection conn;

    public ServiceEvenement() throws SQLException {
        conn = MyDataBase.getInstance().getMyConnection();
    }

    public void ajouterEvenement(Evenement e) {
        String req = "INSERT INTO evenement (type_evenement_id, titre, description, lieu, nombre_places, date_debut, date_fin, statut, url_image, mail) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setInt(1, e.getTypeEvenementId());
            pst.setString(2, e.getTitre());
            pst.setString(3, e.getDescription());
            pst.setString(4, e.getLieu());
            pst.setInt(5, e.getNombrePlaces());
            pst.setDate(6, Date.valueOf(e.getDateDebut()));
            pst.setDate(7, Date.valueOf(e.getDateFin()));
            pst.setString(8, "en attente"); // Le statut est toujours "en attente"
            pst.setString(9, e.getUrlImage());
            pst.setString(10, e.getMail());
            pst.executeUpdate();
            System.out.println("✅ Événement ajouté !");
        } catch (SQLException ex) {
            System.out.println("❌ Erreur ajout événement : " + ex.getMessage());
        }
    }

    public List<Evenement> afficherEvenements() {
        List<Evenement> list = new ArrayList<>();
        String req = "SELECT e.*, t.nom AS nom_type FROM evenement e JOIN type_evenement t ON e.type_evenement_id = t.id";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                Evenement e = new Evenement(
                        rs.getInt("id"),
                        rs.getInt("type_evenement_id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getString("lieu"),
                        rs.getInt("nombre_places"),
                        rs.getDate("date_debut").toLocalDate(),
                        rs.getDate("date_fin").toLocalDate(),
                        rs.getString("statut"),
                        rs.getString("url_image"),
                        rs.getString("mail")
                );
                e.setTypeEvenementNom(rs.getString("nom_type"));
                list.add(e);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur affichage événements : " + e.getMessage());
        }
        return list;
    }

    public void modifierEvenement(Evenement e) {
        String req = "UPDATE evenement SET type_evenement_id=?, titre=?, description=?, lieu=?, nombre_places=?, date_debut=?, date_fin=?, statut=?, url_image=?, mail=? WHERE id=?";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setInt(1, e.getTypeEvenementId());
            pst.setString(2, e.getTitre());
            pst.setString(3, e.getDescription());
            pst.setString(4, e.getLieu());
            pst.setInt(5, e.getNombrePlaces());
            pst.setDate(6, Date.valueOf(e.getDateDebut()));
            pst.setDate(7, Date.valueOf(e.getDateFin()));
            pst.setString(8, "en attente"); // Le statut reste "en attente"
            pst.setString(9, e.getUrlImage());
            pst.setString(10, e.getMail());
            pst.setInt(11, e.getId());
            pst.executeUpdate();
            System.out.println("✏️ Événement modifié !");
        } catch (SQLException ex) {
            System.out.println("❌ Erreur modification événement : " + ex.getMessage());
        }
    }

    public void supprimerEvenement(int id) {
        String req = "DELETE FROM evenement WHERE id=?";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("🗑️ Événement supprimé !");
        } catch (SQLException ex) {
            System.out.println("❌ Erreur suppression événement : " + ex.getMessage());
        }
    }
}
