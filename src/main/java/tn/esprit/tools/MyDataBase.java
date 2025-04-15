package tn.esprit.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    private final String URL = "jdbc:mysql://localhost:3306/vivaculture";
    private final String USER = "root";
    private final String PSW = "";

    private Connection myConnection;
    private static MyDataBase instance;

    // Private constructor to prevent instantiation
    private MyDataBase() {
        try {
            myConnection = DriverManager.getConnection(URL, USER, PSW);
            System.out.println("Connected to database successfully.");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    // Singleton instance access method
    public static MyDataBase getInstance() {
        if (instance == null) {
            instance = new MyDataBase();
        }
        return instance;
    }

    // Connection getter
    public Connection getCnx() {
        return myConnection;
    }


}
