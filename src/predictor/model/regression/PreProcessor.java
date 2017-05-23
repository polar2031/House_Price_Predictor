package predictor.model.regression;

import java.util.ArrayList;
import java.util.List;

import predictor.model.data.House;

public class PreProcessor {
	public static int variableNumber = 6;
	
	public static List<House> sampleFilter(List<House> sampleList, House target, int maxSampleNumber){

        // remove bad samples
        for(int i = 0; i < sampleList.size(); ){
            if(sampleList.get(i).lastSoldPrice == null){
            	sampleList.remove(i);
            }
            else{
            	i++;
            }
        }

		List<House> newSamples = new ArrayList<House>();
		double minSimilarity = 0.5;
		int filterSampleNumber = 0;

        if(sampleList.size() < maxSampleNumber * 4){
        	filterSampleNumber = sampleList.size();
        }
        else{
        	filterSampleNumber = maxSampleNumber * 4;
        }
        
        // floor size
        for(int i = 0; i < sampleList.size(); i++){
            double sizeSimilarity = (sampleList.get(i).floorSize / target.floorSize) > 1 ? (target.floorSize / sampleList.get(i).floorSize) : (sampleList.get(i).floorSize / target.floorSize);
            sampleList.get(i).similarity += sizeSimilarity;
        }
        
        // take top 4n similar sample
        for(int i = 0; i < filterSampleNumber; i++){
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
        
        
        for(int i = 0; i < sampleList.size(); ){
            if(sampleList.get(i).similarity < minSimilarity){
            	sampleList.remove(i);
            	continue;
            }
            i++;
        }
        
        System.err.println("sample number: " + sampleList.size());
        return sampleList;
	}
	
	public static double[][] sampleVariableTransform(List<House> sampleList){
		int thisYear = 2017;
		double[][] a = new double[sampleList.size()][variableNumber];
		
        for(int i = 0; i < sampleList.size(); i++){
        	a[i][0] = 0;
            a[i][1] = sampleList.get(i).floorSize;
            a[i][2] = sampleList.get(i).lotSize;
//            a[i][2] = 0;
            a[i][3] = sampleList.get(i).bedroomNumber;
            a[i][3] = 0;
            a[i][4] = sampleList.get(i).bathroomNumber;
            a[i][4] = 0;
            a[i][5] = (double)(sampleList.get(i).builtYear - thisYear);
//            a[i][5] = 0;
        }
        
        return a;
	}

	public static double[] sampleSolutionTransform(List<House> sampleList, double lamda) {
		double[] a = new double[sampleList.size()];
		
        for(int i = 0; i < sampleList.size(); i++){
        	a[i] = Math.pow((double)sampleList.get(i).lastSoldPrice, lamda);
        }
        
		return a;
	}

	public static double[][] targetVariableTransform(House target) {
		int thisYear = 2017;
        double[][] a = new double[1][variableNumber];
    	a[0][0] = 0;
        a[0][1] = target.floorSize;
        a[0][2] = target.lotSize;
//        a[0][2] = 0;
        a[0][3] = target.bedroomNumber;
        a[0][3] = 0;
        a[0][4] = target.bathroomNumber;
        a[0][4] = 0;
        a[0][5] = (double)(target.builtYear - thisYear);
//        a[0][5] = 0;

		return a;
	}
	

}
