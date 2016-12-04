package vehicleGarage;

/**
 *  CIS319 Advanced OOP with Java
 *  Course Project
 *
 *  This class is the representational object of a vehicle. 
 *  It is meant to be translated to and from storage.<br/>
 *  <br/>
 *  
 * @author  Chris Vought     countorlak@gmail.com
 * @since   11/29/2016
 */
public class Vehicle {
    
    private String make;
    private String model;
    private int year;
    private double price;
    private VehicleType type;
    
    
//    Make sure there are no useless instantiations lying around.
    private Vehicle(){}
    
//    Make sure it has all it needs from the get go.
    public Vehicle(String aMake, String aModel, int aYear, 
            double aPrice, VehicleType aType) {
        
        this.setMake(aMake);
        this.setModel(aModel);
        this.setPrice(aPrice);
        this.setYear(aYear);
        this.setType(aType);
    }
    

    /**
     * @return the make
     */
    public String getMake() {
        return make;
    }

    /**
     * @param make the make to set
     */
    public final void setMake(String make) {
        this.make = make;
    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public final void setModel(String model) {
        this.model = model;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public final void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public final void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the type
     */
    public VehicleType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public final void setType(VehicleType type) {
        this.type = type;
    }
    
    public boolean CheckCompletion() {
        
        boolean isComplete = false;
        
        if (this.getMake() != null) {
            
        }
        
        return isComplete;
    }
        
}
