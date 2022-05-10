import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

public class folderCode {

    bookingConstructor bookingCreator = new bookingConstructor();

    serverConnection connect = new serverConnection();

    // This method copies the processed file to a different location
    public void copyToTargetFolder(String fileName) throws IOException {
        // assigning path names for target and source
        Path source = Paths.get(String.format("C:\\COPARNS\\COPARNISOHLCL\\coparn\\in\\%s", fileName));
        Path target = Paths.get(String.format("C:\\COPARNS\\COPARNISOHLCL\\coparn\\out\\%s", fileName));


        // copying the file after it's been processed
        try{
            Files.copy(source, target);
            System.out.println("File copied successfully");
        }catch (IOException E){
            System.out.println("File failed to copy");
        }
    }

    public void moveFile(String fileName) {
        try{
            Path source = Paths.get(String.format("C:\\COPARNS\\COPARNISOHLCL\\coparn\\in\\%s", fileName));
            Path target = Paths.get(String.format("C:\\COPARNS\\COPARNISOHLCL\\coparn\\out\\%s", fileName));
            Path bytes = Files.move(
                    new File(String.valueOf(source)).toPath(),
                    new File(String.valueOf(target)).toPath(),
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File moved successfully");
        }catch (IOException E){
            System.out.println("File failed to move");
        }
    }

    // This method reads and formats the file
    public void readFiles() throws IOException, SQLException {
        Connection myConnection = connect.connectToServer();

        String fileName = null;
        File inputFile = new File("C:\\COPARNS\\COPARNISOHLCL\\coparn\\in");
        File doneFolder = new File("C:\\COPARNS\\COPARNISOHLCL\\coparn\\out");
        File[] files = inputFile.listFiles();
        System.out.println(Arrays.toString(files));

        // Fetching all the files
        /*assert files != null;*/

        for (File file : Objects.requireNonNull(files)) {

            fileName = file.getName();
            if (file.isFile()) {
                String line;
                BufferedReader inputStream = new BufferedReader(new FileReader(file));
                try  {
                    while ((line = inputStream.readLine()) != null) {
                        System.out.println("\n" + fileName + " has been processed");
                        System.out.println(fileName);

                        bookingCreator.makeBooking(fileName, myConnection);
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
                inputStream.close();
            }
            moveFile(fileName);
        }
    }
}
