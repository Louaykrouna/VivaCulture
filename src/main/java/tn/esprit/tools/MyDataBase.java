package tn.esprit.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MyDataBase {
    private final String DB_NAME = "vivaculture";
    private final String URL_BASE = "jdbc:mysql://localhost:3306/";
    private final String URL_DB = URL_BASE + DB_NAME;
    private final String USER = "root";
    private final String PWD = "";
    private Connection cnx;
    private static MyDataBase myDataBase;

    private MyDataBase() {
        try {
            // Step 1: Connect to MySQL without specifying a DB to create it
            Connection tempConnection = DriverManager.getConnection(URL_BASE, USER, PWD);
            Statement st = tempConnection.createStatement();
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            tempConnection.close();
            System.out.println("Database checked/created.");

            // Step 2: Now connect to the actual database
            cnx = DriverManager.getConnection(URL_DB, USER, PWD);
            System.out.println("Connection to " + DB_NAME + " established!");

            // Step 3 (optional): Create user table if not exists
            String createUserTable = """
                CREATE TABLE IF NOT EXISTS user (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nom VARCHAR(255) NOT NULL,
                    prenom VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    numTel INT NOT NULL,
                    sexe VARCHAR(255) NOT NULL,
                    address VARCHAR(255) NOT NULL,
                    companyName VARCHAR(255),
                    matricule VARCHAR(255),
                    role VARCHAR(255),
                    status VARCHAR(255),
                    image VARCHAR(255),
                    organizerRequestStatus VARCHAR(20)
                );
            """;

            Statement tableStatement = cnx.createStatement();
            tableStatement.executeUpdate(createUserTable);
            System.out.println("User table checked/created.");

        } catch (SQLException e) {
            System.err.println("Erreur connexion DB: " + e.getMessage());
        }
    }

    public static MyDataBase getInstance() {
        if (myDataBase == null)
            myDataBase = new MyDataBase();
        return myDataBase;
    }

    public Connection getCnx() {
        return cnx;
    }
}
