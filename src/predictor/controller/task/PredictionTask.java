package predictor.controller.task;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import predictor.controller.Common;
import predictor.gui.GUI;
import predictor.model.Model;

public class PredictionTask extends SwingWorker<Void, String> {
	private Model m;
	private GUI g;
	
	public PredictionTask(Model m, GUI g){
		super();
		this.m = m;
		this.g = g;
	}
	
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
		
		
        DecimalFormat formatter = new DecimalFormat("#");

		for(int i = 0; !isCancelled() && i < dataList.size(); i++){
			float persantage = ((float)(i + 1) / (float)dataList.size()) * 100;
			publish("Get Sample Houses: " + formatter.format(persantage) + "%");
			try{
				m.getDataOfCoordinate(dataList.get(i));
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		try{
			publish("Predicting");
			m.fixSample();
			m.sampleFilter();
            m.predict();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
    protected void done(){
    	if(!isCancelled()){
    		Common.showHouseInfo(m.target, g);
            Common.showSampleList(m.sampleList, g);
            g.status.setText("ready");
            g.cancelButton.setEnabled(false);
            g.startButton.setEnabled(true);
    	}
    	else{
    		g.status.setText("ready");
            g.cancelButton.setEnabled(false);
            g.startButton.setEnabled(true);
    	}
    }
	
	@Override
	protected void process(List<String> chunks){
		for(String row : chunks){
			g.status.setText(row);
		}
	}
}
