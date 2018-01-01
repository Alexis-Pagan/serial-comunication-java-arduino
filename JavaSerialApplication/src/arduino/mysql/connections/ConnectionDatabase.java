package arduino.mysql.connections;

import java.sql.Connection;			
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ConnectionDatabase extends JFrame {

	private static final long serialVersionUID = 56514L;
	private String user = "your name of profile database";
	private String pass = "your password";
	private String databaseName = "name_of_database?useSSL=false";
	private static Connection con;

	public Connection performConnectionDB() {

		try {
			String newConnectionURL = "jdbc:mysql://127.0.0.1:3306/" + databaseName;
			con = DriverManager.getConnection(newConnectionURL, user, pass);
			
			if(con != null) {
				JOptionPane.showMessageDialog(null, "Connected to database");
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "!Connected to database");
		}

		return con;
	}

	public static void main(String[] args) {
		ConnectionDatabase connection = new ConnectionDatabase();
		connection.performConnectionDB();
	}
}