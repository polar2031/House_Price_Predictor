import java.util.logging.*;

public class CustomLogger{
    public static void init(){
        try{
            SimpleFormatter formatter = new SimpleFormatter();

            Logger logger = Logger.getLogger("mainLogger");
            FileHandler handler = new FileHandler("./logfile.log",true);
            logger.addHandler(handler);
            logger.setLevel(Level.ALL);
            handler.setFormatter(formatter);

            Logger urlLogger = Logger.getLogger("houseUrl");
            FileHandler urlHandler = new FileHandler("./houseUrl.log",true);
            urlLogger.addHandler(handler);
            urlLogger.setLevel(Level.ALL);
            urlHandler.setFormatter(formatter);

        }
        catch(Exception e){}
    }
}
