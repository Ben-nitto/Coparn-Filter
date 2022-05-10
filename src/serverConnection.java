import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class serverConnection {

    public Connection connectToServer() throws SQLException {

        // Insert server connection string here
        String serverURL = "";

        return DriverManager.getConnection(serverURL);
    }
}
