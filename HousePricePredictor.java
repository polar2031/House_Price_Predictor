import java.io.*;

public class HousePricePredictor{
    public static void main(String[] args) throws IOException {
        if(args.length > 0 && args[0].equals("test")){
            System.out.println("Enter testing mode.");
        }
        else{
            GUI g = new GUI();
        }
    }
}
