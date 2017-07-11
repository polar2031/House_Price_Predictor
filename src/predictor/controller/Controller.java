package predictor.controller;

import javax.swing.SwingWorker;

import predictor.gui.MainGui;
import predictor.controller.listener.StartListener;
import predictor.controller.listener.CancelListener;
import predictor.controller.listener.RestartListener;

public class Controller {
    public MainGui g;
    public SwingWorker<?, ?> task;

    public Controller(MainGui g) {
        this.g = g;
        g.addStartListener(new StartListener(this));
        g.addCancelListener(new CancelListener(this));
        g.addRestartListener(new RestartListener(this));
    }
    public void start() {
		try {
			g.frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
