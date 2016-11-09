import java.io.*;
import java.lang.Math.*;

public class Price_predictor
{
	private House target;
	private House[] samples;

	public static void main(String[] args){
		Price_predictor p = new Price_predictor();
		//set target data
		p.target = new House();
		p.target.setByTerminal();
		p.target.lat = 42.1204139;
		p.target.lng = -71.16507799999999;

		//get sample house data
		p.getDatabaseSample();

		//count base price using sample data
		p.countBasePrice();
		p.predict();
		System.out.println("Base Price: " + p.target.getBasePrice());
		System.out.println("Predict Price: " + p.target.getPredictPrice());
	}

	//get sample houses from database
	public void getDatabaseSample(){
		int sampleNumber = 10;
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
				samples[i] = new House();
				line = br.readLine();
				tempTrainData = line.split(" ");
				samples[i].setBedNumber(Integer.parseInt(tempTrainData[0]));
				samples[i].setBathNumber(Integer.parseInt(tempTrainData[1]));
				samples[i].setFloorSize(Integer.parseInt(tempTrainData[2]));
				samples[i].setSalePrice(Integer.parseInt(tempTrainData[3]));
				samples[i].lat = Double.parseDouble(tempTrainData[4]);
				samples[i].lng = Double.parseDouble(tempTrainData[5]);
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
		double xy = 0.0;
		double x = 0.0;
		double y = 0.0;
		double x2 = 0.0;
		int n = 0;
		for(int i = 0; i < samples.length; i++){
			//filter
			if((samples[i].getFloorSize() - target.getFloorSize()) < (target.getFloorSize() * 0.5) ||
			(samples[i].getFloorSize() - target.getFloorSize()) > (target.getFloorSize() * -0.5) ){
				xy += (double)samples[i].getSalePrice() * (double)samples[i].getFloorSize();
				x += (double)samples[i].getFloorSize();
				y += (double)samples[i].getSalePrice();
				x2 += (double)samples[i].getFloorSize() * (double)samples[i].getFloorSize();
				n += 1;
			}
		}
		double b1 = (xy - (x * y / n)) / (x2 - x * x / n);
		double b0 = (y - b1 * x) / n;
		target.setBasePrice((int)(b0 + b1 * target.getFloorSize()));
	}

	public void predict(){
		int bedRate = 500;
		int bathRate = 500;
		int bedAdjust = target.bedNumber * bedRate;
		int bathAdjust = target.bathNumber * bathRate;
		target.setPredictPrice(target.getBasePrice() + bedRate + bathRate);

	}
}
