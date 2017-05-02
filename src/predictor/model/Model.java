package predictor.model;

import java.util.ArrayList;
import java.util.List;

import predictor.model.data.House;
import predictor.model.parser.ZillowParser;
import predictor.model.regression.LeastSquare;
import predictor.model.regression.PreProcessor;

public class Model {
	public House target;
	public List<House> sampleList;
	public List<Double> data;
	
    public boolean setTarget(String address, String city, String state, String zip){
    	target = null;
    	sampleList = new ArrayList<House>();
    	try{
	    	House h = ZillowParser.parseHouseThrowAddress(address, state, zip);
	    	if(h != null){
	    		target = h;
	    		return true;
	    	}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return false;
    	
    }
    
   public boolean addSamples(List<House> newSampleList) {
	   if(newSampleList == null){
		   return false;
	   }
	   else{ 
		   boolean repeat = false;
		   for(int i = 0; i < newSampleList.size();){
			   //exclude target house
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
    	sampleList = PreProcessor.sampleFilter(sampleList, target, maxSampleNumber);
    }
    
    
    
    public void predict() throws Exception{
    	double[][] a;
    	double[] b;
    	double[][] test;
    	
    	LeastSquare l;
    	
    	double lamda = -1.0;
    	double bestLamda = -1.0;
    	double minRss = Double.MAX_VALUE;
    	test = PreProcessor.targetVariableTransform(target);
    	
    	do{
	    	a = PreProcessor.sampleVariableTransform(sampleList);
	    	b = PreProcessor.sampleSolutionTransform(sampleList, lamda);
	    	LeastSquare lTemp = new LeastSquare(a, b);
	    	double rss = lTemp.rss(a, b, lamda);
	    	System.err.println("lamda: " + lamda);
	    	System.err.println("rss: " + rss);
	    	System.err.println("predict: " + lTemp.solve(test, lamda));
	    	
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
        
        
    }
}
