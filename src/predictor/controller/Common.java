package predictor.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import predictor.gui.MainGui;
import predictor.model.VariableOption;
import predictor.model.data.House;

public class Common {
	public static void showHouseInfo(House h, MainGui g){
        try{
//            DecimalFormat formatter = new DecimalFormat("0.00");
//            g.setOutputFloorSize(formatter.format(h.floorSize));
//            g.setOutputLotSize(formatter.format(h.lotSize));
//            formatter.applyPattern("0.0");
//            g.setOutputBedroom(formatter.format(h.bedroomNumber));
//            g.setOutputBathroom(formatter.format(h.bathroomNumber));
//            formatter.applyPattern("0");
//            g.setOutputYearBuilt(formatter.format(h.builtYear));
//            if(h.lastSoldDate != null){
//                SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy");
//                g.setOutputLastSoldDate(sdFormatter.format(h.lastSoldDate));
//                formatter.applyPattern("0.00");
//                g.setOutputLastSoldPrice(formatter.format(h.lastSoldPrice));
//            }
//            
//            g.setOutputPredictPrice(formatter.format(h.predictPrice));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
	
    public static void showSampleList(List<House> houseList, MainGui g){
//        for(int i = 0; i < houseList.size(); i++){
//    		g.sampleList.setText(g.sampleList.getText() + houseList.get(i).address + "\n");
//        }
    }

	public static boolean[] getVariableOptions(MainGui g) {
		boolean[] o = new boolean[VariableOption.maxVariable];
		o[VariableOption.floorSize] = g.getChckbxFloorSize().isSelected();
		o[VariableOption.lotSize] = g.getChckbxLotSize().isSelected();
		o[VariableOption.bedroomNumber] = g.getChckbxBedroomNumber().isSelected();
		o[VariableOption.bathroomNumber] = g.getChckbxBathroomNumber().isSelected();
		o[VariableOption.age] = g.getChckbxHouseAge().isSelected();
		o[VariableOption.basePrice] = g.getChckbxUseBasePrice().isSelected();
		return o;
	}
	public static boolean[] getVariableTransferOptions(MainGui g) {
		boolean[] o = new boolean[VariableOption.maxVariable];
		for(int i = 0; i < o.length; i++){
			o[i] = false;
		}
		o[VariableOption.floorSize] = g.getChckbxTransferFloorSize().isSelected();
		o[VariableOption.lotSize] = g.getChckbxTransferLotSize().isSelected();
		return o;
	}
	public static boolean getValueTransferOptions(MainGui g) {
		return g.getChckbxTransferPrice().isSelected();
	}

	public static int getMethod(MainGui g) {
		return g.getComboBoxMethodIndex();
	}
}
