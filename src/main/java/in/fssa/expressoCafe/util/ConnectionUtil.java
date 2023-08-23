package in.fssa.expressoCafe.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;
public class ConnectionUtil {
	public static Connection getConnnetion() {
		
        String url;
        String userName;
        String password;

        if (System.getenv("CI") != null) {
            url = System.getenv("DATABASE_HOSTNAME");
            userName = System.getenv("DATABASE_USERNAME");
            password = System.getenv("DATABASE_PASSWORD");
        } else {
//            Dotenv env = Dotenv.load();
//            url = env.get("DATABASE_HOSTNAME");
//            userName = env.get("DATABASE_USERNAME");
//            password = env.get("DATABASE_PASSWORD");
        	
        	url ="jdbc:mysql://164.52.216.41:3306/jayashree_selvarangam_corejava_project";
        	userName = "FJRF5pAEFMOa";
        	password = "31d3ab25-ff69-44ee-928a-5d4ef7de504c"; 
        }		
		Connection connection = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			 connection  = DriverManager.getConnection(url,userName,password);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return connection;
	}
	public static void close(Connection connection, PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void close(Connection connection, PreparedStatement ps, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}