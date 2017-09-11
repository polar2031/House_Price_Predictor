package predictor.controller.task;

import java.util.List;

import javax.swing.SwingWorker;

import predictor.controller.Common;
import predictor.gui.MainGui;
import predictor.model.PredictionModel;
import predictor.model.PredictionModel2;
import predictor.model.Sampler;
import predictor.model.data.House;
import predictor.model.regression.PreProcessor;

public class AreaPredictionTask extends SwingWorker<Void, MessagePack> {
	private PredictionModel2 m;
	private MainGui g;
	boolean workDone;
	
	public AreaPredictionTask(MainGui g){
		super();
		this.g = g;
		
	}
	
	@Override
	protected Void doInBackground() throws Exception{
		workDone = false;
		List<House> testHouses = null;
		String zip = g.getInputAreaZip();
		try{
			if(!Sampler.isDataOfZipUp2Date(zip)){
				Sampler.updateDataOfzip(zip);
			}
			testHouses = Sampler.getDataOfZip(zip);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		//remove testing data with missing/error feature
		testHouses = PreProcessor.basicFilter(testHouses);
		
		double[] errorPercentage = new double[testHouses.size()];
		for(int i = 0; i < testHouses.size(); i++){
			int progress = 0;
			
			m = new PredictionModel2();
			m.setTarget(testHouses.get(i));
			
			progress = 30;
			
			m.setSamples(testHouses);
//				m.addSamples(ZillowParser.parseArea(m.target.latitude, m.target.longitude, 0.042197, 0.056992));
			
			progress = 60;
			publish(new MessagePack(progress, "Predicting"));
			
			try{
//				m.setSampleNumber(g.getSampleNumber());
				m.setVariable(Common.getVariableOptions(g));
				m.setVariableTransfer(Common.getVariableTransferOptions(g));
				m.setValueTransfer(Common.getValueTransferOptions(g));
				m.predict(Common.getMethod(g));

			}
			catch(Exception e){
				publish(new MessagePack(progress, "Oops, Something Went Wrong"));
				e.printStackTrace();
			}
			
			errorPercentage[i] = Math.abs((m.target.predictPrice - m.target.lastSoldPrice) / m.target.lastSoldPrice);
//				System.err.println(m.target.predictPrice + ", " + m.target.lastSoldPrice + ", " + errorPercentage[j]);
			System.out.println(m.target.lastSoldPrice + "," + m.target.floorSize + "," + m.target.lotSize + 
					"," + m.target.bedroomNumber + "," + m.target.bathroomNumber + "," + m.target.builtYear + "," + 
					m.target.longitude + "," + m.target.latitude + "," + m.target.address + "," + 
					m.target.lastSoldPrice / m.target.floorSize + "," + m.target.predictPrice + "," + m.sampleList.size());
//			System.out.println(m.target.lastSoldPrice + "," + m.target.predictPrice);

		}
	
	
		int five = 0;
		int ten = 0;
		int fifteen = 0;
		int twenty = 0;
		for(int i = 0; i < errorPercentage.length; i++){
			if(errorPercentage[i] < 0.05){
				five++;
				ten++;
				fifteen++;
				twenty++;
			}
			else if(errorPercentage[i] < 0.1){
				ten++;
				fifteen++;
				twenty++;
			}
			else if(errorPercentage[i] < 0.15){
				fifteen++;
				twenty++;
			}
			else if(errorPercentage[i] < 0.2){
				twenty++;
			}
		}
	
	
//		showResult("Error Rate:");
//		showResult("Error Rate in 5%");
		showResult(String.valueOf((double)five / (double)errorPercentage.length));
//		showResult("");
//		showResult("Error Rate in 10%");
		showResult(String.valueOf((double)ten / (double)errorPercentage.length));
//		showResult("");
//		showResult("Error Rate in 20%");
		showResult(String.valueOf((double)fifteen / (double)errorPercentage.length));
//		showResult("");
//		showResult("Error Rate in 50%");
		showResult(String.valueOf((double)twenty / (double)errorPercentage.length));
		showResult("");
		showResult(String.valueOf(errorPercentage.length));
		
		
		workDone = true;
		return null;

	}
	
	@Override
    protected void done(){
		// task is done normally
    	if(workDone == true){
    		g.showAreaResultCard();
//    		Common.showHouseInfo(m.target, g);
//            Common.showSampleList(m.sampleList, g);
//            g.cancelButton.setEnabled(false);
//            g.startTestButton.setEnabled(true);
    	}
    	// task is cancelled
    	else if(isCancelled()){
//            g.cancelButton.setEnabled(false);
//            g.startTestButton.setEnabled(true);
            g.showInputCard();
    	}
    	// error occurred
    	else{
//            g.cancelButton.setEnabled(false);
//            g.startTestButton.setEnabled(true);
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
	
	private void showResult(String s){
//		g.textArea.setText(g.textArea.getText() + "\r\n" + s);
		System.err.println(s);
	}
	private void clearResult(){
//		g.textArea.setText("");
	}
	
}

