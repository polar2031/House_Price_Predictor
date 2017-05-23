package predictor.controller;

import javax.swing.SwingWorker;

import predictor.model.Model;
import predictor.gui.mainGui;
import predictor.controller.listener.StartListener;
import predictor.controller.listener.CancelListener;
import predictor.controller.listener.RestartListener;

public class Controller {
    public Model m;
    public mainGui g;
    public SwingWorker<?, ?> task;

    public Controller(Model m, mainGui g) {
        this.m = m;
        this.g = g;
        g.addStartListener(new StartListener(this));
        g.addCancelListener(new CancelListener(this));
        g.addRestartListener(new RestartListener(this));
    }
    public void start() {
    	g.frame.setVisible(true);
    }
}
