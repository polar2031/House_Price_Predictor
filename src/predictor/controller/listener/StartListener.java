package predictor.controller.listener;

import predictor.controller.Controller;
import predictor.gui.GUI;
import predictor.model.Model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

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
        c.task = new PredictionTask();
        c.task.execute();
        c.g.cancelButton.setEnabled(true);
    }
	
	class PredictionTask extends SwingWorker<Void, String> {
		
		@Override
		protected Void doInBackground() throws Exception{
			publish("Get Information of Target House");
			if(!m.setTarget(g.getInputAddress(), g.getInputCity(), g.getInputState(), g.getInputZip())){
				publish("House Not Found on Zillow or Internet Error");
				return null;
			}
			if(isCancelled()){
				return null;
			}
			
			ArrayList<String> dataList = m.getSampleDataList();
			
			for(int i = 0; !isCancelled() && i < dataList.size(); i++){
				publish("Get Sample Houses" + String.valueOf(i + 1) + "/" + String.valueOf(dataList.size()));
				try{
					m.getDataOfCoordinate(dataList.get(i));
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			
			return null;
		}
		
		@Override
        protected void done(){
        	if(!isCancelled()){
	            c.g.cancelButton.setEnabled(false);
	            c.g.startButton.setEnabled(true);
        	}
        	else{
                c.g.cancelButton.setEnabled(false);
                c.g.startButton.setEnabled(true);
        	}
        }
		
		@Override
		protected void process(List<String> chunks){
			for(String row : chunks){
				c.g.status.setText(row);
			}
		}
	}
}