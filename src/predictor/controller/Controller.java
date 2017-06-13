package predictor.controller;

import javax.swing.SwingWorker;

import predictor.gui.mainGui;
import predictor.controller.listener.StartListener;
import predictor.controller.listener.StartTestListener;
import predictor.controller.listener.CancelListener;
import predictor.controller.listener.RestartListener;

public class Controller {
    public mainGui g;
    public SwingWorker<?, ?> task;

    public Controller(mainGui g) {
        this.g = g;
        g.addStartListener(new StartListener(this));
        g.addCancelListener(new CancelListener(this));
        g.addRestartListener(new RestartListener(this));
        g.addRestartTestListener(new RestartListener(this));
        g.addStartTestListener(new StartTestListener(this));
    }
    public void start() {
    	g.frame.setVisible(true);
    }
}
