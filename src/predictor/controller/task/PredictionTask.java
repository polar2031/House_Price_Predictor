package predictor.controller.task;

import java.util.List;

import javax.swing.SwingWorker;

import predictor.controller.Common;
import predictor.gui.mainGui;
import predictor.model.Model;
import predictor.model.Sampler;

public class PredictionTask extends SwingWorker<Void, MessagePack> {
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
		workDone = false;
		int progress = 0;
		//
		publish(new MessagePack(progress, "Get Information of Target House"));
		try{
			m.setTarget(g.getInputAddress(), g.getInputCity(), g.getInputState(), g.getInputZip());
		}
		catch(Exception e){
			publish(new MessagePack(progress, "Connection Error"));
			return null;
		}
		if(m.isTargetSet() == false){
			publish(new MessagePack(progress, "House Not Found on Zillow.com"));
			return null;
		}
		if(isCancelled()){
			return null;
		}
		
		progress = 30;
		
		//sample range (in miles)
		double range = 1.0;
		List<String> CoordinateList = Sampler.getSampleCoordinateList(m.target.latitude, m.target.longitude, range);
		
		for(int i = 0; !isCancelled() && i < CoordinateList.size(); i++){
			int persentage = i * 100 / CoordinateList.size();
			progress = 30 + 30 * persentage / 100;
			publish(new MessagePack(progress, "Get Sample Houses: " + persentage + "%"));
			
			if(!Sampler.isDataOfCoordinateUp2Date(CoordinateList.get(i))){
				publish(new MessagePack(progress, "Get Sample Houses: " + persentage + "%  Updating Data..."));
				try{
					Sampler.updateDataOfCoordinate(CoordinateList.get(i));
				}
				catch(Exception e){
					e.printStackTrace();
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
		
		progress = 60;
		publish(new MessagePack(progress, "Predicting"));
		try{
			m.sampleFilter(200);
            m.predict();
		}
		catch(Exception e){
			publish(new MessagePack(progress, "Oops, Something Went Wrong"));
			e.printStackTrace();
		}
		
		workDone = true;
		return null;
	}
	
	@Override
    protected void done(){
		// task is done normally
    	if(workDone == true){
    		g.showResultCard();
    		Common.showHouseInfo(m.target, g);
            Common.showSampleList(m.sampleList, g);
            g.cancelButton.setEnabled(false);
            g.startButton.setEnabled(true);
    	}
    	// task is cancelled
    	else if(isCancelled()){
            g.cancelButton.setEnabled(false);
            g.startButton.setEnabled(true);
            g.showInputCard();
    	}
    	// error occured
    	else{
            g.cancelButton.setEnabled(false);
            g.startButton.setEnabled(true);
            g.showInputCard();
    	}
    	workDone = false;
    }
	
	@Override
	protected void process(List<MessagePack> chunks){
		for(MessagePack row : chunks){
			g.setProgressBar(row.progress);
			g.setStatusText(row.message);
		}
	}
}

class MessagePack{
	int progress;
	String message;
	
	public MessagePack(int i, String s){
		progress = i;
		message = s;
	}
}
