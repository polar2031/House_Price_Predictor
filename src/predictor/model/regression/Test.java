package predictor.model.regression;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double[][] a = {{1,1},{1,2},{1,7}};
		double[] b = {3,5,15};
		double[] r;
		LinearRegression l = new LinearRegression();
		r = l.solve(a, b);
		for(int i = 0; i < r.length; i++){
			System.err.println(r[i]);
		}
	}

}
