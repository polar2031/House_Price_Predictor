package predictor.model.regression;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.linear.SingularValueDecomposition;

public class LeastSquare {
	
    private RealMatrix solution;
    private boolean solutionExist;
    
	public LeastSquare(double[][] a, double[] b){
		try{
	        RealMatrix coefficients = new Array2DRowRealMatrix(a, false);
	        DecompositionSolver solver = new SingularValueDecomposition(coefficients).getSolver();
	        RealMatrix constants = new Array2DRowRealMatrix(b);
	        solution = solver.solve(constants);
	        solutionExist = true;
		}
		catch(SingularMatrixException e){
			solutionExist = false;
		}
	}
	
	public double solve(double[][] test){
        RealMatrix tMatrix = new Array2DRowRealMatrix(test);
        RealMatrix p = tMatrix.multiply(solution);
        return p.getEntry(0, 0);
	}
	
	public double[] solveAll(double[][] test){
		double[] result = new double[test.length];
        RealMatrix tMatrix = new Array2DRowRealMatrix(test);
        RealMatrix p = tMatrix.multiply(solution);
        for(int i = 0; i < test.length; i++){
        	result[i] = p.getEntry(i, 0);
        }
        return result;
	}
	
	public double rss(double[][] a, double[] b, double lamda){
		double rss = 0;
		RealMatrix tMatrix = new Array2DRowRealMatrix(a);
        RealMatrix p = tMatrix.multiply(solution);
        
		for(int i = 0; i < a.length; i++){
			double yLamda = p.getEntry(i, 0);
			rss += Math.abs(Math.pow(yLamda, 1.0 / lamda) - Math.pow(b[i], 1.0 / lamda));
		}
		rss /= a.length;
//		System.err.println("rss: " + rss);
		return rss;
	}
	
	public void showSolution(){
        // show solution
        double[][] o = solution.getData();
        String value[] = {"floor size", "lot size", "base price", "bedroom", "bathroom", "age", "summer", "fall", "winter"};
        System.err.println("Variables");
        for(int i = 0; i < solution.getRowDimension(); i++){
            System.err.print(value[i] + ": ");
            for(int j = 0; j < solution.getColumnDimension(); j++){
                System.err.print(o[i][j]);
            }
            System.err.println("");
        }
	}
	
	public boolean isSolutionExist(){
		return solutionExist;
	}


}
