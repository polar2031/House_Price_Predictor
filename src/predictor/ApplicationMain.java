package predictor;

import predictor.gui.mainGui;
import predictor.model.Model;
import predictor.controller.Controller;

public class ApplicationMain {

	public static void main(String[] args) {
		// stop logger from parser
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.SEVERE); 
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

		try{
			Model m = new Model();
			mainGui g = new mainGui();
	        Controller c = new Controller(m, g);
	        c.start();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
