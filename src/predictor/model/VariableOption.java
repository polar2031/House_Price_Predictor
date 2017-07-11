package predictor.model;

import java.util.Calendar;
import predictor.model.data.House;

public class VariableOption {
	public static final int floorSize = 0;
	public static final int lotSize = 1;
	public static final int bedroomNumber = 2;
	public static final int bathroomNumber = 3;
	public static final int age = 4;
	public static final int basePrice = 5;
	public static final int maxVariable = 6;
	public static final String[] variableName = {"Floor Size", "Lot Size", "Bedroom Number", "Bathroom Number", "Age"};
	
	boolean[] variableFlag;
	boolean[] variableTransferFlag;
	
	boolean[] usedVariableTransferFlag;
	double[] usedVariableLamda;
	
	int totalVariable;
	
	public VariableOption(){
		variableFlag = new boolean[maxVariable];
		variableTransferFlag = new boolean[maxVariable];
		for(int i = 0; i < variableFlag.length; i++){
			variableFlag[i] = false;
			variableTransferFlag[i] = false;
		}
	}
	
	public void setFloorSizeFlag(boolean b){
		variableFlag[floorSize] = b;
	}
	public void setLotSizeFlag(boolean b){
		variableFlag[lotSize] = b;
	}
	public void setBedroomNumberFlag(boolean b){
		variableFlag[bedroomNumber] = b;
	}
	public void setBathroomNumberFlag(boolean b){
		variableFlag[bathroomNumber] = b;
	}
	public void setAgeFlag(boolean b){
		variableFlag[age] = b;
	}
	public void setBasePriceFlag(boolean b){
		variableFlag[basePrice] = b;
	}
	
	public void setTransferFlag(boolean[] bArray){
		for(int i = 0; i < variableTransferFlag.length; i++){
			variableTransferFlag[i] = bArray[i];
		}
	}
	
	public int variableNumber(){
		int n = 0;
		for(int i = 0; i < variableFlag.length; i++){
			if(variableFlag[i] == true){
				n += 1;
			}
		}
		return n;
	}
	
	public void setUsedVariableTransferFlag(){
		usedVariableTransferFlag = new boolean[variableNumber()];
		usedVariableLamda = new double[variableNumber()];

		int v = 0;
        if(variableFlag[floorSize]){
        	usedVariableTransferFlag[v++] = variableTransferFlag[floorSize];
        }
        if(variableFlag[lotSize]){
        	usedVariableTransferFlag[v++] = variableTransferFlag[lotSize];
        }

        if(variableFlag[bedroomNumber]){
        	usedVariableTransferFlag[v++] = variableTransferFlag[bedroomNumber];
        }
        if(variableFlag[bathroomNumber]){
        	usedVariableTransferFlag[v++] = variableTransferFlag[bathroomNumber];
        }
        if(variableFlag[age]){
        	usedVariableTransferFlag[v++] = variableTransferFlag[age];
        }
        if(variableFlag[basePrice]){
        	usedVariableTransferFlag[v++] = variableTransferFlag[basePrice];
        }
        
        return;
	}
	
	
	public double[] variableTransform(House target) {
		int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        double[] a = new double[variableNumber()];
        int v = 0;

        if(variableFlag[floorSize]){
        	a[v++] = target.floorSize;
        }
        if(variableFlag[lotSize]){
        	a[v++] = target.lotSize;
        }

        if(variableFlag[bedroomNumber]){
        	a[v++] = target.bedroomNumber;
        }
        if(variableFlag[bathroomNumber]){
        	a[v++] = target.bathroomNumber;
        }
        if(variableFlag[age]){
        	a[v++] = (double)(- thisYear + target.builtYear);
        }
        if(variableFlag[basePrice]){
        	a[v++] = 1;
        }

		return a;
	}
	
}
