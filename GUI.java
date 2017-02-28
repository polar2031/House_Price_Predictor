import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// import java.io.IOException;
// import java.io.OutputStream;
// import java.io.PrintStream;

public class GUI{
	private JFrame f;
	private JTextField input[];
    public JLabel status;
    // Input columns' name
    private String name[] = {"Address",
							"City",
							"State",
							"Zip",
							"Floor Size",
							"Number of Bed Room",
							"Number of Bath Room"};

	public GUI(){
		//set program style as default style of OS
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e){}

        // create frame with name "House Price Predictor"
		f = new JFrame("House Price Predictor");
        // set frame size
        f.setSize(900, 400);
        // set frame layout type
        f.setLayout(new GridBagLayout());
        // exit GUI after closing the frame
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints c = new GridBagConstraints();
        JPanel p1 = new JPanel();
        p1.setLayout(new GridBagLayout());
		// EmptyBorder(up, left, down, right)
		p1.setBorder(new EmptyBorder(0,20,0,20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        f.add(p1, c);

	        // set input area
	        GridBagConstraints c1 = new GridBagConstraints();
	        input = new JTextField[name.length];

			//address
			JLabel l1_1 = new JLabel(name[0]);
			c1.gridx = 0;
			c1.gridy = 0;
			c1.gridwidth = 1;
			c1.gridheight = 1;
			c1.weightx = 0.2;
			c1.weighty = 0;
			c1.fill = GridBagConstraints.NONE;
			c1.anchor = GridBagConstraints.CENTER;
			p1.add(l1_1, c1);

			JTextField t1_1 = new JTextField();
			c1.gridx = 1;
			c1.gridy = 0;
			c1.gridwidth = 1;
			c1.gridheight = 1;
			c1.weightx = 3;
			c1.weighty = 0;
			c1.fill = GridBagConstraints.BOTH;
			c1.anchor = GridBagConstraints.WEST;
			p1.add(t1_1, c1);
			input[0] = t1_1;

			//City
			JLabel l1_2 = new JLabel(name[1]);
			c1.gridx = 0;
			c1.gridy = 1;
			c1.gridwidth = 1;
			c1.gridheight = 1;
			c1.weightx = 0.2;
			c1.weighty = 0;
			c1.fill = GridBagConstraints.NONE;
			c1.anchor = GridBagConstraints.CENTER;
			p1.add(l1_2, c1);

			JTextField t1_2 = new JTextField();
			c1.gridx = 1;
			c1.gridy = 1;
			c1.gridwidth = 1;
			c1.gridheight = 1;
			c1.weightx = 1;
			c1.weighty = 0;
			c1.fill = GridBagConstraints.BOTH;
			c1.anchor = GridBagConstraints.WEST;
			p1.add(t1_2, c1);
			input[1] = t1_2;

			//state
			JLabel l1_3 = new JLabel(name[2]);
			c1.gridx = 0;
			c1.gridy = 2;
			c1.gridwidth = 1;
			c1.gridheight = 1;
			c1.weightx = 0.2;
			c1.weighty = 0;
			c1.fill = GridBagConstraints.NONE;
			c1.anchor = GridBagConstraints.CENTER;
			p1.add(l1_3, c1);

			JTextField t1_3 = new JTextField();
			c1.gridx = 1;
			c1.gridy = 2;
			c1.gridwidth = 1;
			c1.gridheight = 1;
			c1.weightx = 0.1;
			c1.weighty = 0;
			c1.fill = GridBagConstraints.BOTH;
			c1.anchor = GridBagConstraints.WEST;
			p1.add(t1_3, c1);
			input[2] = t1_3;

			//zip
			JLabel l1_4 = new JLabel(name[3]);
			c1.gridx = 0;
			c1.gridy = 3;
			c1.gridwidth = 1;
			c1.gridheight = 1;
			c1.weightx = 0.2;
			c1.weighty = 0;
			c1.fill = GridBagConstraints.NONE;
			c1.anchor = GridBagConstraints.CENTER;
			p1.add(l1_4, c1);

			JTextField t1_4 = new JTextField();
			c1.gridx = 1;
			c1.gridy = 3;
			c1.gridwidth = 1;
			c1.gridheight = 1;
			c1.weightx = 0.2;
			c1.weighty = 0;
			c1.fill = GridBagConstraints.BOTH;
			c1.anchor = GridBagConstraints.WEST;
			p1.add(t1_4, c1);
			input[3] = t1_4;

	        // set default text in input columns
	        input[0].setText("34 Longmeadow Ln");
	        input[1].setText("Town of Sharon");
	        input[2].setText("ma");
	        input[3].setText("02067");

        // set button area
        JPanel p2 = new JPanel();
        p2.setLayout(new GridBagLayout());
		p2.setBorder(new EmptyBorder(10,20,10,20));
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        f.add(p2, c);

	        JButton b2_1 = new JButton("start");
	        p2.add(b2_1);
	        b2_1.addActionListener(new StartLitener());

        // set output area
        JPanel p3 = new JPanel();
        p3.setLayout(new BorderLayout(5, 5));
		p3.setBorder(new EmptyBorder(0,20,20,20));
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.WEST;
        f.add(p3, c);

	        JLabel t3_1 = new JLabel("Ready");
	        p3.add(t3_1);
	        status = t3_1;

        // turn frame visible
		f.setVisible(true);
	}

    class StartLitener implements ActionListener{

		@Override
        public void actionPerformed(ActionEvent event) {
			PredictHousePrice task = new PredictHousePrice(input, status);
			task.execute();

            // House h = new House(input[0].getText(),
            //                     input[1].getText(),
            //                     input[2].getText(),
            //                     input[3].getText());
			// PricePredictor p = new PricePredictor();
			// // status.setText("");
			// if(p.start(h)){
			// 	String result = "Predict Price: " + h.predictBasePrice;
			// 	status.setText(result);
			// }
			// else{
			// 	status.setText("Oops, Something goes wrong.");
			// }
        }
    }

	class PredictHousePrice extends SwingWorker<Integer, Integer> {
		private JTextField input[];
		private JLabel status;

		public PredictHousePrice(JTextField input[], JLabel status){
			this.input = input;
			this.status = status;
		}

		protected Integer doInBackground() throws Exception{
			House h = new House(input[0].getText(),
								input[1].getText(),
								input[2].getText(),
								input[3].getText());
			PricePredictor p = new PricePredictor();
			if(p.start(h)){
				String result = "Predict Price: " + h.predictBasePrice;
				status.setText(result);
			}
			else{
				status.setText("Oops, Something goes wrong.");
			}
			return 0;
		}
	}


}
