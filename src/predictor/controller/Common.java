package predictor.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import predictor.gui.GUI;
import predictor.model.data.House;

public class Common {
	public static void showHouseInfo(House h, GUI g){
        try{
            DecimalFormat formatter = new DecimalFormat("0.00");
            g.targetHouseInfo[0].setText(formatter.format(h.floorSize));
            g.targetHouseInfo[1].setText(formatter.format(h.lotSize));
            formatter.applyPattern("0.0");
            g.targetHouseInfo[2].setText(formatter.format(h.bedroomNumber));
            g.targetHouseInfo[3].setText(formatter.format(h.bathroomNumber));
            formatter.applyPattern("0");
            g.targetHouseInfo[4].setText(formatter.format(h.builtYear));
            if(h.lastSoldDate != null){
                SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy");
                g.targetHouseInfo[5].setText(sdFormatter.format(h.lastSoldDate));
                formatter.applyPattern("0.00");
                g.targetHouseInfo[6].setText(formatter.format(h.lastSoldPrice));
            }
            g.targetHouseInfo[7].setText(formatter.format(h.predictPrice));
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
	
    public static void showSampleList(List<House> houseList, GUI g){
        for(int i = 0; i < houseList.size(); i++){
            g.sampleList.setText(g.sampleList.getText() + houseList.get(i).address + "\n");
        }
    }
    
}
