package tn.esprit.services;

import tn.esprit.entities.TypeEvenement;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceTypeEvenement {
    private final Connection conn;

    public ServiceTypeEvenement() throws SQLException {
        conn = MyDataBase.getInstance().getMyConnection();
    }

public void ajouterTypeEvenement(TypeEvenement t) {
        String req = "INSERT INTO type_evenement (categorie_id, nom, url_image, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setInt(1, t.getCategorieId());
            pst.setString(2, t.getNom());
            pst.setString(3, t.getUrlImage());
            pst.setString(4, t.getDescription());
            pst.executeUpdate();
            System.out.println("✅ Type d'événement ajouté !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur ajout type événement : " + e.getMessage());
        }
    }

    public List<TypeEvenement> afficherTypeEvenements() {
        List<TypeEvenement> list = new ArrayList<>();
        String req = "SELECT t.*, c.nom AS nom_categorie FROM type_evenement t JOIN categorie c ON t.categorie_id = c.id";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                TypeEvenement t = new TypeEvenement(
                        rs.getInt("id"),
                        rs.getInt("categorie_id"),
                        rs.getString("nom"),
                        rs.getString("url_image"),
                        rs.getString("description")
                );
                // On stocke temporairement le nom de la catégorie en description, ou autre si tu ajoutes un champ
                t.setCategorieNom(rs.getString("nom_categorie"));
                list.add(t);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur affichage types d'événements : " + e.getMessage());
        }
        return list;
    }


    public void supprimerTypeEvenement(int id) {
        String req = "DELETE FROM type_evenement WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("🗑️ Type d'événement supprimé !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur suppression type événement : " + e.getMessage());
        }
    }

    public void modifierTypeEvenement(TypeEvenement t) {
        String req = "UPDATE type_evenement SET categorie_id = ?, nom = ?, url_image = ?, description = ? WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setInt(1, t.getCategorieId());
            pst.setString(2, t.getNom());
            pst.setString(3, t.getUrlImage());
            pst.setString(4, t.getDescription());
            pst.setInt(5, t.getId());
            pst.executeUpdate();
            System.out.println("✏️ Type d'événement mis à jour !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur update type événement : " + e.getMessage());
        }
    }
    public TypeEvenement getTypeEvenementByNom(String nom) throws SQLException {
        String query = "SELECT * FROM type_evenement WHERE nom = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                TypeEvenement type = new TypeEvenement();
                type.setId(rs.getInt("id"));
                type.setCategorieId(rs.getInt("categorie_id"));
                type.setNom(rs.getString("nom"));
                type.setUrlImage(rs.getString("url_image"));
                type.setDescription(rs.getString("description"));
                return type;
            }
        }
        return null;  // Retourner null si aucun type d'événement trouvé
    }



}
