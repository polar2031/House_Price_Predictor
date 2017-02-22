import java.io.*;
import java.util.logging.Logger;

public class HousePricePredictor{
    public static void main(String[] args) throws Exception {
        CustomLogger.init();
        Logger logger = Logger.getLogger("mainLogger");
        logger.info("program start");
        if(args.length > 0 && args[0].equals("test")){
            logger.info("Enter testing mode.");
            Tester t = new Tester();
            t.start();
        }
        else{
            GUI g = new GUI();
        }
    }
}
