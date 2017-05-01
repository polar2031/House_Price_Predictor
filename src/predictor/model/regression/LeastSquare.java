package predictor.model.regression;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

public class LeastSquare {
	
    public RealMatrix solution;
    
	public LeastSquare(double[][] a, double[] b){
        RealMatrix coefficients = new Array2DRowRealMatrix(a, false);
        DecompositionSolver solver = new SingularValueDecomposition(coefficients).getSolver();
        RealMatrix constants = new Array2DRowRealMatrix(b);
        solution = solver.solve(constants);
	}
	
	public double solve(double[][] test){
        RealMatrix tMatrix = new Array2DRowRealMatrix(test);
        RealMatrix p = tMatrix.multiply(solution);
        return p.getEntry(0, 0);
	}
	
	public double rss(double[][] a, double[] b){
		double rss = 0;
		RealMatrix tMatrix = new Array2DRowRealMatrix(a);
        RealMatrix p = tMatrix.multiply(solution);
		for(int i = 0; i < a.length; i++){
	        rss += Math.pow(b[i] - p.getEntry(i, 0), 2);
		}
		rss /= a.length;
		System.err.println("rss: " + rss);
		return rss;
	}
	
	public void showSolution(){
        // show solution
        double[][] o = solution.getData();
        String value[] = {"base", "floor size", "lot size", "bedroom", "bathroom", "age", "summer", "fall", "winter"};
        System.err.println("Variables");
        for(int i = 0; i < solution.getRowDimension(); i++){
            System.err.print(value[i] + ": ");
            for(int j = 0; j < solution.getColumnDimension(); j++){
                System.err.print(o[i][j]);
            }
            System.err.println("");
        }
	}
}
