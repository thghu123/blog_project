package ConnectionUtil;


import java.sql.*;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            String url = "jdbc:oracle:thin:@localhost:1521:oracle19";
            String user = "c##maxgauge", pw = "maxgauge";
            conn = DriverManager.getConnection(url,user,pw);
        }catch(Exception e ) {
            e.printStackTrace();
            if(conn!=null)conn.close();
        }
        return conn;
    }
}