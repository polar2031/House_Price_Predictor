import javax.swing.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;



public class Controller{
    private Model m;
    private GUI g;

    PredictHousePrice task;

    Controller(Model m, GUI g){
        this.m = m;
        this.g = g;
        g.addStartListener(new StartListener());
        g.addCancelListener(new CancelListener());
    }

    class StartListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            g.startButton.setEnabled(false);
            task = new PredictHousePrice(g);
            task.execute();
            g.cancelButton.setEnabled(true);
        }
    }

    class CancelListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            if(task != null){
                task.cancel(true);
            }
        }
    }

    class PredictHousePrice extends SwingWorker<Integer, Integer> {
        private JTextField input[];
        private JTextField targetHouseInfo[];
        private JTextArea sampleList;
        private JLabel status;
        private JButton startButton;
        private JButton cancelButton;

        public PredictHousePrice(GUI g){
            this.startButton = g.startButton;
            this.cancelButton = g.cancelButton;
            this.input = g.input;
            this.targetHouseInfo = g.targetHouseInfo;
            this.sampleList = g.sampleList;
            this.status = g.status;
        }

        protected Integer doInBackground() throws Exception{
            status.setText("Running");
            boolean re = true;
            m.reset();
            re = m.searchHouse(input[0].getText(), input[1].getText(), input[2].getText(), input[3].getText());
            if(re == false){
                status.setText("House information not found");
                return 0;
            }
            re = m.getSamples();
            if(re == false){
                status.setText("Error occur while searching samples");
                return 0;
            }
            re = m.adjustSampleWeight();
            if(re == false){
                status.setText("Error occur while adjusting sample weight");
                return 0;
            }
            re = m.predict();
            if(re == false){
                status.setText("Error occur while predicting");
                return 0;
            }
            return 0;
        }
        protected void done(){
            showHouseInfo();
            showSampleList();
            status.setText("Ready");
            cancelButton.setEnabled(false);
            startButton.setEnabled(true);
        }
        private void showHouseInfo(){
            DecimalFormat formatter = new DecimalFormat("0.00");
            targetHouseInfo[0].setText(formatter.format(m.target.floorSize));
            targetHouseInfo[1].setText(formatter.format(m.target.lotSize));
            formatter.applyPattern("0.0");
            targetHouseInfo[2].setText(formatter.format(m.target.bedroomNumber));
            targetHouseInfo[3].setText(formatter.format(m.target.bathroomNumber));
            formatter.applyPattern("0");
            targetHouseInfo[4].setText(formatter.format(m.target.builtYear));
            if(m.target.lastSoldDate != null){
                SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy");
                targetHouseInfo[5].setText(sdFormatter.format(m.target.lastSoldDate));
                formatter.applyPattern("0.00");
                targetHouseInfo[6].setText(formatter.format(m.target.lastSoldPrice));
            }
            targetHouseInfo[7].setText(formatter.format(m.target.predictBasePrice));
        }
        private void clearHouseInfo(){
            for(int i = 0; i < targetHouseInfo.length; i ++){
                targetHouseInfo[i].setText("");
            }
        }
        private void showSampleList(){
            for(int i = 0; i < m.samples.size(); i++){
                sampleList.setText(sampleList.getText() + m.samples.get(i).address + "\n");
            }
        }

    }
}
