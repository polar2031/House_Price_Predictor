import java.io.*;
import java.util.*;

public class Tester{
    public Tester(){
    }

    public boolean start(){
        // House h = new House("34 Longmeadow Ln", "Town of Sharon", "ma", "02067");
        // ZillowParser z = new ZillowParser();
        // z.parseSameCityHouses(h);
        try{
            File folder = new File("./houseData/");
            File files[] = folder.listFiles();
            for(int i = 0; i < files.length; i++){
                FileReader f = new FileReader(files[i].toString());
                House temp = new House();
                if(temp.readFromFile(f)){
                    PricePredictor p = new PricePredictor();
                    p.start(temp);
                }
                f.close();
            }
        }
        catch(IOException ioe){
            return false;
        }
        return true;
    }
}
