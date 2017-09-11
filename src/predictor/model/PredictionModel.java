package predictor.model;

import java.util.ArrayList;
import java.util.List;

import predictor.model.data.House;
import predictor.model.parser.ZillowParser;
import predictor.model.regression.BoxCoxTransform;
import predictor.model.regression.LeastSquare;
import predictor.model.regression.PreProcessor;
import predictor.model.VariableOption;

public class PredictionModel {
	public House target;
	public List<House> sampleList;
	private VariableOption v;
	private boolean valueTransferFlag;
	private int sampleNumber;
	
	public PredictionModel(){
		target = null;
		sampleList = new ArrayList<House>();
		v = new VariableOption();
	}
	
    public void setTarget(String address, String city, String state, String zip) throws Exception {
    	target = ZillowParser.parseHouseThrowAddress(address, state, zip);
    	return;
    }
    
    public boolean addSamples(List<House> newSampleList) {
    	if(newSampleList == null){
    		return false;
    	}
    	else{ 
    		for(int i = 0; i < newSampleList.size();){
    			if(newSampleList.get(i).address.compareTo(target.address) != 0){
    				sampleList.add(newSampleList.get(i));
    			}
    			i++;
    		}
    		return true;
    	}
	}
   
   	public void sampleFilter(){
   		
   		sampleList = PreProcessor.basicFilter(sampleList);
//   		sampleList = PreProcessor.soldTimeFilter(sampleList, 365);
   		sampleList = PreProcessor.pricePerSqftFilter(sampleList);
//   		sampleList = PreProcessor.priceFilter(sampleList, target.floorSize);

   		
   		// take 3 closet houses
   		double range = 0.01;
   		double maxRange = 1;
   		while(true){
   			if(range > maxRange){
   				break; 
   			}
   			List<House> t = PreProcessor.rangeFilter(sampleList, target, range, sampleNumber);
   			if(t.size() < sampleNumber){
   				range += 0.01;
   			}
   			else{
   				sampleList = t;
   				break;
   			}

   		}

   		
//    	sampleList = PreProcessor.floorSizeFilter(sampleList, target, 0.1, maxSampleNumber);

//        System.err.println("sample number: " + sampleList.size());
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
    
   	public void predict(int method) throws Exception{
    	
//   		System.err.println(sampleList.size());
   		
   		// check
    	if(sampleList.size() == 0){
    		target.predictPrice = 0.0;
    		return;
    	}
   		
    	if(method == 0){
    		// take n similar houses
    		

    	}
    	
    	else{
    		// regression
    		double[][] sampleVariable = new double[sampleList.size()][v.variableNumber()];
        	double[] sampleSolution = new double[sampleList.size()];
        	double[][] targetVariable  = new double[1][v.variableNumber()];
        	
        	// build sample matrix
        	for(int i = 0; i < sampleList.size(); i++){
        		sampleVariable[i] = v.variableTransform(sampleList.get(i));
        		sampleSolution[i] = sampleList.get(i).lastSoldPrice;
        	}
        	targetVariable[0] = v.variableTransform(target);
        	
    		LeastSquare bestL;
    		if(method != 5){
    			// Linear
    			if(method == 1){
    				bestL = new LeastSquare(sampleVariable, sampleSolution);
    				target.predictPrice = bestL.solve(targetVariable);
//    	        	System.err.println(target.lastSoldPrice + "," + target.predictPrice + "," + target.address);

    			}
    			// log-Linear
    			else if(method == 2){
    				double[] sampleSolutionT = new double[sampleList.size()];
    				for(int i = 0; i < sampleSolution.length; i++){
    					sampleSolutionT[i] = Math.log(sampleSolution[i]);
//    					System.err.println(sampleSolution[i] + "," + sampleSolutionT[i]);
//    					System.err.println(Math.pow(Math.E, sampleSolutionT[i]));
    				}
    				bestL = new LeastSquare(sampleVariable, sampleSolutionT);
    				target.predictPrice = Math.pow(Math.E, bestL.solve(targetVariable));
//    				System.err.println(bestL.solve(targetVariable));
//    	        	System.err.println(target.lastSoldPrice + "," + target.predictPrice);
//    				System.err.println(Math.pow(Math.E, Math.log(100)));
    			}
    			// linear-log
    			else if(method == 3){
    				double[][] sampleVariableT= new double[sampleList.size()][v.variableNumber()];
    				double[][] targetVariableT  = new double[1][v.variableNumber()];
    				// for all used/unused variable
    				int index = 0;
    				for(int i = 0; i < v.variableFlag.length; i++){
    					// if variable is used
    					if(v.variableFlag[i] == true){
    						// if variable need transfer
    						if(v.variableTransferFlag[i] == true){
    							// transfer that kind of variable in samples
    							for(int j = 0; j < sampleList.size(); j++){
    								sampleVariableT[j][index] = Math.log(sampleVariable[j][index]);
    							}
    							// transfer that kind of variable in target
    							targetVariableT[0][index] = Math.log(targetVariable[0][index]);
    						}
    						// else don't transfer
    						else{
    							for(int j = 0; j < sampleList.size(); j++){
    								sampleVariableT[j][index] = sampleVariable[j][index];
    							}
    							targetVariableT[0][index] = targetVariable[0][index];
    						}
    						index++;
    					}
    				}
    				
    				bestL = new LeastSquare(sampleVariableT, sampleSolution);
    				target.predictPrice = bestL.solve(targetVariableT);
//    	        	System.err.println(target.lastSoldPrice + "," + target.predictPrice + "," + target.address);
    			}
    			// log-log
    			else if(method == 4){
    				double[] sampleSolutionT = new double[sampleList.size()];
    				double[][] sampleVariableT= new double[sampleList.size()][v.variableNumber()];
    				double[][] targetVariableT  = new double[1][v.variableNumber()];
    				
    				for(int i = 0; i < sampleSolution.length; i++){
    					sampleSolutionT[i] = Math.log(sampleSolution[i]);
    				}
    				
    				// for all used/unused variable
    				int index = 0;
    				for(int i = 0; i < v.variableFlag.length; i++){
    					// if variable is used
    					if(v.variableFlag[i] == true){
    						// if variable need transfer
    						if(v.variableTransferFlag[i] == true){
    							// transfer that kind of variable in samples
    							for(int j = 0; j < sampleList.size(); j++){
    								sampleVariableT[j][index] = Math.log(sampleVariable[j][index]);
    							}
    							// transfer that kind of variable in target
    							targetVariableT[0][index] = Math.log(targetVariable[0][index]);
    						}
    						// else don't transfer
    						else{
    							for(int j = 0; j < sampleList.size(); j++){
    								sampleVariableT[j][index] = sampleVariable[j][index];
    							}
    							targetVariableT[0][index] = targetVariable[0][index];
    						}
    						index++;
    					}
    				}
    				
    				bestL = new LeastSquare(sampleVariableT, sampleSolutionT);
    				target.predictPrice = Math.pow(Math.E, bestL.solve(targetVariableT));
//    	        	System.err.println(target.lastSoldPrice + "," + target.predictPrice + "," + target.address);
    			}
    		}
    		// box-cox transfer
    		if(method == 5){
    			
    			v.setUsedVariableTransferFlag();
    			
				double[] sampleSolutionT = new double[sampleList.size()];
				double[][] sampleVariableT= new double[sampleList.size()][v.variableNumber()];
				double[][] targetVariableT  = new double[1][v.variableNumber()];
				
		    	double lamda;
		    	double maxLamda;
		    	double bestLamda;
		    	
		    	double theta;
		    	double maxTheta;
		    	double bestTheta;
		    	
		    	double theta1;
		    	double maxTheta1;
		    	double bestTheta1;
		    	
		    	double minRss = Double.MAX_VALUE;
		    	bestL = new LeastSquare(sampleVariable, sampleSolution);
		    	
		    	if(valueTransferFlag == true){
		    		lamda = -1.0;
		    		maxLamda = 2.001;
		    	}
		    	else{
		    		lamda = 1.0;
		    		maxLamda = 1.001;
		    	}
		    	bestLamda = lamda;
		    	if(v.variableTransferFlag[0] == true){
		    		theta = -1.0;
		    		maxTheta = 2.001;
		    	}
		    	else{
		    		theta = 1.0;
		    		maxTheta = 1.001;
		    	}
		    	bestTheta = theta;
		    	if(v.variableTransferFlag[1] == true){
		    		theta1 = -1.0;
		    		maxTheta1 = 2.001;
		    	}
		    	else{
		    		theta1 = 1.0;
		    		maxTheta1 = 1.001;
		    	}
		    	bestTheta1 = theta1;
		    	
		    	for(; lamda < maxLamda; lamda += 0.1){
		    		sampleSolutionT = BoxCoxTransform.transformAll(sampleSolution, lamda); 
		    		for(; theta < maxTheta; theta += 0.1){
		    			sampleVariableT = BoxCoxTransform.transform(sampleVariable, theta, 0);
		    			for(; theta1 < maxTheta1; theta1 += 0.1){
		    				sampleVariableT = BoxCoxTransform.transform(sampleVariableT, theta1, 1);
		    				LeastSquare l = new LeastSquare(sampleVariableT, sampleSolutionT);
		    				double resultT[] = l.solveAll(sampleVariableT);
		    				double result[] = BoxCoxTransform.reduce(resultT, lamda);
		    				double rss = 0.0;
		    				for(int i = 0; i < sampleList.size(); i++){
		    					rss += Math.abs(result[i] - sampleSolution[i]);
		    				}
		    				if(minRss > rss){
		    					minRss = rss;
		    					bestLamda = lamda;
		    					bestTheta = theta;
		    					bestTheta1 = theta1;
		    					bestL = l;
		    				}
		    				if(Double.isNaN(result[0])){
		    					System.err.println("NaN");
		    				}
		    			}
		    		}
		    	}
		    	
		    	targetVariableT = BoxCoxTransform.transform(targetVariable, bestTheta, 0);
		    	targetVariableT = BoxCoxTransform.transform(targetVariableT, bestTheta1, 1);
		    	double predictPriceT = bestL.solve(targetVariableT);
		    	double predictPrice = Math.pow(predictPriceT, 1.0 / bestLamda);
		    	target.predictPrice = predictPrice;
//	        	System.err.println(target.lastSoldPrice + "," + target.predictPrice + "," + target.address);
    		}
    	}

//    	bestL.showSolution();
    }
   	
   	
   	
	public boolean isTargetSet() {
		if(target != null){
			return true;
		}
		return false;
	}

	public void setTarget(House h) {
		target = h;
		return;
	}

	public void setSampleNumber(int sampleNumber) {
		this.sampleNumber = sampleNumber;
		return;
	}

}
