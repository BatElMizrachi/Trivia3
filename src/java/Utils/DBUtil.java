
package Utils;

import java.sql.*;

public class DBUtil {
    private static Connection connection = null;
    public static Connection getConnection() {  
  
        if (connection != null)  
        {
            return connection;  
        }
        else 
        {  
            try 
            {  
                Class.forName("org.apache.derby.jdbc.ClientDriver");  
                String urlCn ="jdbc:derby://localhost:1527/TriviaDB";
                connection = DriverManager.getConnection(urlCn, "TriviaDB", "TriviaDB");  
            } 
            catch (Exception e) 
            {  
                e.printStackTrace();  
            } 
            return connection;  
        }  
    }
    
}
