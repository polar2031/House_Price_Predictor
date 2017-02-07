import java.io.*;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.*;
import org.jsoup.*;

public class House{
    // house address
    public String address;
    public String city;
    public String state;
    public String zip;

    // basic house info
    public double floorSize;
    public double lotSize;
    public double bedroomNumber;
    public double bathroomNumber;
    public double latitute;
    public double longtitute;
    public int builtYear;

    // price
    public Double lastSoldPrice;
    public String lastSoldDate;
    public Double predictBasePrice;
    public Double predictPrice;

    // weight of the sample
    public int weight;

    // non standard features
    public String features[];

    public House(){
    }

    public House(String address, String city, String state, String zip){
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public boolean getInfoFromZillow(){
        ZillowParser z = new ZillowParser();
        return z.parseHouse(this);
    }

    public boolean writeToFile(FileWriter f){
        try{
            f.write(address + "\n");
            f.write(city + "\n");
            f.write(state + "\n");
            f.write(zip + "\n");
            f.write(String.valueOf(bedroomNumber) + "\n");
            f.write(String.valueOf(bathroomNumber) + "\n");
            f.write(String.valueOf(floorSize) + "\n");
            f.write(String.valueOf(lastSoldPrice) + "\n");
            f.write(lastSoldDate + "\n");
            f.write(String.valueOf(latitute) + "\n");
            f.write(String.valueOf(longtitute) + "\n");
            f.write(String.valueOf(builtYear) + "\n");
            if(features != null){
                f.write(String.valueOf(features.length) + "\n");
                for(int i = 0; i < features.length; i++){
                    f.write(features[i] + "\n");
                }
            }
            else{
                f.write("0\n");
            }
            f.write("\n");
        }
        catch(IOException ioe){
            return false;
        }
        return true;
    }

    public boolean readFromFile(FileReader f){
        try{
            BufferedReader br = new BufferedReader(f);
            String line = null;
			String[] tempTrainData = null;

            address = br.readLine();
            city = br.readLine();
            state = br.readLine();
            zip = br.readLine();
            bedroomNumber = Double.parseDouble(br.readLine());
            bathroomNumber = Double.parseDouble(br.readLine());
            floorSize = Double.parseDouble(br.readLine());
            lastSoldPrice = Double.parseDouble(br.readLine());
            lastSoldDate = br.readLine();
            latitute = Double.parseDouble(br.readLine());
            longtitute = Double.parseDouble(br.readLine());
            builtYear = Integer.parseInt(br.readLine());
            features = new String[Integer.parseInt(br.readLine())];
            for(int i = 0; i < features.length; i++){
                features[i] = br.readLine();
            }
            br.readLine();
        }
        catch(IOException ioe){
            return false;
        }
        return true;
    }

    //count distance between two houses using their coordinates
    public double getDirectDistance(House h){
        //earth ratius
        //6378.137 km = 3963.1906 mile
        double EARTH_RADIUS = 3963.1906;
        double radLat1 = h.latitute * Math.PI / 180.0;
        double radLat2 = this.latitute * Math.PI / 180.0;
        double a = radLat1 - radLat2;
        double b = (h.longtitute * Math.PI / 180.0) - (this.longtitute * Math.PI / 180.0);
        double s = 2.0 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000.0) / 10000.0;
        // System.out.println(s);
        return s;
    }

}
