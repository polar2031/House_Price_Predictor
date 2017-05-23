package predictor.controller.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import predictor.controller.Controller;

public class RestartListener implements ActionListener {
	public Controller c;
	
	public RestartListener(Controller c){
		super();
		this.c = c;
	}
	
	@Override
    public void actionPerformed(ActionEvent event) {
		c.g.showInputCard();
    }
}
