package predictor.model.data;

import java.io.Serializable;
import java.util.Date;

public class House implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	// house address
    public String address;
    public String city;
    public String state;
    public String zip;

    // basic house info
    public double floorSize;
    public double lotSize;
    public double bedroomNumber;
    public double bathroomNumber;
    public double latitude;
    public double longitude;
    public int builtYear;

    // sold info
    public boolean recentlySold;
    public Double lastSoldPrice;
    public Date lastSoldDate;

    // price
    public Double predictPrice;

    public double similarity;

    // non standard features
    public String discription;
	public String houseType;
	public String heating;
	public String cooling;
	public String parking;
	public String features[];

    public House(){
    }
    
}
