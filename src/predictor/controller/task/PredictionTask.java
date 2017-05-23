package predictor.controller.task;

import java.util.List;

import javax.swing.SwingWorker;

import predictor.controller.Common;
import predictor.gui.mainGui;
import predictor.model.Model;
import predictor.model.Sampler;

public class PredictionTask extends SwingWorker<Void, String> {
	private Model m;
	private mainGui g;
	boolean workDone;
	
	public PredictionTask(Model m, mainGui g){
		super();
		this.m = m;
		this.g = g;
	}
	
	@Override
	protected Void doInBackground() throws Exception{

		//
		publish("Get Information of Target House");
		try{
			m.setTarget(g.getInputAddress(), g.getInputCity(), g.getInputState(), g.getInputZip());
		}
		catch(Exception e){
			publish("Connection Error");
			return null;
		}
		if(isCancelled()){
			return null;
		}
		if(m.isTargetSet() == false){
			publish("House Not Found on Zillow.com");
			return null;
		}
		if(isCancelled()){
			return null;
		}
		
		List<String> CoordinateList = Sampler.getSampleCoordinateList(m.target.latitude, m.target.longitude, 1.0);

		for(int i = 0; !isCancelled() && i < CoordinateList.size(); i++){
			int persentage = i * 100 / CoordinateList.size();
			publish("Get Sample Houses: " + persentage + "%");
			
			if(!Sampler.isDataOfCoordinateUp2Date(CoordinateList.get(i))){
				publish("Get Sample Houses: " + persentage + "%  Updating Data...");
				try{
					Sampler.updateDataOfCoordinate(CoordinateList.get(i));
				}
				catch(Exception e){
					
				}
				if(isCancelled()){
					return null;
				}
			}
			try{
				m.addSamples(Sampler.getDataOfCoordinate(CoordinateList.get(i)));
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		publish("Predicting");
		try{
			m.sampleFilter(80);
            m.predict();
		}
		catch(Exception e){
			publish("Oops, Something Went Wrong");
			e.printStackTrace();
		}
		
		workDone = true;
		return null;
	}
	
	@Override
    protected void done(){
    	if(!isCancelled() && workDone == true){
    		g.showResultCard();
    		Common.showHouseInfo(m.target, g);
            Common.showSampleList(m.sampleList, g);
//            g.status.setText("Done");
//            g.cancelButton.setEnabled(false);
            g.startButton.setEnabled(true);
    	}
    	else{
//            g.cancelButton.setEnabled(false);
            g.startButton.setEnabled(true);
    	}
    	workDone = false;
    }
	
	@Override
	protected void process(List<String> chunks){
		for(String row : chunks){
			g.setStatusText(row);
		}
	}
}
