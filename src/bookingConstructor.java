import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class bookingConstructor {

    String tableName = "tblReleaseRef";

    public void makeBooking(String fileName, Connection connection) throws FileNotFoundException, SQLException {

        File inputFile = new File("C:\\COPARNS\\COPARNISOHLCL\\coparn\\in\\" + fileName);

        Scanner reader = new Scanner(inputFile);

        ArrayList<String> list = new ArrayList<>();

        ArrayList<String> splitListUNT = new ArrayList<>();

        while (reader.hasNextLine()) {
            String[] line = reader.nextLine().split("UNT");
            list.addAll(Arrays.asList(line));
            System.out.println(Arrays.toString(line));

        }
        reader.close();

        sqlCode getSQLCode = new sqlCode();
        SingleCoparnFile singleCoparnFile = new SingleCoparnFile();
        MultipleCoparnFile multipleCoparnFile = new MultipleCoparnFile();

        // creating a new arraylist to read multiple lines in the file
        // this list will be used to get all the info from the coparn file in the below nested for loops in if statement
        for (String s : list) {
            if (s.contains("UNH")) {
                splitListUNT.add(s);
            }
        }

        // here we check the size if the arraylist, if there is one UHN block, we crate entry for one
        // if there is more than one UHN block, we accommodate all UHN blocks
        if(splitListUNT.size() == 1){
            singleCoparnFile.singleCoparn(connection, splitListUNT, tableName);

        }else if (splitListUNT.size() > 1){
            multipleCoparnFile.multiCoparn(connection, splitListUNT, tableName);

        }
    }



}
