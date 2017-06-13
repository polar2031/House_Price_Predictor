package predictor.controller.task;

import java.util.List;

import javax.swing.SwingWorker;

import predictor.controller.Common;
import predictor.gui.mainGui;
import predictor.model.PredictionModel;
import predictor.model.Sampler;
import predictor.model.data.House;

public class ListPredictionTask extends SwingWorker<Void, MessagePack> {
	private PredictionModel m;
	private mainGui g;
	boolean workDone;
	
	public ListPredictionTask(mainGui g){
		super();
		this.g = g;
	}
	
	@Override
	protected Void doInBackground() throws Exception{
		List<House> testHouses = null;
		try{
			testHouses = Sampler.getDataOfCoordinate("4211,10883");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		int totalProgress = 100 * testHouses.size();
		
		double[] errorPercentage = new double[testHouses.size()];
		
		for(int j = 0; j < testHouses.size(); j++){
			System.err.println("---Info---");
			System.err.println(testHouses.get(j).address);
			System.err.println(testHouses.get(j).floorSize);
			System.err.println(testHouses.get(j).lastSoldPrice);
			System.err.println("---Info---");

			m = new PredictionModel();
			workDone = false;
			int progress = 0;
			m.target = testHouses.get(j);
			
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
				m.setVariable(g.getTestVariableOptions());
	            m.predict();
			}
			catch(Exception e){
				publish(new MessagePack(progress, "Oops, Something Went Wrong"));
				e.printStackTrace();
			}
			
			errorPercentage[j] = Math.abs((m.target.predictPrice - m.target.lastSoldPrice) / m.target.lastSoldPrice);
		}
		
		int five = 0;
		int ten = 0;
		int twenty = 0;
		int fifty = 0;
		int total = 0;
		for(int i = 0; i < errorPercentage.length; i++){
			if(errorPercentage[i] <= 0.05){
				five++;
				ten++;
				twenty++;
				fifty++;
			}
			else if(errorPercentage[i] <= 0.1){
				ten++;
				twenty++;
				fifty++;
			}
			else if(errorPercentage[i] <= 0.2){
				twenty++;
				fifty++;
			}
			else if(errorPercentage[i] <= 0.5){
				fifty++;
			}
			total++;
			System.err.println(errorPercentage[i]);
		}
		
		System.err.println("5%");
		System.err.println((double)five / (double)errorPercentage.length);
		System.err.println("10%");
		System.err.println((double)ten / (double)errorPercentage.length);
		System.err.println("20%");
		System.err.println((double)twenty / (double)errorPercentage.length);
		System.err.println("50%");
		System.err.println((double)fifty / (double)errorPercentage.length);
		System.err.println("total");
		System.err.println((double)total / (double)errorPercentage.length);
		workDone = true;
		return null;
	}
	
	@Override
    protected void done(){
		// task is done normally
    	if(workDone == true){
    		g.showTestResultCard();
    		Common.showHouseInfo(m.target, g);
            Common.showSampleList(m.sampleList, g);
            g.cancelButton.setEnabled(false);
            g.startTestButton.setEnabled(true);
    	}
    	// task is cancelled
    	else if(isCancelled()){
            g.cancelButton.setEnabled(false);
            g.startTestButton.setEnabled(true);
            g.showInputCard();
    	}
    	// error occured
    	else{
            g.cancelButton.setEnabled(false);
            g.startTestButton.setEnabled(true);
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

