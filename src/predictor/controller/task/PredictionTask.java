package predictor.controller.task;

import java.util.List;

import javax.swing.SwingWorker;

import predictor.controller.Common;
import predictor.gui.GUI;
import predictor.model.Model;
import predictor.model.Sampler;

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
		
		List<String> CoordinateList = Sampler.getSampleCoordinateList(m.target.latitude, m.target.longitude);
		
		for(int i = 0; !isCancelled() && i < CoordinateList.size(); i++){
			int persentage = (i + 1) * 100 / CoordinateList.size();
			publish("Get Sample Houses: " + persentage + "%");
			
			if(Sampler.isDataOfCoordinateUp2Date(CoordinateList.get(i))){
				publish("Get Sample Houses: " + persentage + "%  Updating Data...");
				Sampler.updateDataOfCoordinate(CoordinateList.get(i));
			}
			m.addSamples(Sampler.getDataOfCoordinate(CoordinateList.get(i)));
		}
		
		try{
			publish("Predicting");
			m.sampleFilter(80);
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
