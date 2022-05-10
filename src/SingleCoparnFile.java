import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SingleCoparnFile {

    String UNB = null;
    String UNH = null;
    String BGM = null;
    String RFF = null; //need booking ref RFF+BN:
    ArrayList<String> TDT = new ArrayList<>(); // need this is vessel and voyage
    String RFFVON = null; //RFF+VON:
    String NAD = null; // dont need name and address
    String EQD = null; // need, size type
    String RFFSQ = null; // RFF+SQ
    String EQN = null; // need, qty
    String DTM = null; // need date
    String LOC = null; //
    String MEA = null; // measurement
    String DIM = null; //
    String TMP = null; // need temp
    String RNG = null; // need this
    ArrayList<String> FTX = new ArrayList<>(); // need
    String DGS = null; //
    String CNT = null;//
    String UNT = null;//
    String UNZ = null;//

    public void singleCoparn(Connection connection, ArrayList<String> splitListUNT, String tableName) throws SQLException {
        sqlCode getSQLCode = new sqlCode();

        for (String h : splitListUNT){
            String [] line = h.split("'");

            for (String s : line) {
                if (s.contains("UNB")) {
                    UNB = s;
                }
            }
            for (String s : line) {
                if (s.contains("UNH")) {
                    UNH = s;
                }
            }
            for (String s : line) {
                if (s.contains("BGM")) {
                    BGM = s;
                }
            }
            for (String s : line) {
                if (s.contains("RFF+BN")) {
                    RFF = s;
                }
            }
            for (String s : line) {
                if (s.contains("TDT")){
                    TDT.add(s);
                }
            }
            for (String s : line) {
                if (s.contains("RFF+VON")) {
                    RFFVON = s;
                }
            }
            for (String s : line) {
                if (s.contains("NAD")) {
                    NAD = s;
                }
            }
            for (String s : line) {
                if (s.contains("EQD")) {
                    EQD = s;
                }
            }
            for (String s : line) {
                if (s.contains("RFF+SQ")) {
                    RFFSQ = s;
                }
            }
            for (String s : line) {
                if (s.contains("EQN")) {
                    EQN = s;
                }
            }
            for (String s : line) {
                if (s.contains("DTM")) {
                    DTM = s;
                }
            }
            for (String s : line) {
                if (s.contains("LOC")) {
                    LOC = s;
                }
            }
            for (String s : line) {
                if (s.contains("MEA")) {
                    MEA = s;
                }
            }
            for (String s : line) {
                if (s.contains("DIM")) {
                    DIM = s;
                }
            }
            for (String s : line) {
                if (s.contains("TMP")) {
                    TMP = s;
                }
            }
            for (String s : line) {
                if (s.contains("RNG")) {
                    RNG = s;
                }
            }
            for (String s : line) {
                if (s.contains("FTX")) {
                    FTX.add(s);
                }
            }
            for (String s : line) {
                if (s.contains("DGS")) {
                    DGS = s;
                }
            }
            for (String s : line) {
                if (s.contains("CNT")) {
                    CNT = s;
                }
            }
            for (String s : line) {
                if (s.contains("UNT")) {
                    UNT = s;
                }
            }
            for (String s : line) {
                if (s.contains("UNZ")) {
                    UNZ = s;
                }
            }

            // BGM check
            assert BGM != null;
            String BGMCheck = BGM.replace("+", ":");
            String [] BGMList = BGMCheck.split(":");
            String BGMNum = BGMList[6];
            System.out.println("BGM: " + BGMList[6]);

            String client = "";
            String bookingRef = null;
            String isoType = null;
            String vessel = null;
            String voyage = null;
            String quantity = null;
            String remarks = null;
            String pod = "";
            String dateFrom = null;
            String dateTo = null;


            if(BGMNum.equals("1")){
                // booking variables
                client = "HAPAG";
                System.out.println("client: " + client);

                // booking reff
                String remPlusRff = RFF.replace("+", ":");
                String remMinus = remPlusRff.replace("-", ":");
                String [] refList = remMinus.split(":");
                bookingRef = refList[2];
                System.out.println("Booking no: " + bookingRef);

                // iso code
                String remPlusIso = EQD.replace("+", ":");
                String [] isoList = remPlusIso.split(":");
                isoType = isoList[3];
                System.out.println("Iso: " + isoType);

                // quantity
                String remPlusQTY = EQN.replace("+", ":");
                String [] QTY = remPlusQTY.split(":");
                quantity = QTY[1];
                System.out.println("QTY: " + quantity);
            }else{
                // booking variables
                client = "HAPAG";
                System.out.println("client: " + client);

                // booking reff
                String remPlusRff = RFF.replace("+", ":");
                String remMinus = remPlusRff.replace("-", ":");
                String [] refList = remMinus.split(":");
                bookingRef = refList[2];
                System.out.println("Booking no: " + bookingRef);

                // iso code
                String remPlusIso = EQD.replace("+", ":");
                String [] isoList = remPlusIso.split(":");
                isoType = isoList[3];
                System.out.println("Iso: " + isoType);

                // vessel
                String [] vAndvList = {};
                if(TDT == null){
                    vessel = "TBA";
                    voyage = "TBA";

                }else{
                    String vAndv = String.valueOf(TDT);
                    String vAndvrembrk1 = vAndv.replace("]", "");
                    String vAndvrembrk2 = vAndvrembrk1.replace("[", "");
                    String vAndvrembrk3 = vAndvrembrk2.replace("[", "");
                    String vAndvrembrk4 = vAndvrembrk3.replace("]", "");
                    String remPlusVandV = vAndvrembrk4.replace("+", ":");
                    vAndvList = remPlusVandV.split(":");
                    // voyage
                    voyage = vAndvList[2];
                    vessel = "";

                    if (voyage.equals("Transporter") || voyage.equals("TRANSPORTER") || voyage.equals("transporter")) {
                        vessel = "TRANSPORTER";
                    } else {
                        vessel = vAndvList[13].replace(", TDT", "");

                        System.out.println("Vessel: " + vessel);
                    }
                }

                // pod
                pod = "";
                // quantity
                String remPlusQTY = EQN.replace("+", ":");
                String [] QTY = remPlusQTY.split(":");
                quantity = QTY[1];
                System.out.println("QTY: " + quantity);

                String[] gpList = {"4310", "43G1", "4510", "45G1", "2210", "22G1", "4351", "43UT", "4251", "42UT", "2251", "22UT", "2264", "22PC","4564", "45PC", "4551", "45UT", "42U6", "42UP", "43U6", "43UP", "45U6", "45UP", "22U6", "22UP", "4363", "25G1", "2510"};
                ArrayList<String> listOfGp = new ArrayList<String>(List.of(gpList));


                // remarks and commodity
                if(FTX.isEmpty()){
                    remarks = "TBA";
                    System.out.println("Remarks and com: " + remarks);
                }else{
                    if(listOfGp.contains(isoType)){
                        String atmosphere = String.valueOf(FTX);
                        String replaceFwrdSlash = atmosphere.replace(", ", "/");
                        String remFtx = replaceFwrdSlash.replace("FTX+", "");
                        String remZZZ = remFtx.replace("ZZZ+++", "");
                        String remAAI = remZZZ.replace("AAI+++", "");
                        String remAAA = remAAI.replace("AAA+++", "");
                        String replaceBrk1 = remAAA.replace("[", "");
                        String fileRemarks = replaceBrk1.replace("]", "");

                        int checkLength = fileRemarks.length();
                        if(checkLength > 100){
                            remarks = fileRemarks.substring(0, 100);
                            System.out.println("Remarks and com: " + remarks);
                        }else{
                            remarks = fileRemarks;
                            System.out.println("Remarks and com: " + remarks);
                        }


                    }else{
                        if(TMP == null){
                            String atmosphere = String.valueOf(FTX);
                            String replaceFwrdSlash = atmosphere.replace(", ", "/");
                            String remFtx = replaceFwrdSlash.replace("FTX+", "");
                            String remZZZ = remFtx.replace("ZZZ+++", "");
                            String remAAI = remZZZ.replace("AAI+++", "");
                            String remAAA = remAAI.replace("AAA+++", "");
                            String replaceBrk1 = remAAA.replace("[", "");
                            String fileRemarks = replaceBrk1.replace("]", "");
                            int checkLength = fileRemarks.length();

                            if(checkLength > 100){
                                remarks = fileRemarks.substring(0, 100);
                                System.out.println("Remarks and com: " + remarks);
                            }else{
                                remarks = fileRemarks;
                                System.out.println("Remarks and com: " + remarks);
                            }

                        }else{
                            String remTMP = TMP.replace("TMP+2+","");
                            String atmosphere = String.valueOf(FTX);
                            String replaceFwrdSlash = atmosphere.replace(", ", "/");
                            String remFtx = replaceFwrdSlash.replace("FTX+", "");
                            String remZZZ = remFtx.replace("ZZZ+++", "");
                            String remAAI = remZZZ.replace("AAI+++", "");
                            String remAAA = remAAI.replace("AAA+++", "");
                            String replaceBrk1 = remAAA.replace("[", "");
                            String fileRemarks = replaceBrk1.replace("]", "");
                            int checkLength = fileRemarks.length();

                            if(checkLength > 100){
                                remarks = remTMP +"/" + fileRemarks.substring(0,100);
                                System.out.println("Remarks and com: " + remarks);
                            }else{
                                remarks = fileRemarks;
                                System.out.println("Remarks and com: " + remarks);
                            }

                        }

                    }
                }

                // date from
                String [] getDate = DTM.split(":");
                String fileDate= getDate[1];
                String charToAdd = "-";
                String splitYear = fileDate.substring(0,4) + charToAdd + fileDate.substring(4);
                dateFrom = splitYear.substring(0,7) + charToAdd + splitYear.substring(7);
                System.out.println("Date from " + dateFrom);

                // date to
                LocalDate date = LocalDate.parse(dateFrom);
                LocalDate dateTillNexMonth = date.plusMonths(1);
                dateTo = dateTillNexMonth.toString();
                System.out.println("Date to " + dateTo);
                System.out.println("\n");

            }

            switch (BGMNum) {

                // deduction
                case "1" -> getSQLCode.deduction(connection, tableName,bookingRef,isoType,quantity);
                // additions - add qty and size types
                case "2" -> getSQLCode.additions(connection, tableName, bookingRef, dateFrom, dateTo, isoType, quantity, remarks, vessel, voyage, pod);
                // delete booking
                case "3" -> getSQLCode.deletion(connection, tableName, bookingRef);
                // ignore, redelivery
                case "5" -> System.out.println("redelivery");

                // creating a booking
                case "9" -> getSQLCode.insertBooking(bookingRef, dateFrom, dateTo, isoType, quantity, remarks, vessel, voyage, pod, connection, tableName);
            }
        }

    }
}
