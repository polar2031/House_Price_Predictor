import java.io.*;

public class HousePricePredictor{
    public static void main(String[] args) throws Exception {
        if(args.length > 0 && args[0].equals("test")){
            // Tester t = new Tester();
            // t.start();
        }
        else{
            Model m = new Model();
            GUI g = new GUI();
            Controller c = new Controller(m, g);
            g.show();
        }
    }
}
