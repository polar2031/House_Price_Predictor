package predictor.model.neuralNetwork;

import org.apache.commons.math3.analysis.function.Sigmoid;

public class SigmoidTransfer implements TransferFunction {
	
	public static void main(String[] args) {
		SigmoidTransfer s = new SigmoidTransfer();
		System.out.println(s.execute(0));
	}
	
	
	@Override
	public double execute(double x) {
		Sigmoid s = new Sigmoid();
		return s.value(x);
	}

	@Override
	public double derive(double x) {
		return this.execute(x) * (1 - this.execute(x));
	}

}
