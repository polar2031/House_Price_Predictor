import java.util.*;
import java.lang.Math.*;
import org.apache.commons.math3.linear.*;

public class Model{
    public ArrayList<House> samples;
    public House target;

    public Model(){}
    public void reset(){
        samples = null;
        target = null;
    }
    public boolean searchHouse(String address, String city, String state, String zip){
        House h = new House(address, city, state, zip);
        if(h.getInfoFromZillow() == true){
            target = h;
            return true;
        }
        else{
            return false;
        }
    }
    public boolean getSamples(){
        ZillowParser z = new ZillowParser();
        ArrayList<House> hs = z.parseNearbyHouses(target);
        //remove target house from samples
        for(int i = 0; i < hs.size(); i++){
            if(hs.get(i).address.trim().equals(target.address.trim())){
                hs.remove(i);
                System.out.println("remove target from sample");
                break;
            }
        }
        if(hs.size() > 0){
            samples = hs;
            return true;
        }
        else{
            return false;
        }
    }

    public boolean adjustSampleWeight(){
        ////////////////////////////////////
        // parameter for weight adjustment//
        ////////////////////////////////////
        double distanceRange[] = {0.5, 1.0, 1.5, 2.0, Double.MAX_VALUE};
        int distanceWeight[] = {4, 3, 2, 1, 0};
        double sizeDifferenceRange[] = {0.1, 0.15, 0.2, Double.MAX_VALUE};
        int sizeDifferenceWeight[] = {8, 4, 2, 0};

        int totalWeight = 0;
        double pricePerSqft = 0.0;
        double totalSqft = 0.0;

        //initial default weight
        for(int i = 0; i < samples.size(); i++){
            samples.get(i).weight = 0;
        }

        //adjust weight based on distance
        for(int i = 0; i < samples.size(); i++){
            double distance = samples.get(i).getDirectDistance(target);
            for(int j = 0; j < distanceRange.length; j++){
                if(distance < distanceRange[j]){
                    samples.get(i).weight += distanceWeight[j];
                    break;
                }
            }
            if(samples.get(i).weight == 0){
                samples.remove(i);
            }
        }
        // adjust weight based on floor size
        for(int i = 0; i < samples.size(); i++){
            double sizeDiffer = Math.abs(target.floorSize - samples.get(i).floorSize) / target.floorSize;
            for(int j = 0; j < sizeDifferenceRange.length; j++){
                if(sizeDiffer < sizeDifferenceRange[j]){
                    samples.get(i).weight += sizeDifferenceWeight[j];
                    break;
                }
            }
            if(samples.get(i).weight == 0){
                samples.remove(i);
            }
        }

        //adjust weight based on price/sqft
        for(int i = 0; i < samples.size(); i++){
            if(samples.get(i).lastSoldPrice == null){
                samples.remove(i);
            }
            else{
                pricePerSqft += samples.get(i).lastSoldPrice;
                totalSqft += samples.get(i).floorSize;
            }
        }
        pricePerSqft = pricePerSqft / totalSqft;

        for(int i = 0; i < samples.size(); i++){
            double pricePerSqftDiff = Math.abs(samples.get(i).lastSoldPrice / samples.get(i).floorSize - pricePerSqft) / pricePerSqft;
            if(pricePerSqftDiff > 0.5){
                samples.remove(i);
            }
        }
        for(int i = 0; i < samples.size(); i++){
            totalWeight += samples.get(i).weight;
        }
        if(samples.size() > 0){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean predict(){
        return leastSquare();
    }
    public boolean leastSquare(){

        int thisYear = 2017;
        //number of features
        int vNumber = 9;
        //features
        double[][] a;
        //price
        double[] b;

        int totalWeight = 0;
        for(int i = 0; i < samples.size(); i++){
            totalWeight += samples.get(i).weight;
        }

        a = new double[totalWeight][vNumber];
        b = new double[totalWeight];

        int c = 0;
        for(int i = 0; i < samples.size(); i++){
            for(int j = 0; j < samples.get(i).weight; j++){
                a[c][0] = (double)samples.get(i).floorSize;
                a[c][1] = (double)samples.get(i).lotSize;
                a[c][2] = (double)samples.get(i).bedroomNumber;
                a[c][3] = (double)samples.get(i).bathroomNumber;
                a[c][4] = (double)(samples.get(i).builtYear - thisYear);
                switch(samples.get(i).lastSoldDate.getMonth()){
                    case 0:
                    case 1:
                    case 2:
                        a[c][5] = 1;
                        a[c][6] = 0;
                        a[c][7] = 0;
                        a[c][8] = 0;
                        break;
                    case 3:
                    case 4:
                    case 5:
                        a[c][5] = 0;
                        a[c][6] = 1;
                        a[c][7] = 0;
                        a[c][8] = 0;
                        break;
                    case 6:
                    case 7:
                    case 8:
                        a[c][5] = 0;
                        a[c][6] = 0;
                        a[c][7] = 1;
                        a[c][8] = 0;
                        break;
                    case 9:
                    case 10:
                    case 11:
                        a[c][5] = 0;
                        a[c][6] = 0;
                        a[c][7] = 0;
                        a[c][8] = 1;
                        break;
                    default:
                        a[c][5] = 0;
                        a[c][6] = 0;
                        a[c][7] = 0;
                        a[c][8] = 0;
                        break;
                }
                b[c] = (double)samples.get(i).lastSoldPrice;
                c++;
            }
        }
        try{
            RealMatrix coefficients = new Array2DRowRealMatrix(a, false);
            // DecompositionSolver solver = new QRDecomposition(coefficients).getSolver();
            DecompositionSolver solver = new SingularValueDecomposition(coefficients).getSolver();
            RealMatrix constants = new Array2DRowRealMatrix(b);
            RealMatrix solution = solver.solve(constants);
            double[][] test = {{target.floorSize,
                                target.lotSize,
                                target.bedroomNumber,
                                target.bathroomNumber,
                                (double)(target.builtYear-thisYear),
                                1,
                                0,
                                0,
                                0
                                }};
            RealMatrix tMatrix = new Array2DRowRealMatrix(test);

            // System.out.println(tMatrix.getRowDimension());
            // System.out.println(tMatrix.getColumnDimension());
            // System.out.println(solution.getRowDimension());
            // System.out.println(solution.getColumnDimension());

            RealMatrix p = tMatrix.multiply(solution);
            // System.out.println(p.getRowDimension());
            // System.out.println(p.getColumnDimension());
            target.predictBasePrice = p.getEntry(0, 0);
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

}
