package predictor.model;

import java.util.ArrayList;
import java.util.List;

import predictor.model.data.House;
import predictor.model.parser.ZillowParser;
import predictor.model.regression.LeastSquare;

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

	
    public void sampleFilter(){
        int sampleNumber = 40;

        // initial
        for(int i = 0; i < sampleList.size(); i++){
            sampleList.get(i).similarity = 0;
            if(sampleList.get(i).lastSoldPrice == null){
            	sampleList.remove(i);
            }
        }
        List<House> newSamples;
        
        if(sampleList.size() >= sampleNumber * 4){
	        // floor size
	        for(int i = 0; i < sampleList.size(); i++){
	        	
	            double sizeSimilarity = (sampleList.get(i).floorSize / target.floorSize) > 1 ? (target.floorSize / sampleList.get(i).floorSize) : (sampleList.get(i).floorSize / target.floorSize);
	            sampleList.get(i).similarity += sizeSimilarity;
	        }
	        
	        // take top 4n similar sample
	        newSamples = new ArrayList<House>();
	        for(int i = 0; i < sampleNumber * 4; i++){
	            double tempS = 0;
	            int index = 0;
	            for(int j = 0; j < sampleList.size(); j++){
	                if(sampleList.get(j).similarity > tempS){
	                    tempS = sampleList.get(j).similarity;
	                    index = j;
	                }
	            }
	            newSamples.add(sampleList.get(index));
	            sampleList.remove(index);
	        }
	        sampleList = newSamples;
        }
        
        for(int i = 0; i < sampleList.size(); ){
        	System.err.println(sampleList.get(i).similarity);
            if(sampleList.get(i).similarity < 0.75){
            	sampleList.remove(i);
            	continue;
            }
            i++;
        }
        
        System.err.println("sample number: " + sampleList.size());
        return;
    }
    
    public void predict() throws Exception{
    	int thisYear = 2017;
    	int vNumber = 6;
    	double[][] a = new double[sampleList.size()][vNumber];
    	double[] b = new double[sampleList.size()];
    	LeastSquare l;
    	
    	
    	//build data
        for(int i = 0; i < sampleList.size(); i++){
        	a[i][0] = 0;
            a[i][1] = sampleList.get(i).floorSize;
            a[i][2] = sampleList.get(i).lotSize;
            a[i][2] = 0;
            a[i][3] = sampleList.get(i).bedroomNumber;
            a[i][3] = 0;
            a[i][4] = sampleList.get(i).bathroomNumber;
            a[i][4] = 0;
            a[i][5] = (double)(sampleList.get(i).builtYear - thisYear);
            a[i][5] = 0;
            b[i] = (double)sampleList.get(i).lastSoldPrice;
        }
    	
        double[][] test = {{0, target.floorSize,
            target.lotSize,
            target.bedroomNumber,
            target.bathroomNumber,
            (double)(target.builtYear-thisYear)}};
        
        l = new LeastSquare(a, b);
        target.predictPrice = l.solve(test);
        
        
        
        // box-cox test
        LeastSquare bestL;
        double minRss;
        double bestLamda;
        double[][] aT = new double[sampleList.size()][vNumber];
    	double[] bT = new double[sampleList.size()];
    	double[][] testT = new double[1][vNumber];
        
        for(int i = 0; i < sampleList.size(); i++){
        	aT[i][0] = a[i][0];
            aT[i][1] = Math.log(a[i][1]);
            aT[i][2] = a[i][2];
            aT[i][3] = a[i][3];
            aT[i][4] = a[i][4];
            aT[i][5] = a[i][5];
            bT[i] = Math.log(b[i]);
        }
        l = new LeastSquare(aT, bT);
        minRss = l.rss(aT, bT);
        bestLamda = 0;
        testT[0][0] = test[0][0];
        testT[0][1] = Math.log(test[0][1]);
        testT[0][2] = test[0][2];
        testT[0][3] = test[0][3];
        testT[0][4] = test[0][4];
        testT[0][5] = test[0][5];
        
        System.err.println("price: " + Math.pow(Math.E, l.solve(testT)));
        l.showSolution();
        
        
//        for(double lamda = -1; lamda <= 1; lamda += 0.1){
//        	if(lamda > -0.1 && lamda < 0.1){
//        		lamda += 0.1;
//        	}
//        	else{
//                for(int i = 0; i < sampleList.size(); i++){
//                	aT[i][0] = a[i][0];
//                    aT[i][1] = a[i][1];
//                    aT[i][2] = a[i][2];
//                    aT[i][3] = a[i][3];
//                    aT[i][4] = a[i][4];
//                    aT[i][5] = a[i][5];
//                    bT[i] = (Math.pow(b[i], lamda) - 1) / lamda;
//                }
//        	}
//            l = new LeastSquare(aT, bT);
//            if(l.rss(aT, bT) < minRss){
//            	minRss = l.rss(aT, bT);
//            	bestLamda = lamda;
//            }
//        }
//        if(bestLamda > -0.1 && bestLamda < 0.1){
//            for(int i = 0; i < sampleList.size(); i++){
//            	aT[i][0] = a[i][0];
//                aT[i][1] = a[i][1];
//                aT[i][2] = a[i][2];
//                aT[i][3] = a[i][3];
//                aT[i][4] = a[i][4];
//                aT[i][5] = a[i][5];
//                bT[i] = Math.log(b[i]);
//            }
//            l = new LeastSquare(aT, bT);
//            target.predictPrice = Math.pow(Math.E, l.solve(test));
//            l.showSolution();
//            l.rss(a, b);
//        }
//        else{
//            for(int i = 0; i < sampleList.size(); i++){
//            	aT[i][0] = a[i][0];
//                aT[i][1] = a[i][1];
//                aT[i][2] = a[i][2];
//                aT[i][3] = a[i][3];
//                aT[i][4] = a[i][4];
//                aT[i][5] = a[i][5];
//                bT[i] = (Math.pow(b[i], bestLamda) - 1) / bestLamda;
//            }
//            l = new LeastSquare(aT, bT);
//            target.predictPrice = Math.log(l.solve(test) * bestLamda + 1) / Math.log(bestLamda);
//            l.showSolution();
//            l.rss(a, b);
//        }
        
    }
}
