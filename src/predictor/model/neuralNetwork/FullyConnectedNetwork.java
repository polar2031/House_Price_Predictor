package predictor.model.neuralNetwork;

import java.util.Random;

import org.la4j.Matrix;

public class FullyConnectedNetwork {
	private static int featureCount = 5;
	private Layer[] layers = {
			new Layer(featureCount, 0, 0, new LinearTransfer()), 
			new Layer(10, 0.01, .01, new SigmoidTransfer()), 
			new Layer(10, 0.01, 0.9, new SigmoidTransfer()),  
			new Layer(1, 0, 0, new LinearTransfer())
		};
	
	public FullyConnectedNetwork(){

	}
	
	//train the network
	public void train(Matrix input, Matrix output, int LoopTimes){
		//set initial random weight
		Random r = new Random();
		for(int i = 0; i < layers.length; i++){
			
		}
		
		//start training
		for(int loop = 0; loop < LoopTimes; loop++){
			//forward
			for(Layer l : layers){
				
			}
		}
	}
	
	//test the network
	public void test(){
		
	}
	
	
	
}
