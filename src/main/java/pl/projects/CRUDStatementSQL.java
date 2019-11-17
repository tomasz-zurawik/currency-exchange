package pl.projects;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class CRUDStatementSQL {

    private Connection conn;

    public CRUDStatementSQL(Connection conn) {
        this.conn = conn;
    }

    public void selectSQLbyJDBC() {
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT name FROM currency WHERE symbol = 'AUD';");
            while (rs.next()){
                System.out.println(rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
