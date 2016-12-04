package vehicleGarage;

import garageDocManagement.csv.CSVhandler;
import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import storage.JDBrecords;
import static vehicleGarage.VehicleType.OTHER;

/**
 *  CIS319 Advanced OOP with Java
 *  Course Project
 *
 *  This class is the front end for the user and acts as a basic command line
 * read-eval-print-loop to guide the user through the desired operations.<br/>
 * <br/>
 * 
 * @author  Chris Vought     countorlak@gmail.com
 * @since   11/29/2016
 */
public class UserREPL {

    public static final String QUIT = "quit";
    public static final String LIST = "list";
    public static final String SEARCHTYPE = "search_type";
    public static final String SEARCH = "search";
    public static final String IMPORT_CSV = "import";
    public static final String ADD = "add";
    public static final String INTRO = "intro";


    private void repl() {

        Scanner userIn = new Scanner(System.in);

        do{
            displayReplHelp(INTRO);
//            Take user input and make sure it matches needed case.
            String input = userIn.next().toLowerCase();

            switch(input) {
                case ADD:
                    
                    displayReplHelp(ADD);
                    JDBrecords jdbrAdder = new JDBrecords();
                    
                    try {
                        Connection jdbrConn = jdbrAdder.getConnection();
                        jdbrAdder.addSingleEntry(
                                createSingleVehicle(userIn), 
                                jdbrConn
                        );
                        
                    } catch (SQLException sqle) {
                        sqle.printStackTrace();
                        System.exit(-1);
                    }
                    
                        
                    break;
                    
                case IMPORT_CSV:
                    
                    displayReplHelp(IMPORT_CSV);
                    System.out.println("Please enter readable csv file path.");
                    
                    File userPath;
                    String filePathString;
                    
                    try{
//                        Make sure user's entry is valid before proceeding.
                        do{
                            String userFilePath = userIn.next();
                            userPath = new File(userFilePath);
                            filePathString = userPath.getCanonicalPath();
                        } while(!userPath.exists() || !userPath.canRead());
                        
//                        The proceed to CSV handling.
                        CSVhandler csvProcessing = new CSVhandler(new File(filePathString));
                        csvProcessing.storeDocument();
                    } catch (NoSuchFileException nsfe) {
                        nsfe.printStackTrace();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    break;
                    
                case SEARCH:
                    
                    displayReplHelp(SEARCH);
                    JDBrecords jdbrSearch = new JDBrecords();
//                    jdbrSearch.search(userIn.next());
                    break;
                    
                case SEARCHTYPE:
                    
                    displayReplHelp(SEARCHTYPE);
                    JDBrecords jdbrSearchType = new JDBrecords();
                    jdbrSearchType.searchType(userIn.next());
                    break;
                    
                case LIST:
                    
                    displayReplHelp(LIST);
                    JDBrecords jdbr = new JDBrecords(); 
                    try {
                        jdbr.viewAll(jdbr.getConnection());
                    } catch (SQLException sqle) {
                        sqle.printStackTrace();
                        System.exit(-1);
                    }
                    break;
                    
                case QUIT:
                    
                    System.exit(0);
                    break;
                    
                default:
                    
                    displayReplHelp(INTRO);
                    break;
            }
            
//      Keep it going if input is neither null or QUIT.
        } while (userIn == null ? QUIT != null : !userIn.equals(QUIT));
    }

//    The method used to familiarize the user with operation.
    public void displayReplHelp(String optionCase) {
        
        switch (optionCase) {
            case ADD:

                System.out.println("Entries must be complete before moving on."
                        + "\n* Note *"
                        + "\nThere is no way to escape this operation until"
                        + " finished except to terminate the terminal."
                        + "\n-----");
                break;
                
            case IMPORT_CSV:

                System.out.println("Supported CSV format must match:"
                        + "\nMAKE,MODEL,YEAR,PRICE,TYPE"
                        + "\nThey must be delimited by only commas."
                        + "\nThey must be in order."
                        + "\nEntries will only be validated by data type."
                        + "\n-----");
                break;
                
            case SEARCH:

                System.out.println("Enter a make, model, year, or price value"
                        + " to retrieve all matching entries."
                        + "\n-----");
                break;
                
            case SEARCHTYPE:

                System.out.println("Valid types are in upper case as follows:"
                        + "\nCAR"
                        + "\nTRUCK"
                        + "\nSUV"
                        + "\nVAN"
                        + "\nCROSSOVER"
                        + "\nOTHER"
                        + "\n-----");
                break;
                
            case LIST:

                System.out.println("All entries are as follows:\n");
                break;
                
            case INTRO:
                
                System.out.println("This program is a cli front end for an"
                        + " included database\n where you can manipulate entries"
                        + " by adding, listing all,\n searching them by make,"
                        + " model, year, price or, \nseparately, by type,"
                        + " and even import properly formed CSV documents."
                        + "\n-----");
                
                System.out.println("Please specify one of the following operations:"
                        + "\nadd            - Append single entry."
                        + "\nlist           - List all entries."
                        + "\nsearch         - Search by make, model, year, or price"
                        + "\nsearch_type    - Search by vehicle type."
                        + "\nimport         - Import properly formed CSV document."
                        + "\n-----");
                break;
//          
            default:
                
                System.out.println("Please specify one of the following operations:"
                        + "\nadd            - Append single entry."
                        + "\nlist           - List all entries."
                        + "\nsearch         - Search by make, model, year, or price"
                        + "\nsearch_type    - Search by vehicle type."
                        + "\nimport         - Import properly formed CSV document."
                        + "\n-----");
                break;
        }
        
    }
    
    public static Vehicle createSingleVehicle(Scanner user) {
        
        String currentMake;
        String currentModel;
        int currentYear;
        double currentPrice;
        VehicleType currentType;
        
        System.out.println("Please enter Make.");
        currentMake = user.next();
        System.out.println("Please enter Model.");
        currentModel = user.next();
        System.out.println("Please enter Year.");
        currentYear = user.nextInt();
        System.out.println("Please enter Price.");
        currentPrice = user.nextDouble();
        System.out.println("Please enter Type.");
        currentType = fromString(user.next());
        
        
        Vehicle v = new Vehicle(
                currentMake,
                currentModel,
                currentYear,
                currentPrice,
                currentType
                );
        
        return v;
    }
    
//    Need's to find a home!
    public static VehicleType fromString(String s) {
        VehicleType currentVehicleType = OTHER;

        for (VehicleType v : VehicleType.values()) {
            if (v.name().equals(s)) {
                currentVehicleType = v;
            }
        }
        
        return currentVehicleType;
    }

    public static void main(String[] args) {
        
        UserREPL user = new UserREPL();
        user.repl();

    }
}
