package predictor;

import predictor.gui.mainGui;
import predictor.controller.Controller;

public class ApplicationMain {

	public static void main(String[] args) {
		
		try{
			// stop logger from parser
			java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.SEVERE); 
			System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
			
			mainGui g = new mainGui();
	        Controller c = new Controller(g);
	        c.start();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
