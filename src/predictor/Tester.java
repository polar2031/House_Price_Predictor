package predictor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import predictor.controller.Common;
import predictor.controller.task.MessagePack;
import predictor.model.PredictModel;
import predictor.model.Sampler;
import predictor.model.textAnalyzer;
import predictor.model.data.House;
import predictor.model.regression.PreProcessor;

public class Tester {
	public static void main(String[] args) {
		
		// stop logger from parser
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.SEVERE); 
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		
		List<House> testHouses = null;
		
		
		String zip = "02062";
		System.out.println(zip);
		
		try{
			if(!Sampler.isDataOfZipUp2Date(zip)){
				Sampler.updateDataOfzip(zip);
			}
			testHouses = Sampler.getDataOfZip(zip);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//remove testing data with missing/error feature
		testHouses = PreProcessor.basicFilter(testHouses);
		System.out.println(testHouses.size());
//		int bestSampleNumber = 30;
//		int bestSampleNumber = 17; //02067
//		int bestSampleNumber = 7; //02169 317
//		int bestSampleNumber = 12; //02186 243
//		int bestSampleNumber = 3; //02740 66
//		int bestSampleNumber = 7; //02747 65
//		int bestSampleNumber = 6; //02072 307
//		int bestSampleNumber = 26; //02021 348
//		int bestSampleNumber = 11; //02081
		int bestSampleNumber = 5; //02062

		int testMode = 1;
		
//		textAnalyzer a = new textAnalyzer();
//		a.analysis(testHouses);
		
//		System.out.println("BASEMENT_FEATURE: Unfinished Basement, BASEMENT_FEATURE: Unfinished Basement".split(",?\\W?\\w+:\\W?")[0]);
		
		//test sample number
		if(testMode == 0){
			for(int s = 2; s < testHouses.size(); s += 1){
				double[] errorPercentage = new double[testHouses.size()];
				
				for(int i = 0; i < testHouses.size(); i++){
					House target = testHouses.get(i);
					// chose similar houses
					List<House> sampleList = new ArrayList<House>(testHouses);
					PredictModel m = new PredictModel();
					m.setSampleNumber(s);
					m.setTarget(target);
					m.setSamples(sampleList);
					m.predict(4);
					
					errorPercentage[i] = Math.abs((target.predictPrice - target.lastSoldPrice) / target.lastSoldPrice);
	
				}
	
				int five = 0;
				int ten = 0;
				int fifteen = 0;
				int twenty = 0;
				for(int i = 0; i < errorPercentage.length; i++){
					if(errorPercentage[i] < 0.05){
						five++;
						ten++;
						fifteen++;
						twenty++;
					}
					else if(errorPercentage[i] < 0.1){
						ten++;
						fifteen++;
						twenty++;
					}
					else if(errorPercentage[i] < 0.15){
						fifteen++;
						twenty++;
					}
					else if(errorPercentage[i] < 0.2){
						twenty++;
					}
				}
				System.out.println(s);
				System.out.println(String.valueOf((double)five / (double)errorPercentage.length));
				System.out.println(String.valueOf((double)ten / (double)errorPercentage.length));
				System.out.println(String.valueOf((double)fifteen / (double)errorPercentage.length));
				System.out.println(String.valueOf((double)twenty / (double)errorPercentage.length));
				System.out.println("");
	 
			}
		}
		//test method
		else if(testMode == 1){
			for(int s = 0; s < 5; s += 1){
				double[] errorPercentage = new double[testHouses.size()];
				
				for(int i = 0; i < testHouses.size(); i++){
					House target = testHouses.get(i);
					// chose similar houses
					List<House> sampleList = new ArrayList<House>(testHouses);
					PredictModel m = new PredictModel();
					m.setSampleNumber(bestSampleNumber);
					m.setTarget(target);
					m.setSamples(sampleList);
					m.predict(s);
					
					errorPercentage[i] = Math.abs((target.predictPrice - target.lastSoldPrice) / target.lastSoldPrice);
					DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//					System.out.println(df.format(target.lastSoldDate) + "," + target.lastSoldPrice + "," + target.floorSize + "," + target.lotSize + 
//							"," + target.bedroomNumber + "," + target.bathroomNumber + "," + target.builtYear + "," + 
//							target.longitude + "," + target.latitude + "," + target.address + "," + 
//							target.lastSoldPrice / target.floorSize + "," + target.predictPrice);
	
				}
	
				int five = 0;
				int ten = 0;
				int fifteen = 0;
				int twenty = 0;
				for(int i = 0; i < errorPercentage.length; i++){
					if(errorPercentage[i] < 0.05){
						five++;
						ten++;
						fifteen++;
						twenty++;
					}
					else if(errorPercentage[i] < 0.1){
						ten++;
						fifteen++;
						twenty++;
					}
					else if(errorPercentage[i] < 0.15){
						fifteen++;
						twenty++;
					}
					else if(errorPercentage[i] < 0.2){
						twenty++;
					}
				}
				String[] methodName = {"Price/Sqft", "Price/Sqft (Select similiar Houses)", 
						"Linear Regression",  
						"Linear Regression (Price/sqft)", "New"};
				System.out.println(methodName[s]);
				System.out.println(String.valueOf((double)five / (double)errorPercentage.length));
				System.out.println(String.valueOf((double)ten / (double)errorPercentage.length));
				System.out.println(String.valueOf((double)fifteen / (double)errorPercentage.length));
				System.out.println(String.valueOf((double)twenty / (double)errorPercentage.length));
				System.out.println("");
	 
			}
		}
		else{
			for(int s = 0; s < 5; s += 1){
				double[] errorPercentage = new double[testHouses.size()];
				
				for(int i = 0; i < testHouses.size(); i++){
					House target = testHouses.get(i);
					// chose similar houses
					List<House> sampleList = new ArrayList<House>(testHouses);
					PredictModel m = new PredictModel();
					m.setSampleNumber(bestSampleNumber);
					m.setTarget(target);
					m.setSamples(sampleList);
					m.predict(s);
					
					errorPercentage[i] = Math.abs((target.predictPrice - target.lastSoldPrice) / target.lastSoldPrice);
					System.out.println(target.lastSoldPrice + "," + target.floorSize + "," + target.lotSize + 
							"," + target.bedroomNumber + "," + target.bathroomNumber + "," + target.builtYear + "," + 
							target.longitude + "," + target.latitude + "," + target.address + "," + 
							target.lastSoldPrice / target.floorSize + "," + target.predictPrice);
	
				}
	
				int five = 0;
				int ten = 0;
				int fifteen = 0;
				int twenty = 0;
				for(int i = 0; i < errorPercentage.length; i++){
					if(errorPercentage[i] < 0.05){
						five++;
						ten++;
						fifteen++;
						twenty++;
					}
					else if(errorPercentage[i] < 0.1){
						ten++;
						fifteen++;
						twenty++;
					}
					else if(errorPercentage[i] < 0.15){
						fifteen++;
						twenty++;
					}
					else if(errorPercentage[i] < 0.2){
						twenty++;
					}
				}
				String[] methodName = {"Price/Sqft", "Price/Sqft (Select similiar Houses)", 
						"Linear Regression", "Linear Regression (Select similiar Houses)", 
						"Linear Regression (Transfer Price with Log)", "Linear Regression (Transfer Price with Log)(Select similiar Houses)"};
				System.out.println(methodName[s]);
				System.out.println(String.valueOf((double)five / (double)errorPercentage.length));
				System.out.println(String.valueOf((double)ten / (double)errorPercentage.length));
				System.out.println(String.valueOf((double)fifteen / (double)errorPercentage.length));
				System.out.println(String.valueOf((double)twenty / (double)errorPercentage.length));
				System.out.println("");
	 
			}
		}
	}
}
