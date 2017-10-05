package predictor.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import predictor.model.data.House;
import predictor.model.regression.LeastSquare;

public class PredictModel {
	private House target;
	private List<House> sampleList;
	private int sampleNumber;
	public double lotInf;
	public double bedInf;
	public double bathInf;
	public double ageInf;
	public double pDistance;
	public double pFloorSize;

	public PredictModel(){
		sampleList = new ArrayList<House>();
		sampleNumber = 5;
		lotInf = 0.02;
		bedInf = 0.05;
		bathInf = 0;
		ageInf = 0;
		
		pDistance = 0.1;
		pFloorSize = 1.7;
	}
	
	public void setTarget(House h){
		target = h;
	}
	
    public void setSamples(List<House> newSampleList) {
		// add all houses in the list except target
		for(int i = 0; i < newSampleList.size(); i++){
			House h = newSampleList.get(i);
			if(h.address.compareTo(target.address) != 0){
				sampleList.add(h);
			}
		}
	}
    
    public void predict(int method){
    	int thisYear = Calendar.getInstance().get(Calendar.YEAR);
    	// average price per sqft of all samples
    	if(method == 0){
    		double avgPricePerSqft = 0.0;
    		
    		for(int i = 0; i < sampleList.size(); i++){
    			House h = sampleList.get(i);
    			avgPricePerSqft += h.lastSoldPrice / h.floorSize;
    		}
    		avgPricePerSqft /= sampleList.size();
    		target.predictPrice = avgPricePerSqft * target.floorSize;
    	}
    	
    	// average price per sqft of selected samples
    	else if(method == 1){
    		takeNSimilarSamples(sampleNumber);
    		double avgPricePerSqft = 0.0;

    		for(int i = 0; i < sampleList.size(); i++){
    			House h = sampleList.get(i);
    			avgPricePerSqft += h.lastSoldPrice / h.floorSize;
    		}
    		avgPricePerSqft /= sampleList.size();
    		target.predictPrice = avgPricePerSqft * target.floorSize;
    	}
    	
    	// linear regression using all samples
    	else if(method == 2){
    		// build sample matrix
    		VariableOption v = new VariableOption();
    		v.setBasePriceFlag(true);
    		v.setFloorSizeFlag(true);
    		v.setLotSizeFlag(true);
    		v.setBathroomNumberFlag(true);
    		v.setBedroomNumberFlag(true);
    		v.setAgeFlag(true);
    		
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
    	}
//    	
    	else if(method == 3){
//    		takeNSimilarSamples(sampleNumber);
    		// build sample matrix
    		VariableOption v = new VariableOption();
    		v.setBasePriceFlag(true);
    		v.setFloorSizeFlag(true);
    		v.setLotSizeFlag(true);
    		v.setBathroomNumberFlag(true);
    		v.setBedroomNumberFlag(true);
    		v.setAgeFlag(true);
    		
    		double[][] sampleVariable = new double[sampleList.size()][v.variableNumber()];
        	double[] sampleSolution = new double[sampleList.size()];
        	double[][] targetVariable  = new double[1][v.variableNumber()];
        	
        	double avgAge = 0;
        	for(int i = 0; i < sampleList.size(); i++){
        		avgAge += (2017 - sampleList.get(i).builtYear);
        	}
        	avgAge /= sampleList.size();
        	
        	for(int i = 0; i < sampleList.size(); i++){
        		sampleVariable[i] = v.variableTransform(sampleList.get(i));
        		sampleSolution[i] = sampleList.get(i).lastSoldPrice / sampleList.get(i).floorSize;
//        		sampleSolution[i] = Math.log(sampleList.get(i).lastSoldPrice / sampleList.get(i).floorSize);
//        		sampleSolution[i] = Math.pow(sampleList.get(i).lastSoldPrice / sampleList.get(i).floorSize, 0.33);
        	}
        	
        	targetVariable[0] = v.variableTransform(target);
        	
    		LeastSquare bestL;
    		bestL = new LeastSquare(sampleVariable, sampleSolution);
    		
    		target.predictPrice = bestL.solve(targetVariable) * target.floorSize;
//    		target.predictPrice = Math.pow(Math.E, bestL.solve(targetVariable)) * target.floorSize;
//    		target.predictPrice = Math.pow(bestL.solve(targetVariable), 3) * target.floorSize;
//    		bestL.showSolution();
    	}
    	
    	// price = estimated price * (bed/avgbed)^a ...
    	else if(method == 4){
//    		takeNSimilarSamples(sampleNumber);
    		double avgPrice = 0.0;
    		double avgLot = 0;
    		double avgBed = 0;
    		double avgBath = 0;
    		double avgBathPerBed = 0;
    		double avgBedroomSize = 0;
    		double avgAge = 0;
    		
    		for(int i = 0; i < sampleList.size(); i++){
    			House h = sampleList.get(i);
    			avgPrice += h.lastSoldPrice / h.floorSize;
    			avgLot += h.lotSize;
    			avgBed += h.bedroomNumber;
    			avgBath += h.bathroomNumber;
    			avgBathPerBed += h.bathroomNumber / h.bedroomNumber;
    			avgBedroomSize += h.floorSize / h.bedroomNumber;
    			avgAge += (thisYear - h.builtYear);
    		}
    		avgPrice /= sampleList.size();
    		avgLot /= sampleList.size();
    		avgBed /= sampleList.size();
    		avgBath /= sampleList.size();
    		avgBathPerBed /= sampleList.size();
    		avgBedroomSize /= sampleList.size();
    		avgAge /= sampleList.size();
    		    		
    		// build sample matrix
    		
    		double[][] sampleVariable = new double[sampleList.size()][6];
        	double[] sampleSolution = new double[sampleList.size()];
        	double[][] targetVariable  = new double[1][6];
        	
        	for(int i = 0; i < sampleList.size(); i++){
        		int v = 0;
//        		sampleVariable[i][v++] = Math.log(sampleList.get(i).floorSize * avgPrice);
        		sampleVariable[i][v++] = Math.log(1 + sampleList.get(i).lotSize / avgLot);
        		sampleVariable[i][v++] = Math.log(1 + sampleList.get(i).bedroomNumber / avgBed);
        		sampleVariable[i][v++] = Math.log(1 + sampleList.get(i).bathroomNumber / avgBath);
        		sampleVariable[i][v++] = Math.log(1 + (sampleList.get(i).bathroomNumber / sampleList.get(i).bedroomNumber) / avgBathPerBed);
        		sampleVariable[i][v++] = Math.log(1 + (sampleList.get(i).floorSize / sampleList.get(i).bedroomNumber) / avgBedroomSize);
        		sampleVariable[i][v++] = Math.log(1 + (thisYear - sampleList.get(i).builtYear) / avgAge);
        		sampleSolution[i] = Math.log(sampleList.get(i).lastSoldPrice) - Math.log(sampleList.get(i).floorSize * avgPrice);
        	}
        	
        	int v = 0;
//        	targetVariable[0][v++] = Math.log(target.floorSize * avgPrice);
        	targetVariable[0][v++] = Math.log(1 + target.lotSize / avgLot);
        	targetVariable[0][v++] = Math.log(1 + target.bedroomNumber / avgBed);
        	targetVariable[0][v++] = Math.log(1 + target.bathroomNumber / avgBath);
        	targetVariable[0][v++] = Math.log(1 + (target.bathroomNumber / target.bedroomNumber) / avgBathPerBed);
        	targetVariable[0][v++] = Math.log(1 + (target.floorSize / target.bedroomNumber) / avgBedroomSize);
        	targetVariable[0][v++] = Math.log(1 + (thisYear - target.builtYear) / avgAge);

    		LeastSquare bestL;
    		bestL = new LeastSquare(sampleVariable, sampleSolution);
    		target.predictPrice = Math.pow(Math.E, (bestL.solve(targetVariable) + Math.log(target.floorSize * avgPrice)));
    		String[] val = {"lot", "bed", "bath", "bath/bed", "floorsize/bed", "age"};
//    		bestL.showSolution(val);
    	}
    	else if(method == 5){
    		
    	}
    }
    
    private void takeNSimilarSamples(int n){

    	if(sampleList.size() >= n){

    		double EARTH_RADIUS = 3963.1906;
    	    double milesPerlatitude = EARTH_RADIUS * Math.PI / 180.0;
    	    double milesPerlongitude = Math.abs(EARTH_RADIUS * Math.PI / 180.0 * Math.cos(target.longitude));
        	
    	    // Calculate similarity
        	for(int i = 0; i < sampleList.size(); i++){
        		House h = sampleList.get(i);
        		double distance = Math.pow(Math.pow(((h.longitude - target.longitude) * milesPerlongitude), 2) + Math.pow(((h.latitude - target.latitude) * milesPerlatitude), 2), 0.5);
        		double floorSizeDiffRate = 1 - Math.abs((h.floorSize - target.floorSize) / target.floorSize);
        		h.similarity = Math.pow(floorSizeDiffRate, pFloorSize) / Math.pow(distance, pDistance);
        	}
        	
        	// take n houses
            //O(k*n)
        	List<House> l = new ArrayList<House>();
            for(int i = 0; i < n; i++){
            	double maxSimilarity = Double.MIN_VALUE;
            	int index = 0;
            	for(int j = 0; j < sampleList.size(); j++){
            		if(sampleList.get(j).similarity > maxSimilarity){
            			index = j;
            			maxSimilarity = sampleList.get(j).similarity;
            			
            		}
            	}
            	l.add(sampleList.get(index));
            	sampleList.get(index).similarity = Double.MIN_VALUE;
            }
            sampleList = l;
    	}
    }
    
    public void setSampleNumber(int n){
    	sampleNumber = n;
    }
}
