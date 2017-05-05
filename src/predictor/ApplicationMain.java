package predictor;

import predictor.gui.GUI;
import predictor.model.Model;
import predictor.model.parser.ZillowParser;
import predictor.controller.Controller;

public class ApplicationMain {

	public static void main(String[] args) {
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.SEVERE); 
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		try {
			ZillowParser.parseHouseDetailPage("http://www.zillow.com/homes/18-Sandy-Ridge-Cir-Sharon-MA-02067_rb/");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			Model m = new Model();
	        GUI g = new GUI();
	        Controller c = new Controller(m, g);
	        c.start();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
