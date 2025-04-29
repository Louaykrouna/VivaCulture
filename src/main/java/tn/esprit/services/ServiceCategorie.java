// ServiceCategorie.java
package tn.esprit.services;

import tn.esprit.entities.Categorie;
import tn.esprit.utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ServiceCategorie {
    private final Connection conn;

    public ServiceCategorie() throws SQLException {
        conn = MyDataBase.getInstance().getMyConnection();
    }

    public void ajouterCategorie(Categorie c) {
        String sql = "INSERT INTO categorie (nom, description, statut, url_image) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNom());
            ps.setString(2, c.getDescription());
            ps.setString(3, c.getStatut());
            ps.setString(4, c.getUrl_image());
            ps.executeUpdate();
            System.out.println("✅ Catégorie ajoutée avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Categorie> afficherCategories() {
        List<Categorie> list = new ArrayList<>();
        String sql = "SELECT * FROM categorie";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Categorie c = new Categorie(
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getString("statut"),
                        rs.getString("url_image")
                );
                c.setId(rs.getInt("id"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateCategorie(Categorie c) throws SQLException {
        String req = "UPDATE categorie SET nom=?, description=?, statut=?, url_image=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(req)) {
            ps.setString(1, c.getNom());
            ps.setString(2, c.getDescription());
            ps.setString(3, c.getStatut());
            ps.setString(4, c.getUrl_image());
            ps.setInt(5, c.getId());
            ps.executeUpdate();
        }
        return true;
    }

    public void supprimerCategorie(int id) {
        String sql = "DELETE FROM categorie WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("🗑 Catégorie supprimée.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> afficherCategoriesNoms() {
        List<String> list = new ArrayList<>();
        String req = "SELECT nom FROM categorie";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                list.add(rs.getString("nom"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur affichage catégories : " + e.getMessage());
        }
        return list;
    }

    public int getCategorieIdByNom(String nom) {
        String req = "SELECT id FROM categorie WHERE nom = ?";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setString(1, nom);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur récupération catégorie par nom : " + e.getMessage());
        }
        return -1;
    }

    public String getCategorieNomById(int id) {
        String req = "SELECT nom FROM categorie WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nom");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération du nom de la catégorie : " + e.getMessage());
        }
        return null;
    }

    /**
     * Retourne un map { nomCatégorie → nombre d'événements }
     */
    public Map<String, Integer> getStatsEvenementsParCategorie() throws SQLException {
        String sql = """
        SELECT c.nom AS categorie, COUNT(e.id) AS total
         FROM evenement e
         JOIN type_evenement te ON e.type_evenement_id = te.id
         JOIN categorie c        ON te.categorie_id        = c.id
        GROUP BY c.nom
        """;

        Map<String,Integer> stats = new HashMap<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                stats.put(rs.getString("categorie"), rs.getInt("total"));
            }
        }
        return stats;
    }

}
