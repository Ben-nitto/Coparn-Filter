import java.io.IOException;
import java.sql.SQLException;

public class run {

    public static void main(String [] args) throws IOException, SQLException {

        folderCode features = new folderCode();

        features.readFiles();
    }
}