import java.io.*;
import java.util.*;
import java.util.logging.*;


public class Tester{
    public Tester(){
    }

    public boolean start(){
        try{
            File folder = new File("./houseUrl/");
            File files[] = folder.listFiles();
            for(int i = 0; i < files.length; i++){
                FileReader f = new FileReader(files[i].toString());
                BufferedReader br = new BufferedReader(f);
                String url = null;
                while((url = br.readLine()) != null){
                    House h = new House();
                    ZillowParser z = new ZillowParser();
                    z.parseHouseDetailPage(h, url);
                    PricePredictor p = new PricePredictor();
                    p.start(h);
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
