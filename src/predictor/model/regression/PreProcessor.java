package predictor.model.regression;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import predictor.model.data.House;

public class PreProcessor {
	
	// take at most maxSampleNumber samples with similarity on floor size more than minSimilarity
	public static List<House> floorSizeFilter(List<House> sampleList, House target, double minSimilarity, int maxSampleNumber){
		List<House> newSampleList = new ArrayList<House>();
		
        // floor size
        for(int i = 0; i < sampleList.size();){
            double floorSizeSimilarity = (sampleList.get(i).floorSize / target.floorSize) > 1 ? (target.floorSize / sampleList.get(i).floorSize) : (sampleList.get(i).floorSize / target.floorSize);
            if(floorSizeSimilarity < minSimilarity || sampleList.get(i).lastSoldPrice == null){
            	sampleList.remove(i);
            }
            else{
            	sampleList.get(i).similarity = floorSizeSimilarity;
            	i++;
            }
        }
		
        // take top n similar sample
        if(sampleList.size() <= maxSampleNumber){
        	return sampleList;
        }
        //O(k*n)
        for(int i = 0; i < maxSampleNumber; i++){
        	double maxSimilarity = 0.0;
        	int index = 0;
        	for(int j = 0; j < sampleList.size(); j++){
        		if(sampleList.get(j).similarity > maxSimilarity){
        			index = j;
        			maxSimilarity = sampleList.get(j).similarity;
        		}
        	}
        	newSampleList.add(sampleList.get(index));
        	sampleList.remove(index);
        }
		return newSampleList;
	}
	
	public static List<House> rangeFilter(List<House> sampleList, House target, double maxRange, int maxSampleNumber){
		
		double EARTH_RADIUS = 3963.1906;
        double milesPerlatitude = EARTH_RADIUS * Math.PI / 180.0;
        double milesPerlongitude = Math.abs(EARTH_RADIUS * Math.PI / 180.0 * Math.cos(target.longitude));
        double latitudeRange = maxRange / milesPerlatitude;
        double longitudeRange = maxRange / milesPerlongitude;

        for(int i = 0; i < sampleList.size();){
        	if((sampleList.get(i).longitude > target.longitude + longitudeRange) ||
        		(sampleList.get(i).longitude < target.longitude - longitudeRange) ||
        		(sampleList.get(i).latitude > target.latitude + latitudeRange) ||
        		(sampleList.get(i).latitude < target.latitude - latitudeRange)){
        		sampleList.remove(i);
        	}
        	else{
        		i++;
        	}
        }
		return sampleList;
	}
	
	public static List<House> soldTimeFilter(List<House> sampleList, int expireDays){

        for(int i = 0; i < sampleList.size();){
        	Calendar dateNow = Calendar.getInstance();
        	Calendar dateSold = Calendar.getInstance();
        	dateSold.setTime(sampleList.get(i).lastSoldDate);
        	long days = ChronoUnit.DAYS.between(dateSold.toInstant(), dateNow.toInstant());
        	if(days > expireDays){
        		sampleList.remove(i);
        	}
        	else{
        		i++;
        	}
        }
		return sampleList;
	}
	
	public static List<House> basicFilter(List<House> sampleList){
        for(int i = 0; i < sampleList.size();){
        	if(sampleList.get(i).lastSoldPrice == null || sampleList.get(i).lastSoldDate == null){
        		sampleList.remove(i);
        	}
        	else{
        		i++;
        	}
        }
		return sampleList;
	}
	
	public static List<House> pricePerSqftFilter(List<House> sampleList){
		double avgPricePerSqft = 0;
        for(int i = 0; i < sampleList.size(); i++){
        	avgPricePerSqft += sampleList.get(i).lastSoldPrice / sampleList.get(i).floorSize; 
        }
        avgPricePerSqft = avgPricePerSqft / sampleList.size();
        for(int i = 0; i < sampleList.size(); i++){
        	double pricePerSqft = sampleList.get(i).lastSoldPrice / sampleList.get(i).floorSize; 
        	if(Math.abs((avgPricePerSqft - pricePerSqft) / avgPricePerSqft) > 0.3){
        		sampleList.remove(i);
        	}
        }
		return sampleList;
	}

}
