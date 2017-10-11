package predictor.model.data;

public class HouseWithDetail extends House{
	public HouseWithDetail(House h){
		// copy old house object
		this.address = h.address;
		this.bathroomNumber = h.bathroomNumber;
		this.bedroomNumber = h.bedroomNumber;
		this.builtYear = h.builtYear;
		this.city = h.city;
		this.cooling = h.cooling;
		this.discription = h.discription;
		this.features = h.features;
		this.floorSize = h.floorSize;
		this.heating = h.heating;
		this.houseType = h.houseType;
		this.lastSoldDate = h.lastSoldDate;
		this.lastSoldPrice = h.lastSoldPrice;
		this.latitude = h.latitude;
		this.longitude = h.longitude;
		this.lotSize = h.lotSize;
		this.parking = h.parking;
		this.state = h.state;
		this.zip = h.zip;
		
		for(int i = 0; i < features.length; i++){
			
		}
		
		
	}
}
