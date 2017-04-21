package predictor.controller.listener;

import predictor.controller.Controller;
import predictor.gui.GUI;
import predictor.model.Model;
import predictor.controller.task.PredictionTask;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartListener implements ActionListener {
	public Controller c;
	public Model m;
	public GUI g;
	
	public StartListener(Controller c){
		super();
		this.c = c;
		this.m = c.m;
		this.g = c.g;
	}
	
	@Override
    public void actionPerformed(ActionEvent event) {
        c.g.startButton.setEnabled(false);
        c.g.clean();
        c.task = new PredictionTask(m, g);
        c.task.execute();
        c.g.cancelButton.setEnabled(true);
    }
	
	

	

}