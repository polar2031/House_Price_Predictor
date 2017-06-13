package predictor.model.regression;

import java.util.Calendar;
import predictor.model.data.House;

public class Variable {
	
	boolean basePriceFlag;
	boolean floorSizeFlag;
	boolean lotSizeFlag;
	boolean bedroomNumberFlag;
	boolean bathroomNumberFlag;
	boolean ageFlag;
	
	public Variable(){
		basePriceFlag = false;
		floorSizeFlag = true;
		lotSizeFlag = true;
		bedroomNumberFlag = false;
		bathroomNumberFlag = false;
		ageFlag = false;
	}
	
	public void setBasePriceFlag(boolean b){
		basePriceFlag = b;
	}
	public void setBedroomNumberFlag(boolean b){
		bedroomNumberFlag = b;
	}
	public void setBathroomNumberFlag(boolean b){
		bathroomNumberFlag = b;
	}
	public void setAgeFlag(boolean b){
		ageFlag = b;
	}
	
	public int variableNumber(){
		int v = 0;
		if(floorSizeFlag == true){
			v++;
		}
		if(lotSizeFlag == true){
			v++;
		}
		if(basePriceFlag == true){
			v++;
		}
		if(bedroomNumberFlag == true){
			v++;
		}
		if(bathroomNumberFlag == true){
			v++;
		}
		if(ageFlag == true){
			v++;
		}
		return v;
	}
	
	public double[] variableTransform(House target) {
		int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        double[] a = new double[variableNumber()];
        int v = 0;

        a[v++] = target.floorSize;
        a[v++] = target.lotSize;
        if(basePriceFlag == true){
        	a[v++] = 1;
        }
        if(bedroomNumberFlag == true){
        	a[v++] = target.bedroomNumber;
        }
        if(bathroomNumberFlag == true){
        	a[v++] = target.bathroomNumber;
        }
        if(ageFlag == true){
        	a[v++] = (double)(- thisYear + target.builtYear);
        }

		return a;
	}
}
