package predictor.model.regression;

import java.util.ArrayList;
import java.util.List;

import predictor.model.data.House;

public class PreProcessor {
	public static int variableNumber = 6;
	
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
		List<House> newSampleList = new ArrayList<House>();
		
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
		return newSampleList;
	}
	
	public static double[] variableTransform(House target) {
		int thisYear = 2017;
        double[] a = new double[variableNumber];
    	a[0] = 0;
        a[1] = target.floorSize;
        a[2] = target.lotSize;
        a[3] = target.bedroomNumber;
        a[3] = 0;
        a[4] = target.bathroomNumber;
        a[4] = 0;
        a[5] = (double)(- thisYear + target.builtYear);
//        a[5] = 0;

		return a;
	}
	

}
