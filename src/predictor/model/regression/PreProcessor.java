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
		
		List<House> t = new ArrayList<House>(sampleList);
		
		double EARTH_RADIUS = 3963.1906;
        double milesPerlatitude = EARTH_RADIUS * Math.PI / 180.0;
        double milesPerlongitude = Math.abs(EARTH_RADIUS * Math.PI / 180.0 * Math.cos(target.longitude));
        double latitudeRange = maxRange / milesPerlatitude;
        double longitudeRange = maxRange / milesPerlongitude;

        for(int i = 0; i < t.size();){
        	if((t.get(i).longitude > target.longitude + longitudeRange) ||
        		(t.get(i).longitude < target.longitude - longitudeRange) ||
        		(t.get(i).latitude > target.latitude + latitudeRange) ||
        		(t.get(i).latitude < target.latitude - latitudeRange)){
        		t.remove(i);
        	}
        	else{
        		i++;
        	}
        }
        
		return t;
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
	// remove sample with no sale price or built year
	public static List<House> basicFilter(List<House> sampleList){
        for(int i = 0; i < sampleList.size();){
        	House h = sampleList.get(i);
        	if(h.lastSoldPrice == null || 
        			h.lastSoldPrice <= 100000 || 
        			h.lastSoldDate == null || 
        			h.builtYear == 0 || 
        			(h.lastSoldPrice / h.floorSize) > 2000 || 
        			(h.lastSoldPrice / h.floorSize) < 100 || 
        			h.lotSize == 0){
        		sampleList.remove(i);
        	}
        	else{
        		i++;
        	}
        }
//        System.err.println(sampleList.size());
		return sampleList;
	}
	
	
	public static List<House> pricePerSqftFilter(List<House> sampleList){
		double avgPricePerSqft = 0;
		double totalPrice = 0.0;
		double totalSqft = 0.0;
        for(int i = 0; i < sampleList.size(); i++){
        	totalPrice += sampleList.get(i).lastSoldPrice; 
        	totalSqft += sampleList.get(i).floorSize;
        }
        avgPricePerSqft = totalPrice / totalSqft;
        for(int i = 0; i < sampleList.size(); i++){
        	double pricePerSqft = sampleList.get(i).lastSoldPrice / sampleList.get(i).floorSize; 
        	if(Math.abs((avgPricePerSqft - pricePerSqft) / avgPricePerSqft) > 0.9){
        		sampleList.remove(i);
        	}
        }
		return sampleList;
	}
	
	public static List<House> priceFilter(List<House> sampleList, double floorSize){
		double avgPricePerSqft = 0;
		double totalPrice = 0.0;
		double totalSqft = 0.0;
		double estimatePrice = 0.0;
		
        for(int i = 0; i < sampleList.size(); i++){
        	totalPrice += sampleList.get(i).lastSoldPrice; 
        	totalSqft += sampleList.get(i).floorSize;
        }
        avgPricePerSqft = totalPrice / totalSqft;
        estimatePrice = avgPricePerSqft * floorSize;
        
        
        for(int i = 0; i < sampleList.size(); i++){
        	if(Math.abs((estimatePrice - sampleList.get(i).lastSoldPrice) / estimatePrice) > 0.5){
        		sampleList.remove(i);
        	}
        }
		return sampleList;
	}

}
