package predictor;

import predictor.gui.GUI;
import predictor.model.Model;
import predictor.controller.Controller;

public class ApplicationMain {

	public static void main(String[] args) {
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.SEVERE); 
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		
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
