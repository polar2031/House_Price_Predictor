package predictor.controller.listener;

import predictor.controller.Controller;
import predictor.controller.task.PredictionTask;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartListener implements ActionListener {
	public Controller c;
	
	public StartListener(Controller c){
		super();
		this.c = c;
	}
	
	@Override
    public void actionPerformed(ActionEvent event) {
        c.g.startButton.setEnabled(false);
        c.g.clean();
        c.task = new PredictionTask(c.m, c.g);
        c.task.execute();
        c.g.cancelButton.setEnabled(true);
    }

}