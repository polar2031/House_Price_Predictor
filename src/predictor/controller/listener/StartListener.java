package predictor.controller.listener;

import predictor.controller.Controller;
import predictor.controller.task.AreaPredictionTask;
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
        c.g.showProcessingCard();
        if(c.g.getSelectedMode() == 0){
        	c.task = new PredictionTask(c.g);
        	c.task.execute();
        }
        else if(c.g.getSelectedMode() == 1){
        	c.task = new AreaPredictionTask(c.g);
        	c.task.execute();
        }
    }
}