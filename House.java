import java.io.*;
import java.util.Date;

// import com.google.gson.*;
// import org.jsoup.*;

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

    // sold info
    public boolean recentlySold;
    public Double lastSoldPrice;
    public Date lastSoldDate;

    // price
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

    //count distance between two houses using their coordinates
    public double getDirectDistance(House h){
        //earth radius
        //6378.137 km = 3963.1906 mile
        double EARTH_RADIUS = 3963.1906;
        double radLat1 = h.latitute * Math.PI / 180.0;
        double radLat2 = this.latitute * Math.PI / 180.0;
        double a = radLat1 - radLat2;
        double b = (h.longtitute * Math.PI / 180.0) - (this.longtitute * Math.PI / 180.0);
        double s = 2.0 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000.0) / 10000.0;
        return s;
    }

}
