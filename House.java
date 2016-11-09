import java.util.Scanner;

public class House{
	//house condiction
	public int floorSize;
	public int lot;
	public int year;
	public int bedNumber;
	public int bathNumber;

	//address
	public String zipcode;
	public String state;
	public String city;
	public String address;


	private int salePrice;
	private int basePrice;
	private int predictPrice;

	//Coordinates
	//latitude
	public double lat;
	//longitude
	public double lng;

	//enter house info by terminal if no parameter
	//NOT SAFE, NEED FURTHER EDITING
	public House(){
	}


	public void setByTerminal(){
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter address: ");
		address = scanner.next();
		System.out.println("Please enter city: ");
		city = scanner.next();
		System.out.println("Please enter zipcode: ");
		zipcode = scanner.next();
		System.out.println("Please enter floor size (sqft): ");
		floorSize = scanner.nextInt();
		System.out.println("Please enter number of bedrooms: ");
		bedNumber = scanner.nextInt();
		System.out.println("Please enter number of bathrooms: ");
		bathNumber = scanner.nextInt();
	}
	//count distance between two houses using their coordinates
	public double getDirectDistance(House h){
		double EARTH_RADIUS = 6378.137;
	    double radLat1 = h.lat * Math.PI / 180.0;
	    double radLat2 = this.lat * Math.PI / 180.0;
	    double a = radLat1 - radLat2;
	    double b = (h.lng * Math.PI / 180.0) - (this.lng * Math.PI / 180.0);
	    double s = 2.0 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	    s = s * EARTH_RADIUS;
	    s = Math.round(s * 10000.0) / 10000.0;
		System.out.println(s);
	    return s;
	}

	public void setFloorSize(int f){
		floorSize = f;
	}
	public int getFloorSize(){
		return floorSize;
	}
	public void setBedNumber(int b){
		bedNumber = b;
	}
	public int getBedNumber(){
		return bedNumber;
	}
	public void setBathNumber(int b){
		bathNumber = b;
	}
	public int getBathNumber(){
		return bathNumber;
	}
	public void setSalePrice(int price){
		salePrice = price;
	}
	public int getSalePrice(){
		return salePrice;
	}
	public void setBasePrice(int price){
		basePrice = price;
	}
	public int getBasePrice(){
		return basePrice;
	}
	public String getAddress(){
		return address;
	}
	public void setPredictPrice(int p){
		predictPrice = p;
	}
	public int getPredictPrice(){
		return predictPrice;
	}
}
