package predictor.controller;

import javax.swing.SwingWorker;

import predictor.model.Model;
import predictor.gui.GUI;
import predictor.controller.listener.StartListener;
import predictor.controller.listener.CancelListener;

public class Controller {
    public Model m;
    public GUI g;
    public SwingWorker<?, ?> task;

    public Controller(Model m, GUI g) {
        this.m = m;
        this.g = g;
        g.addStartListener(new StartListener(this));
        g.addCancelListener(new CancelListener(this));
    }
    public void start() {
    	g.show();
    }
}
