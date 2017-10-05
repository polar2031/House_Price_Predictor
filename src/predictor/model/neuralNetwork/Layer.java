package predictor.model.neuralNetwork;

import predictor.model.neuralNetwork.TransferFunction;

public class Layer {
	private int neuronCount;
	private double learningRate;
	private double momentum;
	private TransferFunction transferFunction;
	
	
	public Layer(int neuronCount, double learningRate, double momentum, TransferFunction transferFunction){
		this.neuronCount = neuronCount;
		this.learningRate = learningRate;
		this.momentum = momentum;
		this.transferFunction = transferFunction;
	}
}
