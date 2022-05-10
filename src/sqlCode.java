import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;
import java.util.ArrayList;

public class sqlCode {


    public void insertBooking(String bookingRef, String dateFrom, String dateTo, String isoType, String quantity, String remarks, String vessel, String voyage, String pod, Connection connection, String tableName) throws SQLException {
        int ISOTypeID = 0;
        int ContainerCatID = 0;
        int ContainerTypeID = 0;

        switch (isoType) {
            case "2232", "22RT", "22RF" -> {
                ISOTypeID = 5;
                ContainerCatID = 1;
                ContainerTypeID = 1;
            }
            case "4532", "45RT", "45RF" -> {
                ISOTypeID = 6;
                ContainerCatID = 2;
                ContainerTypeID = 1;
            }
            case "4310", "43G1", "42GP", "42G0", "43G0" -> {
                ISOTypeID = 10;
                ContainerCatID = 2;
                ContainerTypeID = 2;
            }
            case "4510", "45G1", "45GP", "45G0" -> {
                ISOTypeID = 15;
                ContainerCatID = 2;
                ContainerTypeID = 9;
            }
            case "2210", "22G1", "22GP", "22G0" -> {
                ISOTypeID = 16;
                ContainerCatID = 1;
                ContainerTypeID = 2;
            }
            case "4351", "43UT", "4251", "42UT" -> {
                ISOTypeID = 19;
                ContainerCatID = 2;
                ContainerTypeID = 14;
            }
            case "2251", "22UT" -> {
                ISOTypeID = 21;
                ContainerCatID = 1;
                ContainerTypeID = 14;
            }
            case "2264", "22PC" -> {
                ISOTypeID = 23;
                ContainerCatID = 1;
                ContainerTypeID = 4;
            }
            case "4564", "45PC" -> {
                ISOTypeID = 24;
                ContainerCatID = 2;
                ContainerTypeID = 4;
            }
            case "4551", "45UT" -> {
                ISOTypeID = 31;
                ContainerCatID = 2;
                ContainerTypeID = 14;
            }
            case "42U6", "42UP", "43U6", "43UP" -> {
                ISOTypeID = 34;
                ContainerCatID = 2;
                ContainerTypeID = 15;
            }
            case "45U6", "45UP" -> {
                ISOTypeID = 35;
                ContainerCatID = 2;
                ContainerTypeID = 15;
            }
            case "22U6", "22UP" -> {
                ISOTypeID = 37;
                ContainerCatID = 1;
                ContainerTypeID = 15;
            }
            case "4363" -> {
                ISOTypeID = 39;
                ContainerCatID = 2;
                ContainerTypeID = 4;
            }
            case "25G1", "2510", "25GP" -> {
                ISOTypeID = 40;
                ContainerCatID = 1;
                ContainerTypeID = 9;
            }
        }

        // Hapag Lloyd customer master ID and username
        String client = "HPG001";
        String userName = "COPARN";


        try{
            PreparedStatement update = connection.prepareStatement("INSERT INTO " + tableName + " (PastelClientID, ReleaseRef, ReleaseValidFromDT, ReleaseValidToDT, ContainerTypeID, IsoTypeID, Quantity, Commodity, Remarks, ContainerCatID, Vessel, Voyage, POD, UserName, RecordDT) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            update.setString(1, client);
            update.setString(2, bookingRef);
            update.setString(3, dateFrom);
            update.setString(4, dateTo);
            update.setInt(5, ContainerTypeID);
            update.setInt(6, ISOTypeID);
            update.setString(7, quantity);
            update.setString(8, remarks);
            update.setString(9, remarks);
            update.setInt(10, ContainerCatID);
            update.setString(11, vessel);
            update.setString(12, voyage);
            update.setString(13, pod);
            update.setString(14, userName);
            update.setString(15, dateFrom);
            update.executeUpdate();
            System.out.println("\nBooking submitted\n");
        }catch (SQLServerException e){
            System.out.println("booking exists");
        }

    }

    public void deduction(Connection connection, String tableName, String bookingRef, String isoType, String quantity) throws SQLException {

        int ISOTypeID = 0;

        switch (isoType) {
            case "2232", "22RT", "22RF" -> ISOTypeID = 5;
            case "4532", "45RT", "45RF" -> ISOTypeID = 6;
            case "4310", "43G1" -> ISOTypeID = 10;
            case "4510", "45G1" -> ISOTypeID = 15;
            case "2210", "22G1" -> ISOTypeID = 16;
            case "4351", "43UT", "4251", "42UT" -> ISOTypeID = 19;
            case "2251", "22UT" -> ISOTypeID = 21;
            case "2264", "22PC" -> ISOTypeID = 23;
            case "4564", "45PC" -> ISOTypeID = 24;
            case "4551", "45UT" -> ISOTypeID = 31;
            case "42U6", "42UP", "43U6", "43UP" -> ISOTypeID = 34;
            case "45U6", "45UP" -> ISOTypeID = 35;
            case "22U6", "22UP" -> ISOTypeID = 37;
            case "4363" -> ISOTypeID = 39;
            case "25G1", "2510" -> ISOTypeID = 40;
        }
        Statement updateStatement = connection.createStatement();
        ResultSet resultsToUpdate;
        resultsToUpdate = updateStatement.executeQuery("SELECT * FROM " + tableName + " WHERE ReleaseRef like '" + bookingRef + "%' AND IsoTypeID = '" + ISOTypeID + "' ");

        while (resultsToUpdate.next()) {
            int deductQty = resultsToUpdate.getInt("Quantity");
            int qtyToDeduct = deductQty - Integer.parseInt(quantity);
            if(qtyToDeduct < 0){
                qtyToDeduct = 0;
            }
            String referenceToUpdate = resultsToUpdate.getString("ReleaseRef");
            PreparedStatement update = connection.prepareStatement("UPDATE " + tableName + " SET Quantity = ? WHERE ReleaseRef like ? AND IsoTypeID = ?");
            update.setInt(1, qtyToDeduct);
            update.setString(2, referenceToUpdate);
            update.setInt(3, ISOTypeID);
            update.executeUpdate();
            System.out.println("Quantity deducted from " + referenceToUpdate);
        }
    }

    // Size differentiator

    String sizeDiff;
    public String sizePicker(String isoType){
        switch (isoType) {
            case "22G1", "22GP", "2210", "22G0" -> sizeDiff = "-6GP";
            case "4310", "43G1", "42GP", "42G0", "43G0" -> sizeDiff = "-12GP";
            case "4510", "45G1", "45GP", "45G0" -> sizeDiff = "-12HC";
            case "2232", "22RT", "22RF" -> sizeDiff = "-6RT";
            case "4532", "45RT", "45RF" -> sizeDiff = "-12RT";
            case "4351", "43UT", "4251", "42UT" -> sizeDiff = "-12UT";
            case "2251", "22UT" -> sizeDiff = "-6UT";
            case "2264", "22PC" -> sizeDiff = "-6FR";
            case "4564", "45PC", "4363" -> sizeDiff = "-12FR";
            case "4551", "45UT" -> sizeDiff = "-12HUT";
            case "42U6", "42UP", "43U6", "43UP" -> sizeDiff = "-12HT";
            case "45U6", "45UP" -> sizeDiff = "-12HHT";
            case "22U6", "22UP" -> sizeDiff = "-6HT";
            case "25G1", "2510", "25GP" -> sizeDiff = "-6HGP";

        }
        return sizeDiff;
    }


    public void additions(Connection connection, String tableName, String bookingRef, String dateFrom, String dateTo, String isoType, String quantity, String remarks, String vessel, String voyage, String pod) throws SQLException {
        int ISOTypeID = 0;

        switch (isoType) {
            case "2232", "22RT", "22RF" -> ISOTypeID = 5;
            case "4532", "45RT", "45RF" -> ISOTypeID = 6;
            case "4310", "43G1", "42GP", "42G0", "43G0" -> ISOTypeID = 10;
            case "4510", "45G1", "45GP", "45G0" -> ISOTypeID = 15;
            case "2210", "22G1", "22GP", "22G0" -> ISOTypeID = 16;
            case "4351", "43UT", "4251", "42UT" -> ISOTypeID = 19;
            case "2251", "22UT" -> ISOTypeID = 21;
            case "2264", "22PC" -> ISOTypeID = 23;
            case "4564", "45PC" -> ISOTypeID = 24;
            case "4551", "45UT" -> ISOTypeID = 31;
            case "42U6", "42UP", "43U6", "43UP" -> ISOTypeID = 34;
            case "45U6", "45UP" -> ISOTypeID = 35;
            case "22U6", "22UP" -> ISOTypeID = 37;
            case "4363" -> ISOTypeID = 39;
            case "25G1", "2510", "25GP" -> ISOTypeID = 40;
        }
        // checking if the booking exists so that we don't duplicated the booking
        Statement bookingStatement = connection.createStatement();
        ResultSet ifSqlBookingExists = bookingStatement.executeQuery("SELECT ReleaseRef, ISOTypeID FROM " + tableName + " WHERE ReleaseRef = '" + bookingRef + "'");

        String currentBooking = null;
        String currentBookingIso = null;

        ArrayList<String> bookingList = new ArrayList<>();
        while (ifSqlBookingExists.next()){
            currentBooking = ifSqlBookingExists.getString("ReleaseRef");
            currentBookingIso = String.valueOf(ifSqlBookingExists.getInt("ISOTypeID"));
            bookingList.add(currentBooking);
            System.out.println("sql booking check " + currentBooking);
            System.out.println("current iso check " + currentBookingIso);
            System.out.println();
        }

        System.out.println("booking list " + bookingList);


        // getting the booking that matches coparn booking reference with iso type for updates
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT * FROM " + tableName + " WHERE ReleaseRef LIKE '" + bookingRef + "%' AND ISOTypeID = '" + ISOTypeID + "'");
        String sqlBookingToUpdate = null;
        int sqlIso = 0;
        int addQty = 0;
        while (results.next()) {
            addQty = results.getInt("Quantity");
            sqlBookingToUpdate = results.getString("ReleaseRef");
            sqlIso = results.getInt("ISOTypeID");
        }
        System.out.println(sqlBookingToUpdate + " booking to update");
        System.out.println(sqlIso + " ISO to update");


        if (currentBooking == null){

            System.out.println("booking does not exist, creating booking");
            insertBooking(bookingRef, dateFrom, dateTo, isoType, quantity, remarks, vessel, voyage, pod, connection, tableName);
            System.out.println("check 1");

        }else if(!currentBookingIso.equals(String.valueOf(ISOTypeID))){
            System.out.println("booking does not exist, creating booking");
            String newBooking = bookingRef + sizePicker(isoType);
            System.out.println(newBooking);
            insertBooking(newBooking, dateFrom, dateTo, isoType, quantity, remarks, vessel, voyage, pod, connection, tableName);

            System.out.println("check 2");

        }
        else{
            System.out.println("running else statement");

            System.out.println("sql booking check2 " + sqlBookingToUpdate);
            System.out.println("sql iso check2 " + sqlIso + "\n");

            System.out.println("Record exists, updating new details");
            Statement updateStatement = connection.createStatement();
            ResultSet updateResult = updateStatement.executeQuery("SELECT * FROM " + tableName + " WHERE ReleaseRef = '"+ sqlBookingToUpdate +"'");

            String dateF = null;
            String dateT = null;
            int iso = 0;
            String ves = null;
            String voy = null;
            String comm = null;
            String remrks = null;
            String Qty = null;

            while (updateResult.next()){
                dateF = updateResult.getString("ReleaseValidFromDT");
                dateT = updateResult.getString("ReleaseValidToDT");
                iso = updateResult.getInt("ISOTypeID");
                ves = updateResult.getString("Vessel");
                voy = updateResult.getString("Voyage");
                comm = updateResult.getString("Commodity");
                remrks = updateResult.getString("Remarks");
                Qty = updateResult.getString("Quantity");

            }

            assert dateF != null;
            if (dateF.equals(dateFrom)){
                System.out.println("date from is the same");
            }else{
                PreparedStatement update = connection.prepareStatement("UPDATE " + tableName + " SET ReleaseValidFromDT = ? WHERE ReleaseRef = ?");
                update.setString(1, dateFrom);
                update.setString(2, sqlBookingToUpdate);
                update.executeUpdate();
                System.out.println("date from updated");
            }

            if (dateT.equals(dateTo)){
                System.out.println("date to is the same");
            }else{
                PreparedStatement update = connection.prepareStatement("UPDATE " + tableName + " SET ReleaseValidToDT = ? WHERE ReleaseRef = ?");
                update.setString(1, dateTo);
                update.setString(2, sqlBookingToUpdate);
                update.executeUpdate();
                System.out.println("date to updated");
            }

            // Checks if iso type is the same, if it's not, we create a booking with new iso type
            if (iso == ISOTypeID){
                System.out.println("iso type is the same");
            }else{
                PreparedStatement update = connection.prepareStatement("UPDATE " + tableName + " SET ISOTypeID = ? WHERE ReleaseRef = ?");
                update.setInt(1, ISOTypeID);
                update.setString(2, sqlBookingToUpdate);
                update.executeUpdate();
                System.out.println("iso type updated");
            }

            if (ves.equals(vessel)){
                System.out.println("vessel is the same");
            }else{
                PreparedStatement update = connection.prepareStatement("UPDATE " + tableName + " SET Vessel = ? WHERE ReleaseRef = ?");
                update.setString(1, vessel);
                update.setString(2, sqlBookingToUpdate);
                update.executeUpdate();
                System.out.println("vessel updated");
            }

            if (voy.equals(voyage)){
                System.out.println("voyage is the same");
            }else{
                PreparedStatement update = connection.prepareStatement("UPDATE " + tableName + " SET Voyage = ? WHERE ReleaseRef = ?");
                update.setString(1, voyage);
                update.setString(2, sqlBookingToUpdate);
                update.executeUpdate();
                System.out.println("voyage updated");
            }

            if (comm.equals(remarks)){
                System.out.println("commodity is the same");
            }else{
                PreparedStatement update = connection.prepareStatement("UPDATE " + tableName + " SET Commodity = ? WHERE ReleaseRef = ?");
                update.setString(1, remarks);
                update.setString(2, sqlBookingToUpdate);
                update.executeUpdate();
                System.out.println("commodity updated");
            }

            if (remrks.equals(remarks)){
                System.out.println("remarks is the same");
            }else{
                PreparedStatement update = connection.prepareStatement("UPDATE " + tableName + " SET Remarks = ? WHERE ReleaseRef = ?");
                update.setString(1, remarks);
                update.setString(2, sqlBookingToUpdate);
                update.executeUpdate();
                System.out.println("remarks updated");
            }
            if (!Qty.equals(quantity)){
                System.out.println("quantity is the same");
            }else{
                System.out.println("hello");
                System.out.println(addQty);
                int qtyToAdd = addQty + Integer.parseInt(quantity);
                System.out.println(qtyToAdd);

                PreparedStatement update = connection.prepareStatement("UPDATE " + tableName + " SET Quantity = ? WHERE ReleaseRef = ?");
                update.setInt(1, qtyToAdd);
                update.setString(2, sqlBookingToUpdate);
                update.executeUpdate();

            }
        }
    }

    public void deletion(Connection connection, String tableName, String bookingRef) throws SQLException {
        PreparedStatement delete = connection.prepareStatement("DELETE FROM " + tableName + " WHERE ReleaseRef='" + bookingRef + "'");
        delete.setString(1, bookingRef);
        delete.executeUpdate();
        System.out.println("booking deleted");
    }
}


