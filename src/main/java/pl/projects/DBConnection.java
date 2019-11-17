package pl.projects;

import java.sql.*;

public class DBConnection {

    public Statement connectDb() throws ClassNotFoundException, SQLException{
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/currency_exchange";
            String login = "postgres";
            String password = "admin";

            Connection conn = DriverManager.getConnection(url,login,password);
            return conn.createStatement();
    }
}