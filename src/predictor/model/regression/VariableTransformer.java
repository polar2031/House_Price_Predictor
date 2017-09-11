package predictor.model.regression;

public class VariableTransformer {
	public VariableTransformer(){}
	
	public static double[] transform(double[] a, double[] transformer){
		try{
			double[] b = new double[a.length];
			for(int i = 0; i < a.length; i++){
				b[i] = Math.pow(a[i], transformer[i]);
			}
			return b;
		}
		catch(Exception e){
			return null;
		}
	}
	
}
