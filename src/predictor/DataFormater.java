package predictor;

import java.util.List;

import predictor.model.Sampler;
import predictor.model.data.House;
import predictor.model.regression.PreProcessor;

public class DataFormater {
	public static void main(String[] args) {
		List<House> testHouses = null;
		
		String zip = "02062";
		System.out.println(zip);
		
		try{
			if(!Sampler.isDataOfZipUp2Date(zip)){
				Sampler.updateDataOfzip(zip);
			}
			testHouses = Sampler.getDataOfZip(zip);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//remove testing data with missing/error feature
		testHouses = PreProcessor.basicFilter(testHouses);
		
		for(House h : testHouses){
			System.out.println(h.lastSoldPrice + "," + h.bedroomNumber + "," + h.bathroomNumber + "," + h.floorSize + "," + h.lotSize + "," + (2017 - h.builtYear) + "10,0,0,0,0,0,0,0,0,0,0,0,0");
		}
		
		
	}
}
