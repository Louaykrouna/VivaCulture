package tn.esprit.services;

import org.mindrot.jbcrypt.BCrypt; // For password hashing
import tn.esprit.entities.User;
import tn.esprit.tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser implements IService<User> {
    private final Connection cnx;

    public ServiceUser() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(User user) throws SQLException {
        String sql = "INSERT INTO user(nom, prenom, email, password, numTel, sexe, address, companyName, matricule, role, status, image, organizerRequestStatus, is_banned) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Use Statement.RETURN_GENERATED_KEYS to retrieve the auto-generated ID
        try (PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getEmail());
            ps.setString(4, hashPassword(user.getPassword())); // Hash the password before saving
            ps.setInt(5, user.getNumTel());
            ps.setString(6, user.getSexe());
            ps.setString(7, user.getAddress());
            ps.setString(8, user.getCompanyName());
            ps.setString(9, user.getMatricule());
            ps.setString(10, user.getRole());
            ps.setString(11, user.getStatus());
            ps.setString(12, user.getImage());
            ps.setString(13, user.getOrganizerRequestStatus());
            ps.setBoolean(14, user.isBanned());

            ps.executeUpdate();

            // Retrieve the auto-generated ID
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1); // Get the first (and only) generated key
                user.setId(generatedId); // Update the User object with the generated ID
                System.out.println("User ajouté avec ID: " + generatedId);
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
    }


    @Override
    public void supprimer(User user) throws SQLException {
        String sql = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, user.getId());
            ps.executeUpdate();
            System.out.println("User supprimé !");
        }
    }

    @Override
    public void modifier(int id, User user) throws SQLException {
        String sql = "UPDATE user SET nom=?, prenom=?, email=?, password=?, numTel=?, sexe=?, address=?, companyName=?, matricule=?, role=?, status=?, image=?, organizerRequestStatus=? WHERE id=?";
        PreparedStatement ps = null;
        try {
            ps = cnx.prepareStatement(sql);
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getEmail());

            // Check if the password has been updated
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                // Hash the password only if it's being updated
                String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
                ps.setString(4, hashedPassword);
            } else {
                // Keep the existing password unchanged
                ps.setString(4, getCurrentPasswordFromDatabase(id));
            }

            ps.setInt(5, user.getNumTel());
            ps.setString(6, user.getSexe());
            ps.setString(7, user.getAddress());
            ps.setString(8, user.getCompanyName());
            ps.setString(9, user.getMatricule());
            ps.setString(10, user.getRole());
            ps.setString(11, user.getStatus());
            ps.setString(12, user.getImage());
            ps.setString(13, user.getOrganizerRequestStatus());
            ps.setInt(14, id);
            ps.executeUpdate();
            System.out.println("User updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
            throw e; // Re-throw the exception to handle it outside if necessary
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.out.println("Error closing PreparedStatement: " + e.getMessage());
                }
            }
        }
    }

    // Helper method to retrieve the current password from the database
    private String getCurrentPasswordFromDatabase(int id) throws SQLException {
        String sql = "SELECT password FROM user WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("password");
        }
        throw new SQLException("User not found with ID: " + id);
    }

    @Override
    public List<User> recuperer() throws SQLException {
        String sql = "SELECT * FROM user";
        List<User> users = new ArrayList<>();
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password")); // Password remains hashed
                user.setNumTel(rs.getInt("numTel"));
                user.setSexe(rs.getString("sexe"));
                user.setAddress(rs.getString("address"));
                user.setCompanyName(rs.getString("companyName"));
                user.setMatricule(rs.getString("matricule"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setImage(rs.getString("image"));
                user.setOrganizerRequestStatus(rs.getString("organizerRequestStatus"));
                user.setBanned(rs.getBoolean("is_banned")); // Populate isBanned

                users.add(user);
            }
        }
        return users;
    }

    @Override
    public void banUser(int userId) throws SQLException {
        String sql = "UPDATE user SET is_banned = TRUE WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            System.out.println("User banned successfully!");
        } catch (SQLException e) {
            System.err.println("Error banning user: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void unbanUser(int userId) throws SQLException {
        String sql = "UPDATE user SET is_banned = FALSE WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            System.out.println("User unbanned successfully!");
        } catch (SQLException e) {
            System.err.println("Error unbanning user: " + e.getMessage());
            throw e;
        }
    }

    // Utility Method to Hash Passwords Using BCrypt
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt()); // Generate a salted hash
    }
}