package tn.esprit.services;

import tn.esprit.entities.Partenaire;
import tn.esprit.tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartenaireService implements IService<Partenaire> {
    private final Connection connection;

    public PartenaireService() {
        this.connection = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Partenaire p) throws SQLException {
        // Valider les champs requis
        if (p.getNomP() == null || p.getNomP().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du partenaire ne peut pas être vide.");
        }

        if (existsByEmail(p.getEmailP())) {
            throw new IllegalArgumentException("Un partenaire avec cet email existe déjà.");
        }

        String query = "INSERT INTO partenaire (id_p, nom_p, email_p, telephone, type_p, collaboration_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, p.getIdP());
            pst.setString(2, p.getNomP());
            pst.setString(3, p.getEmailP());
            pst.setString(4, p.getTelephone());
            pst.setString(5, p.getTypeP());
            pst.setInt(6, p.getcollaboration_id());

            pst.executeUpdate();

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    p.setId(generatedKeys.getInt(1));
                }
            }

            System.out.println("Partenaire ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion : " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void supprimer(Partenaire p) throws SQLException {
        String query = "DELETE FROM partenaire WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, p.getId());
            pst.executeUpdate();
            System.out.println("Partenaire supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void modifier(Partenaire p) throws SQLException {
        String query = "UPDATE partenaire SET id_p = ?, nom_p = ?, email_p = ?, telephone = ?, type_p = ?, collaboration_id = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, p.getIdP());
            pst.setString(2, p.getNomP());
            pst.setString(3, p.getEmailP());
            pst.setString(4, p.getTelephone());
            pst.setString(5, p.getTypeP());
            pst.setInt(6, p.getcollaboration_id());
            pst.setInt(7, p.getId());

            pst.executeUpdate();
            System.out.println("Partenaire modifié avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Partenaire> recuperer() throws SQLException {
        List<Partenaire> partenaires = new ArrayList<>();
        String query = "SELECT * FROM partenaire";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Partenaire p = new Partenaire();
                p.setId(rs.getInt("id"));
                p.setIdP(rs.getString("id_p"));
                p.setNomP(rs.getString("nom_p"));
                p.setEmailP(rs.getString("email_p"));
                p.setTelephone(rs.getString("telephone"));
                p.setTypeP(rs.getString("type_p"));
                p.setcollaboration_id(rs.getInt("collaboration_id"));

                partenaires.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération : " + e.getMessage());
            throw e;
        }

        return partenaires;
    }

    @Override
    public List<Partenaire> afficher() throws SQLException {
        return recuperer();
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM partenaire WHERE email_p = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Bonus: Trouver un partenaire par ID
    public Partenaire findById(int id) throws SQLException {
        String query = "SELECT * FROM partenaire WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Partenaire p = new Partenaire();
                    p.setId(rs.getInt("id"));
                    p.setIdP(rs.getString("id_p"));
                    p.setNomP(rs.getString("nom_p"));
                    p.setEmailP(rs.getString("email_p"));
                    p.setTelephone(rs.getString("telephone"));
                    p.setTypeP(rs.getString("type_p"));
                    p.setcollaboration_id(rs.getInt("collaboration_id"));
                    return p;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche : " + e.getMessage());
            throw e;
        }
        return null;
    }
}
