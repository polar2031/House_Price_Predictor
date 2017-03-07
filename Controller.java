import javax.swing.*;
import java.awt.event.*;
import java.text.DecimalFormat;


public class Controller{
    private Model m;
    private GUI g;

    Controller(Model m, GUI g){
        this.m = m;
        this.g = g;
        g.addStartListener(new StartListener());
    }

    class StartListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            g.startButton.setEnabled(false);
            PredictHousePrice task = new PredictHousePrice(g.input, g.startButton, g.status);
            task.execute();
        }
    }

    class PredictHousePrice extends SwingWorker<Integer, Integer> {
        private JTextField input[];
        private JLabel status;
        private JButton startButton;
        public PredictHousePrice(JTextField input[], JButton startButton, JLabel status){
            this.input = input;
            this.status = status;
            this.startButton = startButton;
        }

        protected Integer doInBackground() throws Exception{
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
            DecimalFormat formatter = new DecimalFormat("0.00");
            String result = "Predict Price: " + formatter.format(m.target.predictBasePrice);
            
            status.setText(result);
            startButton.setEnabled(true);
        }

    }
}
