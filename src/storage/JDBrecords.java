package storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import vehicleGarage.Vehicle;

/**
 *  CIS319 Advanced OOP with Java
 *  Course Project
 *  
 *  This class is responsible for entering rows at once into the database.<br/>
 *  <br/>
 *  
 * @author  Chris Vought     countorlak@gmail.com
 * @since   11/29/2016
 */
public class JDBrecords {
    
//    User name and password shuld never be here but this is just for proof of concept.
    private final String javaDBuser = "CIS319user";
    private final String javaDBpass = "password";
    private final String driver = "org.apache.derby.jdbc.ClientDriver";
    private final String dbName = "garageDB";
    private final String connectionURL = "jdbc:derby://localhost:1527/" 
            + dbName;
    
    private static final String TABLE = "CIS319USER.VEH";
    private static final String SELECT_ALL_FROM_TABLE = "SELECT * FROM " 
            +  TABLE; 
    
    public Connection getConnection() throws SQLException {
        
        Connection conn = null;

        /**
         * Properties object is not needed.  Thus the more simplistic form
         * of getConnections() is used.<br/>
         * 
         * @param javaDBpass Is hard-coded and final.  That may need to be
         * changed later if the database needs to be secured at all.
         * @param javaDBuser Is hard-coded and final.  May not need to be 
         * changed as embedded javaDB is only single-user.
         */
        conn = DriverManager.getConnection(this.connectionURL, javaDBuser, javaDBpass);
        
        return conn;
    }
    
    public void viewAll(Connection conn) {
        
        try(Statement stmt = conn.createStatement();) {
            
            ResultSet dbReturned = stmt.executeQuery(SELECT_ALL_FROM_TABLE);
            while (dbReturned.next()) {
                System.out.print(dbReturned.getString("MAKE"));
                System.out.print(", " + dbReturned.getString("MODEL"));
                System.out.print(", " + dbReturned.getInt("YEAR_INT"));
                System.out.print(", " + dbReturned.getDouble("PRICE"));
                System.out.println(", " + dbReturned.getString("VEHICLETYPE"));
            }
            System.out.println("-----\n");
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.exit(-1);
        }
    }
    
    /**
     * This'd be better as a batch but there's too little time to implement it.
     * So long as the Connection is being reused by the caller it 
     * shouldn't be too harsh.
     * 
     * @param vehIn Prepared POJO javaBean.
     * @param conn To be instantiated and reused by caller.
     */
    public void addSingleEntry(Vehicle vehIn, Connection conn) {
        
//      Make SQL VALUES string.
        String vehValues = "(\'" + vehIn.getMake() + "\',\'" 
                + vehIn.getModel() + "\'," 
                + vehIn.getYear() + "," 
                + vehIn.getPrice() + ",\'" 
                + vehIn.getType() + "\')";
        
//      Try INSERTing the VALUES string.
        try(Statement stmt = conn.createStatement();) {
            
            stmt.execute("INSERT INTO " + TABLE + " VALUES " + vehValues);
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.exit(-1);
        }
    }
    
    public ResultSet searchMake(String userWants) {
        
//        Double userDbl = Double.parseDouble(userWants);
//        Integer userInteger = Integer.parseInt(userWants);
//        if (userDbl.intValue().equals(userDbl)) {
//            
//        } else {
//        }

        String query = "Select * from " + TABLE 
                + "\nWHERE VEH.MAKE = '" + userWants + "' OR \n"
                + "WHERE VEH.MODEL = '" + userWants + "' OR \n"
                + "WHERE VEH.YEAR_INT = CAST('" + userWants + "' AS INTEGER) OR \n"
                + "WHERE VEH.PRICE = CAST('" + userWants + "' AS DOUBLE) OR \n"
                + "ORDER BY ID_VEH;";
            
        try (Connection conn = this.getConnection();) {
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            return rs;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.exit(-1);
        }
        
        return null;
    }
    
    public ResultSet searchType(String userWants) {
        
        String query = "Select * from " + TABLE 
                + "\nWHERE VEH.VEHICLETYPE = '" + userWants + "'\n"
                + "ORDER BY ID_VEH;";
        
        try (Connection conn = this.getConnection();) {
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            return rs;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.exit(-1);
        }
        
        return null;
    }
    
}
