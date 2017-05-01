package predictor.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import predictor.model.data.Area;
import predictor.model.data.House;
import predictor.model.parser.ZillowParser;

public class Sampler {
	public static List<String> getSampleCoordinateList(Double latitude, Double longitude) {
		double range = 1;
		
		double EARTH_RADIUS = 3963.1906;
        double milesPerlatitude = EARTH_RADIUS * Math.PI / 180.0;
        double milesPerlongitude = Math.abs(EARTH_RADIUS * Math.PI / 180.0 * Math.cos(longitude));
        double latitudeRange = range / milesPerlatitude;
        double longitudeRange = range / milesPerlongitude;

        int minlatitude100 = latitudeTransform(latitude, -latitudeRange);
        int maxlatitude100 = latitudeTransform(latitude, latitudeRange);
        int minlongitude100 = longitudeTransform(longitude, -longitudeRange);
        int maxlongitude100 = longitudeTransform(longitude, longitudeRange);
        
        List<String> list = new ArrayList<String>();
        
        for(int i = minlatitude100; i <= maxlatitude100; i++){
        	for(int j = minlongitude100; j <= maxlongitude100; j++){
        		String sampleFileName = Integer.valueOf(i).toString() + "," + Integer.valueOf(j).toString();
        		list.add(sampleFileName);
        	}
        }
		return list;
	}
	
	public static boolean isDataOfCoordinateUp2Date(String coordinate) {
		File areaData = new File("data" + File.separator + coordinate + ".dat");
//		if(areaData.exists() && FileUtils.isFileNewer(areaData,; System.currentTimeMillis() - (25920000))){
		System.err.println(areaData.lastModified());
		System.err.println("System time: " + System.currentTimeMillis());
		System.err.println(-areaData.lastModified() + System.currentTimeMillis() - (25920000));
	    if(areaData.exists()){
	    	return false;
	    }
	    else{
	    	return true;
	    }
	}
	public static boolean updateDataOfCoordinate(String coordinate) {
		try{
	    	File areaData = new File("data" + File.separator + coordinate + ".dat");
		    FileUtils.forceMkdirParent(areaData);
		    Area a;
	    	double latitude = Sampler.latitudeReduction(Integer.parseInt(coordinate.split(",")[0]));
	    	double longitude = Sampler.longitudeReduction(Integer.parseInt(coordinate.split(",")[1]));
	    	List<House> temp = ZillowParser.parseArea(latitude, longitude, 0.01, 0.01);
	    	a = new Area(temp);
	    	FileOutputStream fos = new FileOutputStream("data" + File.separator + coordinate + ".dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(a);
			oos.close();
			return true;
		}
		catch(IOException e){
			return false;
		}
		
	}
	
    public static List<House> getDataOfCoordinate(String coordinate) throws Exception{
    	List<House> sampleList;
    	File areaData = new File("data" + File.separator + coordinate + ".dat");
	    Area a;
    	if(areaData.exists()){
	    	FileInputStream fis = new FileInputStream("data" + File.separator + coordinate + ".dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			a = (Area)ois.readObject();
			ois.close();
	    }
    	else{
    		return null;
    	}
	    
	    sampleList = new ArrayList<House>(a.HouseList);

    	for(int i = 0; i < a.HouseList.size(); i++){
            sampleList.add(a.HouseList.get(i));
        }
    	
    	return sampleList;
    }
    
	public static int latitudeTransform(double latitude, double latitudeRange){
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
	
	public static double latitudeReduction(int transferedLatitude){
		return (double)transferedLatitude / 100;
	}
	
	public static int longitudeTransform(double longitude, double longitudeRange){
		return (Double.valueOf(((longitude + (longitudeRange / 2)) * 100)).intValue() + 18000) % 36000;
	}
	
	public static double longitudeReduction(int transferedLongitude){
		return (double)(transferedLongitude - 18000) / 100;
	}
}
