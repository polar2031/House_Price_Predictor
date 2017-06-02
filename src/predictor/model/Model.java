package predictor.model;

import java.util.ArrayList;
import java.util.List;

import predictor.model.data.House;
import predictor.model.parser.ZillowParser;
import predictor.model.regression.BoxCoxTransform;
import predictor.model.regression.LeastSquare;
import predictor.model.regression.PreProcessor;

public class Model {
	public House target;
	public List<House> sampleList;
	public List<Double> data;
	
    public void setTarget(String address, String city, String state, String zip) throws Exception {
    	target = null;
    	House h = ZillowParser.parseHouseThrowAddress(address, state, zip);
    	if(h != null){
    		target = h;
    		return;
    	}
    	return;
    }

    
   public boolean addSamples(List<House> newSampleList) {
	   if(sampleList == null){
		   sampleList = new ArrayList<House>();
	   }
	   if(newSampleList == null){
		   return false;
	   }
	   else{ 
		   boolean repeat = false;
		   for(int i = 0; i < newSampleList.size();){
			   //exclude target house
			   if(newSampleList.get(i) == null){
				   newSampleList.remove(i);
			   }
			   if(newSampleList.get(i).address.compareTo(target.address) == 0){
	        		newSampleList.remove(i);
	        		continue;
			   }
			   //exclude same house
			   for(int j = 0; j < sampleList.size(); j++){
				   if(newSampleList.get(i).address.compareTo(sampleList.get(j).address) == 0){
					   newSampleList.remove(i);
					   repeat = true;
					   break;
		           }
			   }
			   if(!repeat){
				   sampleList.add(newSampleList.get(i));
				   i++;
			   }
			   else{
				   repeat = false;
			   }
		   }
		   return true;
	   }
   }

	
    public void sampleFilter(int maxSampleNumber){
    	sampleList = PreProcessor.floorSizeFilter(sampleList, target, 0.7, maxSampleNumber);
    	System.err.println("sample number");
        System.err.println(sampleList.size());
    }
    
    public void predict() throws Exception{
    	int variableNumber = 6;
    	
    	double[][] sampleVariable = new double[sampleList.size()][variableNumber];
    	double[] sampleSolution = new double[sampleList.size()];
    	double[][] targetVariable  = new double[1][variableNumber];
    	

    	for(int i = 0; i < sampleList.size(); i++){
    		sampleVariable[i] = PreProcessor.variableTransform(sampleList.get(i));
    		sampleSolution[i] = sampleList.get(i).lastSoldPrice;
    	}
    	targetVariable[0] = PreProcessor.variableTransform(target);
    	
    	//find best lamda
    	double lamda = -1.0;
    	double bestLamda =  -1.0;
    	double theta = -1.0;
    	double bestTheta = -1.0;
    	double theta1 = -1.0;
    	double bestTheta1 = -1.0;
    	double minRss = Double.MAX_VALUE;
    	LeastSquare bestL = new LeastSquare(sampleVariable, sampleSolution);
    	
    	for(lamda = -1.0; lamda < 1.001; lamda += 0.1){
    		double[] sampleSolutionT = BoxCoxTransform.transform(sampleSolution, lamda); 
    		for(theta = -1.0; theta < 1.001; theta += 0.1){
    			double[][] sampleVariableT = BoxCoxTransform.transform(sampleVariable, theta, 1);
    			for(theta1 = -1.0; theta1 < 1.001; theta1 += 0.1){
    				sampleVariableT = BoxCoxTransform.transform(sampleVariableT, theta1, 2);
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
//    				System.err.println(result[0]);
    				if(Double.isNaN(result[0])){
//    					System.err.println("NaN");
    				}
    			}
    		}
    	}
    	
    	double[][] targetVariableT = BoxCoxTransform.transform(targetVariable, bestTheta, 1);
    	targetVariableT = BoxCoxTransform.transform(targetVariableT, bestTheta1, 2);
    	double predictPriceT = bestL.solve(targetVariableT);
    	double predictPrice = Math.pow(predictPriceT, 1.0 / bestLamda);
    	target.predictPrice = predictPrice;
    	
    	System.err.println("predictPrice");
    	System.err.println(predictPrice);
    	System.err.println("bestLamda");
    	System.err.println(bestLamda);
    	System.err.println("bestTheta");
    	System.err.println(bestTheta);
    	System.err.println("bestTheta1");
    	System.err.println(bestTheta1);
    	bestL.showSolution();
    }
    /*
    public void predict() throws Exception{
    	double[][] a;
    	double[] b;
    	double[][] test;
    	
    	LeastSquare l;
    	
    	double lamda = -1.0;
    	double bestLamda = -1.0;
    	double minRss = Double.MAX_VALUE;
    	test = PreProcessor.targetVariableTransform(target);
    	
    	
    	//find best lamda
    	do{
	    	a = PreProcessor.sampleVariableTransform(sampleList);
	    	b = PreProcessor.sampleSolutionTransform(sampleList, lamda);
	    	LeastSquare lTemp = new LeastSquare(a, b);
	    	double rss = lTemp.rss(a, b, lamda);
//	    	System.err.println("lamda: " + lamda);
//	    	System.err.println("rss: " + rss);  
//	    	System.err.println("predict: " + lTemp.solve(test, lamda));
	    	
	    	if(rss < minRss){
	    		bestLamda = lamda;
	    		minRss = rss;
	    		
	    	}
	    	
	    	lamda += 0.01;
    	}while(lamda < 1.001);
    	
    	System.err.println("best lamda: " + bestLamda);
    	
    	a = PreProcessor.sampleVariableTransform(sampleList);
    	b = PreProcessor.sampleSolutionTransform(sampleList, bestLamda);
        
        l = new LeastSquare(a, b);
        target.predictPrice = l.solve(test, bestLamda);
        l.showSolution();
        
        for(int i = 0; i < sampleList.size(); i++){
        	
			System.out.println(sampleList.get(i).address + "," + 
								sampleList.get(i).floorSize + "," + 
								sampleList.get(i).lotSize + "," +
								sampleList.get(i).bedroomNumber + "," +
								sampleList.get(i).bathroomNumber + "," +
								(2017 - sampleList.get(i).builtYear) + "," +
								sampleList.get(i).lastSoldPrice + "," + 
								l.solve(PreProcessor.targetVariableTransform(sampleList.get(i)), bestLamda));
		}
    }
	*/
	public boolean isTargetSet() {
		if(target != null){
			return true;
		}
		return false;
	}

}
