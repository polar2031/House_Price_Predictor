package predictor.controller.listener;

import predictor.controller.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CancelListener implements ActionListener {
	public Controller c;
	
	public CancelListener(Controller c){
		super();
		this.c = c;
	}
	
	@Override
    public void actionPerformed(ActionEvent event) {
		c.task.cancel(true);
		c.g.showInputCard();
    }
}
