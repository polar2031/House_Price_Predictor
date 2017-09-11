package predictor.controller.task;

import java.util.List;

import javax.swing.SwingWorker;

import predictor.controller.Common;
import predictor.gui.MainGui;
import predictor.model.PredictionModel;
import predictor.model.Sampler;

public class PredictionTask extends SwingWorker<Void, MessagePack> {
	private PredictionModel m;
	private MainGui g;
	boolean workDone;
	
	public PredictionTask(MainGui g){
		super();
		this.g = g;
	}
	
	@Override
	protected Void doInBackground() throws Exception{
		m = new PredictionModel();
		workDone = false;
		int progress = 0;
		
		// find target house
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
		
		//find nearby houses
		progress = 30;
		publish(new MessagePack(progress, "Get Data of Nearby Houses"));
		m.addSamples(Sampler.getDataOfZip(m.target.zip));
		
		// start predicting
		progress = 60;
		publish(new MessagePack(progress, "Predicting"));
		try{
			m.setSampleNumber(g.getSampleNumber());
			m.setVariable(Common.getVariableOptions(g));
			m.setVariableTransfer(Common.getVariableTransferOptions(g));
			m.setValueTransfer(Common.getValueTransferOptions(g));
			m.predict(Common.getMethod(g));
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
//            g.cancelButton.setEnabled(false);
//            g.startButton.setEnabled(true);
    	}
    	// task is cancelled
    	else if(isCancelled()){
//            g.cancelButton.setEnabled(false);
//            g.startButton.setEnabled(true);
            g.showInputCard();
    	}
    	// error occured
    	else{
//            g.cancelButton.setEnabled(false);
//            g.startButton.setEnabled(true);
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

