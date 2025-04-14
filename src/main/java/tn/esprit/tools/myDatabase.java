
package tn.esprit.tools;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class myDatabase {
    private final String URL="jdbc:mysql://localhost:3306/pifinal";
    private final String USER="root";
    private final String PSW="";

    private Connection myConnection;

    private static myDatabase instance;

    private myDatabase(){
        try {
            myConnection = DriverManager.getConnection(URL,USER,PSW);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getMyConnection() {
        return myConnection;
    }

    public static myDatabase getInstance() {
        if( instance == null)
            instance = new myDatabase();
        return instance;
    }
}
