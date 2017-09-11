package predictor.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import predictor.model.data.House;
import predictor.model.regression.LeastSquare;
public class PredictionModel2 {
	public House target;
	public List<House> sampleList;
	private VariableOption v;
	
	public static int PRICE_PER_SQFT = 0;
	
	private boolean isTargetSet;
	private boolean isSampleSet;
	private boolean valueTransferFlag;
	
	public PredictionModel2(){
		target = null;
		sampleList = new ArrayList<House>();
		v = new VariableOption();
		isTargetSet = false;
		isSampleSet = false;
	}
	
	public boolean setTarget(House h){
		if(h == null){
			return false;
		}
		target = h;
		isTargetSet = true;
		return true;
	}
	
    public boolean setSamples(List<House> newSampleList) {
    	if(newSampleList == null){
    		return false;
    	}
    	else{ 
    		// add all houses in the list except target
    		for(int i = 0; i < newSampleList.size();){
    			House h = newSampleList.get(i);
    			if(h.address.compareTo(target.address) != 0){
    				sampleList.add(h);
    			}
    			i++;
    		}
    		isSampleSet = true;
    		return true;
    	}
	}
    
    public boolean predict(int method){
    	if(isTargetSet == false || isSampleSet == false){
    		return false;
    	}
    	double nearbyRange = 0.1;
    	
    	if(method == PRICE_PER_SQFT){
    		// take all houses in same zip and predict target's price
    		target.predictPrice = getAvgPricePerSqft(sampleList) * target.floorSize;
    	}
    	else if(method == 1){
    		// take similar houses and predict target's price(by take similar houses -> calculate average price/sqft -> multiply by house's floor size)
    		int similarHouseNumber = sampleList.size() / 20;
    		List<House> similarHouseList = takeNSimilarHouses(similarHouseNumber);
    		if(similarHouseList.size() > 0){
    			target.predictPrice = getAvgPricePerSqft(similarHouseList) * target.floorSize;
    		}
    		else{
    			target.predictPrice = getAvgPricePerSqft(sampleList) * target.floorSize;
    		}
    	}
    	else if(method == 2){
    		// build sample matrix
    		double[][] sampleVariable = new double[sampleList.size()][v.variableNumber()];
        	double[] sampleSolution = new double[sampleList.size()];
        	double[][] targetVariable  = new double[1][v.variableNumber()];
        	
        	for(int i = 0; i < sampleList.size(); i++){
        		sampleVariable[i] = v.variableTransform(sampleList.get(i));
        		sampleSolution[i] = sampleList.get(i).lastSoldPrice;
        	}
        	
        	targetVariable[0] = v.variableTransform(target);
        	
    		LeastSquare bestL;
    		bestL = new LeastSquare(sampleVariable, sampleSolution);
    		target.predictPrice = bestL.solve(targetVariable);
//	    	System.out.println(target.predictPrice);
    	}
    	else if(method == 3){
    		int similarHouseNumber = sampleList.size() / 5;
    		List<House> similarHouseList = takeNSimilarHouses(similarHouseNumber);
    		// build sample matrix
    		double[][] sampleVariable = new double[similarHouseList.size()][v.variableNumber()];
        	double[] sampleSolution = new double[similarHouseList.size()];
        	double[][] targetVariable  = new double[1][v.variableNumber()];
        	
        	for(int i = 0; i < similarHouseList.size(); i++){
        		sampleVariable[i] = v.variableTransform(similarHouseList.get(i));
        		sampleSolution[i] = similarHouseList.get(i).lastSoldPrice;
        	}
        	
        	targetVariable[0] = v.variableTransform(target);
        	
    		LeastSquare bestL;
    		bestL = new LeastSquare(sampleVariable, sampleSolution);
    		target.predictPrice = bestL.solve(targetVariable);
//	    	System.out.println(target.predictPrice);
    	}
    	
    	else if(method == 4){
    		// build sample matrix
    		double[][] sampleVariable = new double[sampleList.size()][v.variableNumber()];
        	double[] sampleSolution = new double[sampleList.size()];
        	double[][] targetVariable  = new double[1][v.variableNumber()];
        	
        	for(int i = 0; i < sampleList.size(); i++){
        		sampleVariable[i] = v.variableTransform(sampleList.get(i));
        		sampleSolution[i] = sampleList.get(i).lastSoldPrice;
        	}
        	
        	targetVariable[0] = v.variableTransform(target);
        	
    		LeastSquare bestL;
    		bestL = new LeastSquare(sampleVariable, sampleSolution);
    		target.predictPrice = bestL.solve(targetVariable);
    		
    		// predict nearby houses' price (by take similar houses -> calculate average price/sqft -> multiply by houses' floor size)
	    	List<House> nearestHouseList = takeNearbyHouses(nearbyRange);
	    	if(nearestHouseList.size() > 5){
		    	for(int i = 0; i < nearestHouseList.size(); i++){
		    		PredictionModel2 m = new PredictionModel2();
		    		m.setTarget(nearestHouseList.get(i));
		    		m.setSamples(sampleList);
		    		m.predict(PRICE_PER_SQFT);
		    	}
		    	double differentPercentage = 0.0;
		    	for(int i = 0; i < nearestHouseList.size(); i++){
		    		differentPercentage += (nearestHouseList.get(i).lastSoldPrice - nearestHouseList.get(i).predictPrice) 
		    				/ nearestHouseList.get(i).lastSoldPrice;
		    	}
		    	differentPercentage = differentPercentage / nearestHouseList.size();
		    	target.predictPrice = target.predictPrice * (1 + differentPercentage);
	    	}
    		
    	}
    	
    	
    	else{
    		// take similar houses and predict target's price(by take similar houses -> calculate average price/sqft -> multiply by house's floor size)
    		int similarHouseNumber = 10;
	    	List<House> similarHouseList = takeNSimilarHouses(similarHouseNumber);
	    	target.predictPrice = getAvgPricePerSqft(similarHouseList) * target.floorSize;
//	    	System.out.println(target.predictPrice);
	    	
	    	// predict nearby houses' price (by take similar houses -> calculate average price/sqft -> multiply by houses' floor size)
	    	List<House> nearestHouseList = takeNearbyHouses(nearbyRange);
	    	if(nearestHouseList.size() > 5){
		    	for(int i = 0; i < nearestHouseList.size(); i++){
		    		PredictionModel2 m = new PredictionModel2();
		    		m.setTarget(nearestHouseList.get(i));
		    		m.setSamples(sampleList);
		    		m.predict(PRICE_PER_SQFT);
		    	}
		    	double differentPercentage = 0.0;
		    	for(int i = 0; i < nearestHouseList.size(); i++){
		    		differentPercentage += (nearestHouseList.get(i).lastSoldPrice - nearestHouseList.get(i).predictPrice) 
		    				/ nearestHouseList.get(i).lastSoldPrice;
		    	}
		    	differentPercentage = differentPercentage / nearestHouseList.size();
		    	target.predictPrice = target.predictPrice * (1 + differentPercentage / 2);
	    	}
    	}
    	
    	
    	return true;
    }
    public void setVariable(boolean[] variableOptions){
    	v.setFloorSizeFlag(variableOptions[VariableOption.floorSize]);
    	v.setLotSizeFlag(variableOptions[VariableOption.lotSize]);
    	v.setBedroomNumberFlag(variableOptions[VariableOption.bedroomNumber]);
    	v.setBathroomNumberFlag(variableOptions[VariableOption.bathroomNumber]);
    	v.setAgeFlag(variableOptions[VariableOption.age]);
    	v.setBasePriceFlag(variableOptions[VariableOption.basePrice]);
    }
   	
    public void setVariableTransfer(boolean[] transferOptions){
    	v.setTransferFlag(transferOptions);
    }
    public void setValueTransfer(boolean b){
    	valueTransferFlag = b;
    }
    private double getAvgPricePerSqft(List<House> l){
    	double avgPricePerSqft = 0.0;
    	for(int i = 0; i < l.size(); i++){
    		avgPricePerSqft += l.get(i).lastSoldPrice / l.get(i).floorSize;
    	}
    	avgPricePerSqft = avgPricePerSqft / l.size();
    	return avgPricePerSqft;
    }
    
    private List<House> takeNSimilarHouses(int n){
    	
    	if(isSampleSet == false || isTargetSet == false){
    		return null;
    	}
    	List<House> l;
    	if(sampleList.size() <= n){
    		l = new ArrayList<House>(sampleList);
    	}
    	else{
    		l = new ArrayList<House>();
    		
    		// count similarity
    		for(int i = 0; i < sampleList.size(); i++){
    			House h = sampleList.get(i);
    			h.similarity = 1 - Math.abs((h.floorSize - target.floorSize) / target.floorSize);
    			
    		}
            
            //O(k*n)
            for(int i = 0; i < n; i++){
            	double maxSimilarity = Double.MIN_VALUE;
            	int index = 0;
            	for(int j = 0; j < sampleList.size(); j++){
            		if(sampleList.get(j).similarity > maxSimilarity){
            			index = j;
            			maxSimilarity = sampleList.get(j).similarity;
            			sampleList.get(j).similarity = 0;
            		}
            	}
            	l.add(sampleList.get(index));
            }
    	}
    	return l;
    }
    
	private List<House> takeNearbyHouses(double range){
    	if(isSampleSet == false || isTargetSet == false){
    		return null;
    	}
		
		List<House> l = new ArrayList<House>();
		
		double EARTH_RADIUS = 3963.1906;
	    double milesPerlatitude = EARTH_RADIUS * Math.PI / 180.0;
	    double milesPerlongitude = Math.abs(EARTH_RADIUS * Math.PI / 180.0 * Math.cos(target.longitude));
	    double latitudeRange = range / milesPerlatitude;
	    double longitudeRange = range / milesPerlongitude;
	
	    for(int i = 0; i < sampleList.size(); i++){
	    	if((sampleList.get(i).longitude < target.longitude + longitudeRange) &&
	    		(sampleList.get(i).longitude > target.longitude - longitudeRange) &&
	    		(sampleList.get(i).latitude < target.latitude + latitudeRange) &&
	    		(sampleList.get(i).latitude > target.latitude - latitudeRange)){
	    		l.add(sampleList.get(i));
	    	}
	    }
		return l;	
	}
    
}
