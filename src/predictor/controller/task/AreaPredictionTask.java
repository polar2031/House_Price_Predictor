package predictor.controller.task;

import java.util.List;

import javax.swing.SwingWorker;

import predictor.controller.Common;
import predictor.gui.MainGui;
import predictor.model.PredictionModel;
import predictor.model.Sampler;
import predictor.model.data.House;
import predictor.model.regression.PreProcessor;

public class AreaPredictionTask extends SwingWorker<Void, MessagePack> {
	private PredictionModel m;
	private MainGui g;
	boolean workDone;
	
	public AreaPredictionTask(MainGui g){
		super();
		this.g = g;
	}
	
	@Override
	protected Void doInBackground() throws Exception{
		List<House> testHouses = null;
		String zip = g.getInputAreaZip();
		try{
			if(!Sampler.isDataOfZipUp2Date(zip)){
				try{
					Sampler.updateDataOfzip(zip);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			testHouses = PreProcessor.basicFilter(Sampler.getDataOfZip(zip));
			testHouses = PreProcessor.soldTimeFilter(testHouses, 360);
			
			//--------------------------------------------
			List<House> temp = Sampler.getDataOfZip(zip);
			for(int i = 0; i < temp.size(); i++){
				System.out.println(temp.get(i).floorSize + "," + temp.get(i).lastSoldPrice);
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		double[] errorPercentage = new double[testHouses.size()];
		
		for(int j = 0; j < testHouses.size(); j++){

			m = new PredictionModel();
			workDone = false;
			int progress = 0;
			m.target = testHouses.get(j);
			
			progress = 30;
			
//			//sample range (in miles)
//			double range = 1.0;
//			List<String> CoordinateList = Sampler.getSampleCoordinateList(m.target.latitude, m.target.longitude, range);
//			for(int i = 0; !isCancelled() && i < CoordinateList.size(); i++){
//				int persentage = i * 100 / CoordinateList.size();
//				progress = 30 + 30 * persentage / 100;
//				publish(new MessagePack(progress, "Get Sample Houses: " + persentage + "%"));
//				
//				if(!Sampler.isDataOfCoordinateUp2Date(CoordinateList.get(i))){
//					publish(new MessagePack(progress, "Get Sample Houses: " + persentage + "%  Updating Data..."));
//					try{
//						Sampler.updateDataOfCoordinate(CoordinateList.get(i));
//					}
//					catch(Exception e){
//						e.printStackTrace();
//					}
//					if(isCancelled()){
//						return null;
//					}
//				}
//				try{
//					m.addSamples(Sampler.getDataOfCoordinate(CoordinateList.get(i)));
//				}
//				catch(Exception e){
//					e.printStackTrace();
//				}
//			}
			
			m.addSamples(Sampler.getDataOfZip(zip));
			
			progress = 60;
			publish(new MessagePack(progress, "Predicting"));
			
			try{
				m.sampleFilter(g.getSampleNumber());
				m.setVariable(Common.getVariableOptions(g));
				m.setVariableTransfer(Common.getVariableTransferOptions(g));
				m.setValueTransfer(Common.getValueTransferOptions(g));
				m.predict(Common.getMethod(g));
			}
			catch(Exception e){
				publish(new MessagePack(progress, "Oops, Something Went Wrong"));
				e.printStackTrace();
			}
			
			errorPercentage[j] = Math.abs((m.target.predictPrice - m.target.lastSoldPrice) / m.target.lastSoldPrice);
			System.err.println(m.target.predictPrice + ", " + m.target.lastSoldPrice + ", " + errorPercentage[j]);
		}
		
		int five = 0;
		int ten = 0;
		int twenty = 0;
		int fifty = 0;
		for(int i = 0; i < errorPercentage.length; i++){
			if(errorPercentage[i] < 0.05){
				five++;
				ten++;
				twenty++;
				fifty++;
			}
			else if(errorPercentage[i] < 0.1){
				ten++;
				twenty++;
				fifty++;
			}
			else if(errorPercentage[i] < 0.2){
				twenty++;
				fifty++;
			}
			else if(errorPercentage[i] < 0.5){
				fifty++;
			}
//			System.err.println(errorPercentage[i]);
		}
		
		showResult("Error Rate:");
		showResult("Error Rate in 5%");
		showResult(String.valueOf((double)five / (double)errorPercentage.length));
		showResult("");
		showResult("Error Rate in 10%");
		showResult(String.valueOf((double)ten / (double)errorPercentage.length));
		showResult("");
		showResult("Error Rate in 20%");
		showResult(String.valueOf((double)twenty / (double)errorPercentage.length));
		showResult("");
		showResult("Error Rate in 50%");
		showResult(String.valueOf((double)fifty / (double)errorPercentage.length));

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
    	// error occured
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

