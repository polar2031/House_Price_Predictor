package predictor.model.neuralNetwork;

public interface TransferFunction {
	double execute(double x);
	double derive(double x);
}
