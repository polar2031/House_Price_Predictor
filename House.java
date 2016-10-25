
public class House{
	//house condiction
	public int lot;
	public int year;
	public int bedNumber;
	public int bathNumber;
	public String address;

	private int salePrice;

	private int basePrice;
	private int predictPrice;

	public House(){
		//use house id to find house data
		//some sql code

		//testing code start
		this.lot = 7405;
		this.bedNumber = 3;
		this.bathNumber = 1;
		//testing code end

	}
	public House(String a, int l, int bed, int bath){
		address = a;
		lot = l;
		bedNumber = bed;
		bathNumber = bath;
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
}
