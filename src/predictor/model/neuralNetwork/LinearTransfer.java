package predictor.model.neuralNetwork;

public class LinearTransfer implements TransferFunction {

	@Override
	public double execute(double x) {
		return x;
	}

	@Override
	public double derive(double x) {
		return 1.0;
	}

}
