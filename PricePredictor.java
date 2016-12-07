import java.io.*;
import java.lang.Math.*;
import org.apache.commons.math3.linear.*;

public class PricePredictor
{
	private House target;
	private House[] samples;

	public PricePredictor(House h){
		//set target data
		target = h;

		//get sample house data
		getDatabaseSample();

		//count base price using sample data
		countBasePrice();
		System.out.println("Base Price: " + target.getBasePrice());
		// System.out.println("Predict Price: " + target.getPredictPrice());
	}

	public PricePredictor(){
		//set target data
		target = new House();
		target.setByTerminal();

		//get sample house data
		getDatabaseSample();

		//count base price using sample data
		countBasePrice();
		predict();
		System.out.println("Base Price: " + target.getBasePrice());
		System.out.println("Predict Price: " + target.getPredictPrice());
	}

	//get sample houses from database
	public void getDatabaseSample(){
		int sampleNumber = 480;
		samples = new House[sampleNumber];
		//read data
		try{
			FileReader f = new FileReader("houseData");
			BufferedReader br = new BufferedReader(f);
			String line = null;
			String[] tempTrainData = null;

			for(int i = 0; i < sampleNumber; i++){
				//filter start

				//filter end
				// samples[i] = new House();
				// line = br.readLine();
				// tempTrainData = line.split(" ");
				// samples[i].setBedNumber(Integer.parseInt(tempTrainData[0]));
				// samples[i].setBathNumber(Integer.parseInt(tempTrainData[1]));
				// samples[i].setfloorSize(Integer.parseInt(tempTrainData[2]));
				// samples[i].setSalePrice(Integer.parseInt(tempTrainData[3]));
				// samples[i].lat = Double.parseDouble(tempTrainData[4]);
				// samples[i].lng = Double.parseDouble(tempTrainData[5]);
				String address;
				String city;
				String state;
				String zipcode;
				int floorSize;
				int bedNumber;
				int bathNumber;
				int salePrice;
				state = br.readLine();
				city = br.readLine();
				zipcode = br.readLine();
				address = br.readLine();
				bedNumber = Integer.parseInt(br.readLine());
				bathNumber = Integer.parseInt(br.readLine());
				floorSize = Integer.parseInt(br.readLine());
				salePrice = Integer.parseInt(br.readLine());
				br.readLine();
				br.readLine();
				br.readLine();
				samples[i] = new House(address,
										city,
										state,
										zipcode,
										floorSize,
										bedNumber,
										bathNumber,
										salePrice);
			}
		}
		catch (IOException e) {System.out.println(e);}
	}

	//get sample houses data from Zillow
	public void getZillowSample(){
		ZillowParser z = new ZillowParser();

	}

	//count base price
	public void countBasePrice(){
		//temp method: average
		//double pricePerLot = 0;
		// for(int i = 0; i < samples.length; i++){
		// 	pricePerLot += samples[i].getSalePrice() / samples[i].getLot();
		// }
		// pricePerLot /= samples.length;
		// target.setBasePrice(pricePerLot * target.getLot());

		//method: add weight based on distance
		// double totalWeight = 0.0;
		// for(int i = 0; i < samples.length; i++){
		// 	//get distance
		// 	double weight = 1.0 /b;
		// 	//to prevent target itself
		// 	if(weight != 0.0){
		// 		pricePerLot += (double)samples[i].getSalePrice() / (double)samples[i].getLot() * weight;
		// 		totalWeight += weight;
		// 	}
		// }
		// pricePerLot /= totalWeight;
		// target.setBasePrice((int)pricePerLot * target.getLot());

		//method: Linear least squares
		//price X sqft
		// double xy = 0.0;
		// double x = 0.0;
		// double y = 0.0;
		// double x2 = 0.0;
		// int n = 0;
		// for(int i = 0; i < samples.length; i++){
		// 	//filter
		// 	if((samples[i].getfloorSize() - target.getfloorSize()) < (target.getfloorSize() * 0.5) ||
		// 	(samples[i].getfloorSize() - target.getfloorSize()) > (target.getfloorSize() * -0.5) ){
		// 		xy += (double)samples[i].getSalePrice() * (double)samples[i].getfloorSize();
		// 		x += (double)samples[i].getfloorSize();
		// 		y += (double)samples[i].getSalePrice();
		// 		x2 += (double)samples[i].getfloorSize() * (double)samples[i].getfloorSize();
		// 		n += 1;
		// 	}
		// }
		// double b1 = (xy - (x * y / n)) / (x2 - x * x / n);
		// double b0 = (y - b1 * x) / n;
		// target.setBasePrice((int)(b0 + b1 * target.getfloorSize()));

		//counting sample houses' weight
		int totalWeight = 0;
		for(int i = 0; i < samples.length; i++){
			//weight based on distance
			double distance = samples[i].getDirectDistance(target);
			if(distance < 1.0){
				samples[i].weight += 4;
			}
			else if(distance < 2.0){
				samples[i].weight += 3;
			}
			else if(distance < 3.0){
				samples[i].weight += 2;
			}
			else if(distance < 4.0){
				samples[i].weight += 1;
			}
			//weight based on floor size
			double sizeDiffer = Math.abs(target.floorSize - samples[i].floorSize) / target.floorSize;
			if(sizeDiffer < 0.25){
				samples[i].weight *= 4;
			}
			else if(sizeDiffer < 0.5){
				samples[i].weight *= 2;
			}
			else if(sizeDiffer < 0.75){}
			else{
				samples[i].weight = 0;
			}
			totalWeight += samples[i].weight;
		}

		//price x floor size x bed x bath
		int vNumber = 3;
		double[][] a = new double[totalWeight][vNumber];//
		double[] b = new double[totalWeight];//price
		int c = 0;
		for(int i = 0; i < samples.length; i++){
			for(int j = 0; j < samples[i].weight; j++){
				a[c][0] = (double)samples[i].getfloorSize();
				a[c][1] = (double)samples[i].getBedNumber();
				a[c][2] = (double)samples[i].getBathNumber();
				b[c] = (double)samples[i].getSalePrice();
				c++;
			}
		}

		RealMatrix coefficients = new Array2DRowRealMatrix(a, false);
		DecompositionSolver solver = new QRDecomposition(coefficients).getSolver();
		RealMatrix constants = new Array2DRowRealMatrix(b);
		RealMatrix solution = solver.solve(constants);
		double[][] test = {{target.getfloorSize(), target.getBedNumber(), target.getBathNumber()}};
		RealMatrix tMatrix = new Array2DRowRealMatrix(test);
		// System.out.println(tMatrix.getRowDimension());
		// System.out.println(tMatrix.getColumnDimension());
		// System.out.println(solution.getRowDimension());
		// System.out.println(solution.getColumnDimension());

		RealMatrix p = tMatrix.multiply(solution);
		// System.out.println(p.getRowDimension());
		// System.out.println(p.getColumnDimension());
		target.setBasePrice((int)p.getEntry(0, 0));
	}

	public void predict(){
		// int bedRate = 500;
		// int bathRate = 500;
		// int bedAdjust = target.bedNumber * bedRate;
		// int bathAdjust = target.bathNumber * bathRate;
		// target.setPredictPrice(target.getBasePrice() + bedRate + bathRate);

	}
}
