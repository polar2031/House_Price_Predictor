package predictor.model.regression;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

public class LinearRegression {
	public LinearRegression() {	
	}
	
	public static double[] solve(double[][] a, double[] b) {
		try{
			double[] r = new double[a[0].length];
	        RealMatrix coefficients = new Array2DRowRealMatrix(a, false);
	        DecompositionSolver solver = new SingularValueDecomposition(coefficients).getSolver();
	        RealMatrix constants = new Array2DRowRealMatrix(b);
	        RealMatrix solution = solver.solve(constants);
	        for(int i = 0; i < solution.getRowDimension(); i++){
	            r[i] = solution.getData()[i][0];
	        }
	        return r;
		}
		catch(Exception e){
			return null;
		}
	}
	
}
