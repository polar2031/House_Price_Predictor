import java.io.*;
import java.util.*;
import java.util.logging.*;


public class Tester{
    public Tester(){
    }

    public boolean start(){
        try{
            Logger logger = Logger.getLogger("test");
            FileHandler handler = new FileHandler("./testResult.log",true);
            logger.addHandler(handler);
            logger.setLevel(Level.ALL);
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);

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
                    logger.info(String.valueOf(h.lastSoldPrice));
                    logger.info(String.valueOf(h.predictBasePrice));
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
