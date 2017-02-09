import java.io.*;
import java.util.*;
import java.lang.Math.*;
import org.apache.commons.math3.linear.*;
import java.nio.file.Files;

public class PricePredictor{
    ArrayList<House> samples;

    public PricePredictor(){
    }

    public boolean start(House h){
        return this.start(h, "leastSquare");
    }
    public boolean start(House h, String predictMethod){

        /////////////////////////////////////////
        //get target house info from Zillow.com//
        /////////////////////////////////////////
        if(h.getInfoFromZillow() == false){
            // no house info on the internet
            System.out.println("House info does not exist on Zillow.com.");
            // System.out.println("Please enter house info manually.")
            return false;
        }
        else{
            System.out.println("House info found on Zillow.com.");
            System.err.println("Floor Size: " + h.floorSize);
            System.err.println("Lot Size: " + h.lotSize);
            System.err.println("Bedroom Number" + h.bedroomNumber);
            System.err.println("Bathroom Number: " + h.bathroomNumber);
        }

        ///////////////////////////////
        //update local data if needed//
        ///////////////////////////////
        boolean updateRequirement = true;
        if(updateRequirement){
            ZillowParser z = new ZillowParser();
            z.parseNearbyHouses(h);
        }

        /////////////////////////////////////
        //read data of recently sold houses//
        /////////////////////////////////////
        if(!readSampleFromAllData(h)){
            System.err.println("file_access_error: read sample data");
            return false;
        }
        removeSampleData();

        /////////////////////
        //weight adjustment//
        /////////////////////
        int totalWeight = weightAdjustment(h);
        System.err.println("weight: " + totalWeight);


        ////////////////////////////////////////////
        //use selected method to do the prediction//
        ////////////////////////////////////////////
        boolean re = false;
        switch(predictMethod){
            //default: same city
            case "default":
            case "leastSquare":
                re = leastSquare(h, totalWeight);
                break;
        }
        System.err.println(re);
        System.err.println("Predict Price: " + h.predictBasePrice);
        System.err.println("Last Sold Price: " + h.lastSoldPrice);
        System.out.println("Predict Price: " + h.predictBasePrice);
        System.out.println("Last Sold Price: " + h.lastSoldPrice);
        return re;
    }
    private boolean leastSquare(House h, int totalWeight){

        double thisYear = 2017;
        //number of features
        int vNumber = 4;
        //features
        double[][] a = new double[totalWeight][vNumber];
        //price
        double[] b = new double[totalWeight];
        int c = 0;
        for(int i = 0; i < samples.size(); i++){
            for(int j = 0; j < samples.get(i).weight; j++){
                a[c][0] = (double)samples.get(i).floorSize;
                a[c][1] = (double)samples.get(i).lotSize;
                a[c][2] = (double)samples.get(i).bedroomNumber;
                a[c][3] = (double)samples.get(i).bathroomNumber;
                b[c] = (double)samples.get(i).lastSoldPrice;
                c++;
            }
        }
        try{
            RealMatrix coefficients = new Array2DRowRealMatrix(a, false);

            // DecompositionSolver solver = new QRDecomposition(coefficients).getSolver();
            DecompositionSolver solver = new SingularValueDecomposition(coefficients).getSolver();
            RealMatrix constants = new Array2DRowRealMatrix(b);
            //
            RealMatrix solution = solver.solve(constants);
            //
            double[][] test = {{h.floorSize, h.lotSize, h.bedroomNumber, h.bathroomNumber}};
            RealMatrix tMatrix = new Array2DRowRealMatrix(test);

            // System.out.println(tMatrix.getRowDimension());
            // System.out.println(tMatrix.getColumnDimension());
            // System.out.println(solution.getRowDimension());
            // System.out.println(solution.getColumnDimension());

            RealMatrix p = tMatrix.multiply(solution);
            // System.out.println(p.getRowDimension());
            // System.out.println(p.getColumnDimension());
            h.predictBasePrice = p.getEntry(0, 0);
        }
        catch(Exception e){
            return false;
        }
        return true;
    }
    private boolean readSampleFromAllData(House h){
        try{
            File folder = new File("./temp/");
            File files[] = folder.listFiles();
            samples = new ArrayList<House>();
            for(int i = 0; i < files.length; i++){
                // System.err.println(files[i].toString());
                FileReader f = new FileReader(files[i].toString());
                House temp = new House();
                if(temp.readFromFile(f)){
                    samples.add(temp);
                    // System.err.println(samples.get(i).address);
                    temp = new House();
                }
                f.close();
            }
        }
        catch(IOException ioe){
            return false;
        }
        return true;
    }

    private boolean removeSampleData(){
        try{
            File folder = new File("./temp/");
            File files[] = folder.listFiles();
            for(int i = 0; i < files.length; i++){
                // System.err.println(files[i].toString()
                files[i].delete();
            }
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

    private int weightAdjustment(House h){
        //counting sample houses' weight
        int totalWeight = 0;
        double pricePerSqft = 0.0;
        double totalSqft = 0.0;
        for(int i = 0; i < samples.size(); i++){
            pricePerSqft += samples.get(i).lastSoldPrice;
            totalSqft += samples.get(i).floorSize;
        }
        pricePerSqft = pricePerSqft / totalSqft;

        for(int i = 0; i < samples.size(); i++){
            //weight based on distance
            double distance = samples.get(i).getDirectDistance(h);
            if(distance < 1.0){
                samples.get(i).weight += 4;
            }
            else if(distance < 1.0){
                samples.get(i).weight += 3;
            }
            else if(distance < 1.5){
                samples.get(i).weight += 2;
            }
            else if(distance < 2.0){
                samples.get(i).weight += 1;
            }
            // weight based on floor size
            double sizeDiffer = Math.abs(h.floorSize - samples.get(i).floorSize) / h.floorSize;
            if(sizeDiffer < 0.1){
                samples.get(i).weight *= 8;
            }
            else if(sizeDiffer < 0.15){
                samples.get(i).weight *= 4;
            }
            else if(sizeDiffer < 0.2){}
            else{
                samples.get(i).weight = 0;
            }

            double pricePerSqftDiff = Math.abs(samples.get(i).lastSoldPrice / samples.get(i).floorSize - pricePerSqft) / pricePerSqft;
            if(pricePerSqftDiff > 0.5){
                samples.get(i).weight = 0;
            }

            totalWeight += samples.get(i).weight;
        }
        return totalWeight;
    }
}
