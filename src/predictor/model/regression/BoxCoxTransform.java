package predictor.model.regression;

public class BoxCoxTransform {
	public static double[] transform(double[] a, double lamda){
		double[] b = new double[a.length];
		b = a.clone();
		for(int i = 0; i < a.length; i++){
			b[i] = Math.pow(a[i],  lamda);
		}
		return b;
	}
	public static double[][] transform(double[][] a, double lamda, int position){
		double[][] b = new double[a.length][a[0].length];
		for(int i = 0; i < a.length; i++){
			b[i] = a[i].clone();
			b[i][position] = Math.pow(a[i][position],  lamda);
		}
		return b;
	}
	public static double[] reduce(double[] a, double lamda) {
		double[] b = new double[a.length];
		for(int i = 0; i < a.length; i++){
			b[i] = Math.pow(a[i],  1.0 / lamda);
		}
		return b;
	}
}
