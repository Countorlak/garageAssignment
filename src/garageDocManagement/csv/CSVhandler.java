package garageDocManagement.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.*;
import storage.JDBrecords;
import vehicleGarage.Vehicle;
import vehicleGarage.VehicleType;
import static vehicleGarage.VehicleType.*;

/**
 *  CIS319 Advanced OOP with Java
 *  Course Project
 *
 *  This class manages csv files mainly for import. It has extra methods to 
 *  allow for more general purpose use later on.
 *  Only one file per instance.<br/>
 * <br/>
 * 
 * @author  Chris Vought     countorlak@gmail.com
 * @since   11/29/2016
 */
public class CSVhandler {
    
    private File document;

    
    public CSVhandler(File file) {
        this.document = file;
    }
    
    
    /**
     * @return the document
     */
    public File getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    public void setDocument(File document) {
        this.document = document;
    }
    
//    Use Apache Commons CSV to parse the document and store document in JavaDB.
    public void storeDocument() {
        
        ArrayDeque<Vehicle> vehFIFO = new ArrayDeque();
        Iterable<CSVRecord> records = null;
        
        try (final FileReader fr = new FileReader(this.document)) {
            
            records = CSVFormat.RFC4180.withHeader(CSV_Header.class).parse(fr);

            for (CSVRecord entry : records) {

                VehicleType currentVehicleType = OTHER;

                for (VehicleType v : VehicleType.values()) {
                    if (v.name().equals(
                            entry.get(CSV_Header.TYPE).toUpperCase())) {
                        currentVehicleType = v;
                    }
                }

                Vehicle csvVehicle = new Vehicle(
                        entry.get(CSV_Header.MAKE),
                        entry.get(CSV_Header.MODEL),
                        Integer.parseInt(entry.get(CSV_Header.YEAR)),
                        Double.parseDouble(entry.get(CSV_Header.PRICE)),
                        currentVehicleType
                );
                
//              Push it onto the stack/FIFO.
                vehFIFO.add(csvVehicle);
                
            }
        } catch (FileNotFoundException fnfe) {
            Logger.getLogger(CSVhandler.class.getName())
                    .log(Level.SEVERE, null, fnfe);
        } catch (IOException ioe) {
            Logger.getLogger(CSVhandler.class.getName())
                    .log(Level.SEVERE, null, ioe);
        }
        
        JDBrecords jdbr = new JDBrecords();
        try {
            Connection conn = jdbr.getConnection();
            vehFIFO.forEach((vehicle) -> {
                jdbr.addSingleEntry(vehicle,conn);
            });
            
        } catch (SQLException ex) {
            Logger.getLogger(CSVhandler.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
    }
    
}