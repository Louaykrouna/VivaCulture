package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {

    private static final String URL = "jdbc:mysql://localhost:3306/viva?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PSW = "";

    private Connection myConnection;

    private static MyDataBase instance;

    // Constructeur privé (Singleton)
    private MyDataBase() {
        try {
            // Charger le driver JDBC (optionnel mais sûr)
            Class.forName("com.mysql.cj.jdbc.Driver");

            myConnection = DriverManager.getConnection(URL, USER, PSW);
            System.out.println("✅ Connected to database.");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ SQL Error during connection!");
            e.printStackTrace();
        }
    }

    public static MyDataBase getInstance() {
        if (instance == null) {
            instance = new MyDataBase();
        }
        return instance;
    }

    public Connection getMyConnection() {
        return myConnection;
    }
}
