
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

	public House(){
	}
	public House(String a, int l, int bed, int bath){
		address = a;
		lot = l;
		bedNumber = bed;
		bathNumber = bath;
    }
	//count distance
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

	public void setLot(int l){
		lot = l;
	}
	public int getLot(){
		return lot;
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
