package tn.esprit.services;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {
    // Add a new entity to the database
    void ajouter(T t) throws SQLException;

    // Delete an entity from the database
    void supprimer(T t) throws SQLException;

    // Update an entity in the database using its ID and updated object
    void modifier(int id, T t) throws SQLException;

    // Retrieve all entities from the database
    List<T> recuperer() throws SQLException;

    // Ban a user by their ID
    void banUser(int userId) throws SQLException;

    // Unban a user by their ID
    void unbanUser(int userId) throws SQLException;
}