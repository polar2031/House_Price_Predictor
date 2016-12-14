import java.io.*;
import java.util.Scanner;

import com.google.gson.*;
import org.jsoup.*;


public class House{
	//house condiction
	public int floorSize;
	public int lotSize;
	public int year;
	public int bedNumber;
	public int bathNumber;

	//address
	public String address;
	public String city;
	public String state;
	public String zipcode;

	private int salePrice;
	private int basePrice;
	private int predictPrice;

	//Coordinates
	//latitude
	public double lat;
	//longitude
	public double lng;

	//house's weight while counting
	int weight;

	//enter house info by terminal if no parameter
	//NOT SAFE, NEED FURTHER EDITING
	public House(){
	}

	public House(
		String address,
		String city,
		String state,
		String zipcode,
		int floorSize,
		int bedNumber,
		int bathNumber,
		int salePrice
	){
		this.address = address;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
		System.out.println("Create: " + this.address + " " + this.city + " " + this.state + " " + this.zipcode);
		this.floorSize = floorSize;
		this.bedNumber = bedNumber;
		this.bathNumber = bathNumber;
		this.salePrice = salePrice;

		//get coordinates from google
		String leftSide = "https://maps.googleapis.com/maps/api/geocode/json?address=";
		String rightSide = "&key=AIzaSyAkHCRteSI8pE17RvLpTggwZu267wwL3Lw";
		String addressPart = address.replace(" ", "+");
		String cityPart = city.replace(" ", "+");
		String url = leftSide + addressPart + ",+" + cityPart + ",+" + state + rightSide;
		String json = new String();
		try{
			json = Jsoup.connect(url).ignoreContentType(true).execute().body();
		}
		catch (IOException e) {System.out.println(e);}

		JsonParser jsonParser = new JsonParser();
		JsonObject coordinates = jsonParser.parse(json)
			.getAsJsonObject().getAsJsonArray("results").get(0)
			.getAsJsonObject().get("geometry")
			.getAsJsonObject().getAsJsonObject("location");
		lat = Double.parseDouble(coordinates.get("lat").getAsString());
		lng = Double.parseDouble(coordinates.get("lng").getAsString());

		//set default weight to 1
		weight = 1;
		try{
			Thread.sleep(1000);
		}
		catch(InterruptedException ex) {
    		Thread.currentThread().interrupt();
		}

	}
	public void setByTerminal(){
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter address: ");
		// address = scanner.nextLine();
		address = "42b carriage dr";
		System.out.println(address);
		System.out.println("Please enter city: ");
		// city = scanner.nextLine();
		city = "new bedford";
		System.out.println(city);
		System.out.println("Please enter state: ");
		// state = scanner.nextLine();
		state = "ma";
		System.out.println(state);
		System.out.println("Please enter zipcode: ");
		// zipcode = scanner.nextLine();
		zipcode = "02740";
		System.out.println(zipcode);

		//get coordinates from google
		String leftSide = "https://maps.googleapis.com/maps/api/geocode/json?address=";
		String rightSide = "&key=AIzaSyAkHCRteSI8pE17RvLpTggwZu267wwL3Lw";
		String addressPart = address.replace(" ", "+");
		String cityPart = city.replace(" ", "+");
		String url = leftSide + addressPart + ",+" + cityPart + ",+" + state + rightSide;
		String json = new String();
		try{
			json = Jsoup.connect(url).ignoreContentType(true).execute().body();
		}
		catch (IOException e) {System.out.println(e);}

		JsonParser jsonParser = new JsonParser();
		JsonObject coordinates = jsonParser.parse(json)
    		.getAsJsonObject().getAsJsonArray("results").get(0)
    		.getAsJsonObject().get("geometry")
		    .getAsJsonObject().getAsJsonObject("location");
		lat = Double.parseDouble(coordinates.get("lat").getAsString());
		lng = Double.parseDouble(coordinates.get("lng").getAsString());
		System.out.println(lat);
		System.out.println(lng);

		System.out.println("Please enter floor area (sqft): ");
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
		// System.out.println(s);
	    return s;
	}

	public void setfloorSize(int f){
		floorSize = f;
	}
	public int getfloorSize(){
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
