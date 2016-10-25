import java.io.*;
import java.lang.Math.*;
import java.util.Scanner;

public class Price_predictor
{
	private House target;
	private House[] sample;

	public static void main(String[] args){
		Price_predictor p = new Price_predictor();
		//get house which needs to be predicted

		//get house data from terminal
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter address: ");
		String a = scanner.next();
		System.out.println("Please enter lot size (sqft): ");
		int l = scanner.nextInt();
		System.out.println("Please enter number of bedrooms: ");
		int bed = scanner.nextInt();
		System.out.println("Please enter number of bathrooms: ");
		int bath = scanner.nextInt();
		//set target from info above
		p.target = new House(a, l, bed, bath);


		p.countBasePrice();
		int predictPrice = p.predict();
		System.out.println(predictPrice);
	}

	public void countBasePrice(){
		int sampleNumber = 10;
		sample = new House[sampleNumber];
		//read data

		//testing code start
		try{
			FileReader f = new FileReader("houseData");
		 	BufferedReader br = new BufferedReader(f);

			String line = null;
			String[] tempTrainData = null;

			for(int i = 0; i < sampleNumber; i++){
				sample[i] = new House();
				line = br.readLine();
				tempTrainData = line.split(" ");
				//sample[i].setBedNumber(Integer.parseInt(tempTrainData[0]));
				sample[i].setBedNumber(3);
				// sample[i].setBathNumber(Integer.parseInt(tempTrainData[1]));
				// sample[i].setLot(Integer.parseInt(tempTrainData[2]));
				// sample[i].setSalePrice(Integer.parseInt(tempTrainData[3]));
			}
		}
		catch (IOException e) {System.out.println(e);}
		//testing code end

		//count base price
		//temp method: average
		int pricePerLot = 0;
		for(int i = 0; i < sampleNumber; i++){
			pricePerLot += sample[i].getSalePrice() / sample[i].getLot();
		}
		pricePerLot /= sampleNumber;
		target.setBasePrice(pricePerLot * target.getLot());

		return;
	}
	public int predict(){
		//float yearRate = (float)Math.pow(0.98f, (2016 - target.year));
		//float bedAdjust = target.bedNumber * bedRate;
		//float bathAdjust = target.bathNumber * bathRate;
		//target.predictPrice = target.getBasePrice() * yearRate;

		//return target.getBasePrice();
		return 0;
	}
}
