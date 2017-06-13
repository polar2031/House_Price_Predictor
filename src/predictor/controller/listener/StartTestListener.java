package predictor.controller.listener;

import predictor.controller.Controller;
import predictor.controller.task.ListPredictionTask;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartTestListener implements ActionListener {
	public Controller c;
	
	public StartTestListener(Controller c){
		super();
		this.c = c;
	}
	
	@Override
    public void actionPerformed(ActionEvent event) {
        c.g.startTestButton.setEnabled(false);
        c.g.showProcessingCard();
        c.task = new ListPredictionTask(c.g);
        c.task.execute();
        c.g.cancelButton.setEnabled(true);
    }

}