package pl.projects;

import java.sql.*;

public class DBConnection {
    private String url = "jdbc:postgresql://localhost:5432/currency_exchange";
    private String login = "postgres";
    private String password = "admin";


    public Statement connectDb() throws ClassNotFoundException, SQLException{
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url,login,password);
            return conn.createStatement();
    }
}