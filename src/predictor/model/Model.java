package predictor.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

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
    
    public boolean getDataOfCoordinate(String coordinate) throws Exception{
    	File areaData = new File("data" + File.separator + coordinate + ".dat");
	    FileUtils.forceMkdirParent(areaData);
	    Area a;
	    if(areaData.exists() && FileUtils.isFileNewer(areaData, System.currentTimeMillis() - (2592000))){
	    	FileInputStream fis = new FileInputStream("data" + File.separator + coordinate + ".dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			a = (Area)ois.readObject();
			ois.close();
			System.err.println("data read");
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
	    
	    sampleList = new ArrayList<House>(a.HouseList);
	    System.err.println(sampleList.size());
	    
    	return true;
    	
    }
    public ArrayList<House> readFromData(File f){
    	ArrayList<House> hs = new ArrayList<House>();
    	
    	return hs;
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
        System.err.println(minlatitude100);
        System.err.println(maxlatitude100);
		System.err.println(minlongitude100);
		System.err.println(maxlongitude100);
        
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
        int sampleNumber = 10;

        // initial
        for(int i = 0; i < sampleList.size(); i++){
            sampleList.get(i).similarity = 0;
        }

        if(sampleList.size() < sampleNumber){
            System.out.println(sampleList.size());
            return;
        }

        // floor size
        for(int i = 0; i < sampleList.size(); i++){
            double sizeSimilarity = Math.abs(target.floorSize / (sampleList.get(i).floorSize - target.floorSize));
            sampleList.get(i).similarity += sizeSimilarity;
        }
        // // lot size
        // for(int i = 0; i < sampleList.size(); i++){
        //     double lotSimilarity = Math.abs(target.lotSize / (sampleList.get(i).lotSize - target.lotSize));
        //     sampleList.get(i).similarity += lotSimilarity;
        //     //
        //     sampleList.get(i).weight = 1;
        //     //
        // }

        // take top n similar sample
        ArrayList<House> newSamples = new ArrayList<House>();
        for(int i = 0; i < sampleNumber; i++){
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
        System.out.println(sampleList.size());
        return;
    }
}
