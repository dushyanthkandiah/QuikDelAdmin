package ServerLink;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Database {
    private static Connection conn = null;
    private static Statement st = null;

    public static String connectionString = "jdbc:mysql://den1.mysql3.gear.host/dush";
//    public static String connectionString = "jdbc:mysql://den1.mysql6.gear.host/gymfitness";
    public static final String db_user = "dush";
//    public static final String db_user = "gymfitness";

    public static final String db_pass = "Tm6v2kp~_P79";
    public static final String DRIVER = "com.mysql.jdbc.Driver";

    public Database() {

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(connectionString, db_user, db_pass);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }

    public ResultSet executeQuery(String sql){
        ResultSet res  = null;
        try{

            st = conn.createStatement();
            res = st.executeQuery(sql);

        } catch(SQLException e){
            e.printStackTrace();
        }

        return res;
    }

    public PreparedStatement executeUpdate(String sql){
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }



}
