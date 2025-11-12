package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection
{
        private static String url;
        private static String username;
        private static String password; // configure mysql to have user "root" with password: "12345"

        static
        {
            url = "jdbc:mysql://localhost:3306/public_utility_billing_system";
            username = "root";
            password = "12345";
        }

        public static Connection getConnection() throws SQLException
        {
            return DriverManager.getConnection(url, username, password);
        }

}
