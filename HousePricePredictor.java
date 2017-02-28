import java.io.*;
import java.util.logging.Logger;

public class HousePricePredictor{
    public static void main(String[] args) throws Exception {
        if(args.length > 0 && args[0].equals("test")){
            Tester t = new Tester();
            t.start();
        }
        else{
            GUI g = new GUI();
        }
    }
}
