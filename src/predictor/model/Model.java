package predictor.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

import predictor.model.data.Area;
import predictor.model.data.House;
import predictor.model.parser.ZillowParser;

public class Model {
	public House target;
	public ArrayList<House> sampleList;
	
	
	
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
    
    public void fixSample(){
        for(int i = 0; i < sampleList.size(); ){
        	if(sampleList.get(i).address.compareTo(target.address) == 0){
        		sampleList.remove(i);
        		continue;
        	}
        	for(int j = i + 1; j < sampleList.size(); j++){
	            if(sampleList.get(i).address.compareTo(sampleList.get(j).address) == 0){
	            	sampleList.remove(j);
	            }
        	}
        	i++;
        }
    }
    
    
    public boolean getDataOfCoordinate(String coordinate) throws Exception{
    	File areaData = new File("data" + File.separator + coordinate + ".dat");
	    FileUtils.forceMkdirParent(areaData);
	    Area a;
//	    if(areaData.exists() && FileUtils.isFileNewer(areaData, System.currentTimeMillis() - (25920000))){
    	if(areaData.exists()){
	    	FileInputStream fis = new FileInputStream("data" + File.separator + coordinate + ".dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			a = (Area)ois.readObject();
			ois.close();
//			System.err.println("data read");
	    }
	    else{
	    	double latitude = latitudeReduction(Integer.parseInt(coordinate.split(",")[0]));
	    	double longitude = longitudeReduction(Integer.parseInt(coordinate.split(",")[1]));
	    	ArrayList<House> temp = ZillowParser.parseArea(latitude, longitude, 0.01, 0.01);
	    	a = new Area(temp);
	    	FileOutputStream fos = new FileOutputStream("data" + File.separator + coordinate + ".dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(a);
			oos.close();
	    }
	    
	    if(sampleList == null){
	    	sampleList = new ArrayList<House>(a.HouseList);
	    }
	    else{
	    	for(int i = 0; i < a.HouseList.size(); i++){
	            sampleList.add(a.HouseList.get(i));
	        }
	    }
//	    System.err.println(sampleList.size());
	    
    	return true;
    	
    }

	public ArrayList<String> getSampleDataList() throws Exception{
		double range = 1;
		
		double EARTH_RADIUS = 3963.1906;
        double milesPerlatitude = EARTH_RADIUS * Math.PI / 180.0;
        double milesPerlongitude = Math.abs(EARTH_RADIUS * Math.PI / 180.0 * Math.cos(target.longitude));
        double latitudeRange = range / milesPerlatitude;
        double longitudeRange = range / milesPerlongitude;

        int minlatitude100 = latitudeTransfer(target.latitude, -latitudeRange);
        int maxlatitude100 = latitudeTransfer(target.latitude, latitudeRange);
        int minlongitude100 = longitudeTransfer(target.longitude, -longitudeRange);
        int maxlongitude100 = longitudeTransfer(target.longitude, longitudeRange);
        
        ArrayList<String> list = new ArrayList<String>();
//        System.err.println(minlatitude100);
//        System.err.println(maxlatitude100);
//		System.err.println(minlongitude100);
//		System.err.println(maxlongitude100);
        
        for(int i = minlatitude100; i <= maxlatitude100; i++){
        	for(int j = minlongitude100; j <= maxlongitude100; j++){
        		String sampleFileName = Integer.valueOf(i).toString() + "," + Integer.valueOf(j).toString();
        		list.add(sampleFileName);
        	}
        }

		return list;
	}
	
	public int latitudeTransfer(double latitude, double latitudeRange){
		int returnValue = Double.valueOf((latitude + (latitudeRange / 2)) * 100).intValue();
		if(returnValue > 9000){
			return 9000;
		}
		else if(returnValue < -9000){
			return -9000;
		}
		else{
			return returnValue;
		}
	}
	
	public double latitudeReduction(int transferedLatitude){
		return (double)transferedLatitude / 100;
	}
	
	public int longitudeTransfer(double longitude, double longitudeRange){
		return (Double.valueOf(((longitude + (longitudeRange / 2)) * 100)).intValue() + 18000) % 36000;
	}
	
	public double longitudeReduction(int transferedLongitude){
		return (double)(transferedLongitude - 18000) / 100;
	}
	
    public void sampleFilter(){
        int sampleNumber = 5;

        // initial
        for(int i = 0; i < sampleList.size(); i++){
            sampleList.get(i).similarity = 0;
            if(sampleList.get(i).lastSoldPrice == null){
            	sampleList.remove(i);
            }
        }
        ArrayList<House> newSamples;
        
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
//        if(sampleList.size() >= sampleNumber * 2){
//        	
//	         // lot size
//	         for(int i = 0; i < sampleList.size(); i++){
//	             double lotSimilarity = Math.abs(target.lotSize / (sampleList.get(i).lotSize - target.lotSize));
//	             sampleList.get(i).similarity += lotSimilarity;
//	         }
//	
//	        // take top 2n similar sample
//	        newSamples = new ArrayList<House>();
//	        for(int i = 0; i < sampleNumber * 2; i++){
//	            double tempS = 0;
//	            int index = 0;
//	            for(int j = 0; j < sampleList.size(); j++){
//	                if(sampleList.get(j).similarity > tempS){
//	                    tempS = sampleList.get(j).similarity;
//	                    index = j;
//	                }
//	            }
//	            newSamples.add(sampleList.get(index));
//	            sampleList.remove(index);
//	        }
//	        sampleList = newSamples;
//        }
//        
//        if(sampleList.size() >= sampleNumber){
//	        newSamples = new ArrayList<House>();
//	        double pricePerSqft = 0;
//	        for(int i = 0; i < sampleList.size(); i++){
//	        	pricePerSqft += sampleList.get(i).lastSoldPrice / (sampleList.get(i).floorSize + sampleList.get(i).lotSize / 7);
//	        }
//	        pricePerSqft /= sampleList.size();
//	        for(int i = 0; i < sampleList.size(); i++){
//	            double pricePerSqftSimilarity = Math.abs(pricePerSqft / (sampleList.get(i).lastSoldPrice / (sampleList.get(i).floorSize + sampleList.get(i).lotSize / 7) - pricePerSqft));
//	            sampleList.get(i).similarity += pricePerSqftSimilarity;
//	        }
//	        for(int i = 0; i < sampleNumber; i++){
//	            double tempS = 0;
//	            int index = 0;
//	            for(int j = 0; j < sampleList.size(); j++){
//	                if(sampleList.get(j).similarity > tempS){
//	                    tempS = sampleList.get(j).similarity;
//	                    index = j;
//	                }
//	            }
//	            newSamples.add(sampleList.get(index));
//	            sampleList.remove(index);
//	        }
//	        sampleList = newSamples;
//        }
        
        for(int i = 0; i < sampleList.size(); ){
        	System.err.println(sampleList.get(i).similarity);
            if(sampleList.get(i).similarity < 0.9){
            	sampleList.remove(i);
            	continue;
            }
            i++;
        }
        
        System.err.println("sample number: " + sampleList.size());
        return;
    }
    
    public void predict() throws Exception{
        leastSquare();
    }
    public void leastSquare() throws Exception{

        int thisYear = 2017;
        //number of features
        int vNumber = 6;
        //features
        double[][] a;
        //price
        double[] b;

        int totalWeight = 0;
        for(int i = 0; i < sampleList.size(); i++){
//            totalWeight += sampleList.get(i).weight;
        	totalWeight += 1;
            // System.out.println(totalWeight);
        }

        a = new double[totalWeight][vNumber];
        b = new double[totalWeight];

        int c = 0;
        for(int i = 0; i < sampleList.size(); i++){
//            for(int j = 0; j < sampleList.get(i).weight; j++){
            for(int j = 0; j < 1; j++){
                a[c][0] = (double)sampleList.get(i).floorSize;
                a[c][1] = (double)sampleList.get(i).lotSize;
                // a[c][1] = 0;
                a[c][2] = (double)sampleList.get(i).bedroomNumber;
                a[c][2] = 0;
                a[c][3] = (double)sampleList.get(i).bathroomNumber;
                a[c][3] = 0;
                a[c][4] = (double)(sampleList.get(i).builtYear - thisYear);
//                a[c][4] = 0;
                a[c][5] = 1;
                // a[c][6] = 0;
                // a[c][7] = 0;
                // a[c][8] = 0;
                // switch(sampleList.get(i).lastSoldDate.getMonth()){
                //     case 0:
                //     case 1:
                //     case 2:
                //         a[c][5] = 1;
                //         break;
                //     case 3:
                //     case 4:
                //     case 5:
                //         a[c][6] = 1;
                //         break;
                //     case 6:
                //     case 7:
                //     case 8:
                //         a[c][7] = 1;
                //         break;
                //     case 9:
                //     case 10:
                //     case 11:
                //         a[c][8] = 1;
                //         break;
                //     default:
                //         break;
                // }
//                b[c] = Math.log((double)sampleList.get(i).lastSoldPrice);
                b[c] = (double)sampleList.get(i).lastSoldPrice;
                c++;
            }
        }

        RealMatrix coefficients = new Array2DRowRealMatrix(a, false);
        DecompositionSolver solver = new SingularValueDecomposition(coefficients).getSolver();
        RealMatrix constants = new Array2DRowRealMatrix(b);
        RealMatrix solution = solver.solve(constants);

        // double[][] test = {{target.floorSize,
        //                     target.lotSize,
        //                     target.bedroomNumber,
        //                     target.bathroomNumber,
        //                     (double)(target.builtYear-thisYear),
        //                     1,
        //                     0,
        //                     0,
        //                     0
        //                     }};
        double[][] test = {{target.floorSize,
                            target.lotSize,
                            target.bedroomNumber,
                            target.bathroomNumber,
                            (double)(target.builtYear-thisYear),
                            1
                            }};
        RealMatrix tMatrix = new Array2DRowRealMatrix(test);
        RealMatrix p = tMatrix.multiply(solution);
//        target.predictPrice = Math.exp(p.getEntry(0, 0));
        target.predictPrice = p.getEntry(0, 0);

        // show solution
        double[][] o = solution.getData();
        String value[] = {"floor size", "lot size", "bedroom", "bathroom", "age", "base", "summer", "fall", "winter"};
        System.err.println("Variables");
        for(int i = 0; i < solution.getRowDimension(); i++){
            System.err.print(value[i] + ": ");
            for(int j = 0; j < solution.getColumnDimension(); j++){
                System.err.print(o[i][j]);
            }
            System.err.println("");
        }
    }
}
