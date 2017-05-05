package predictor.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;


public class GUI {
	public JFrame f;
	public JTextField input[];
    public JButton startButton;
	public JButton cancelButton;
	public JTextField targetHouseInfo[];
	public JTextArea sampleList;
	public JLabel status;

    // Input columns' name
    private String name[] = {"Address",
							"City",
							"State",
							"Zip",
							"Floor Size"};
	private String houseInfo[] = {"Floor Size",
								"Lot Size",
								"Bedroom",
								"Bathroom",
								"Built Year",
								"Last Sold time",
								"Last Sold Price",
								"Predict Price"
							};

	public GUI(){
		//set program style as default style of the OS
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e){}

        // create frame with name "House Price Predictor"
		f = new JFrame("House Price Predictor");
        // set frame size
        f.setSize(900, 600);
        // set frame layout type
        f.setLayout(new GridBagLayout());
        // exit GUI after closing the frame
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints c = new GridBagConstraints();


		/*
		██ ███    ██ ██████  ██    ██ ████████      █████  ██████  ███████  █████
		██ ████   ██ ██   ██ ██    ██    ██        ██   ██ ██   ██ ██      ██   ██
		██ ██ ██  ██ ██████  ██    ██    ██        ███████ ██████  █████   ███████
		██ ██  ██ ██ ██      ██    ██    ██        ██   ██ ██   ██ ██      ██   ██
		██ ██   ████ ██       ██████     ██        ██   ██ ██   ██ ███████ ██   ██
		*/
        JPanel p1 = new JPanel();
        p1.setLayout(new GridBagLayout());
		// EmptyBorder(up, left, down, right)
		p1.setBorder(new EmptyBorder(20,20,0,20));
		setGridBagConstraints(c, 0, 0, 1, 1, 1, 0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        f.add(p1, c);

	        GridBagConstraints c1 = new GridBagConstraints();
	        input = new JTextField[name.length];

			//address
			JLabel l1_1 = new JLabel(name[0]);
			setGridBagConstraints(c1, 0, 0, 1, 1, 0.2, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
			p1.add(l1_1, c1);

			JTextField t1_1 = new JTextField();
			setGridBagConstraints(c1, 1, 0, 5, 1, 3, 0, GridBagConstraints.BOTH, GridBagConstraints.WEST);
			p1.add(t1_1, c1);
			input[0] = t1_1;

			//City
			JLabel l1_2 = new JLabel(name[1]);
			setGridBagConstraints(c1, 0, 1, 1, 1, 0.2, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
			p1.add(l1_2, c1);

			JTextField t1_2 = new JTextField();
			setGridBagConstraints(c1, 1, 1, 1, 1, 1, 0, GridBagConstraints.BOTH, GridBagConstraints.WEST);
			p1.add(t1_2, c1);
			input[1] = t1_2;

			//state
			JLabel l1_3 = new JLabel(name[2]);
			setGridBagConstraints(c1, 2, 1, 1, 1, 0.2, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
			p1.add(l1_3, c1);

			JTextField t1_3 = new JTextField();
			setGridBagConstraints(c1, 3, 1, 1, 1, 0.1, 0, GridBagConstraints.BOTH, GridBagConstraints.WEST);
			p1.add(t1_3, c1);
			input[2] = t1_3;

			//zip
			JLabel l1_4 = new JLabel(name[3]);
			setGridBagConstraints(c1, 4, 1, 1, 1, 0.2, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
			p1.add(l1_4, c1);

			JTextField t1_4 = new JTextField();
			setGridBagConstraints(c1, 5, 1, 1, 1, 0.2, 0, GridBagConstraints.BOTH, GridBagConstraints.WEST);
			p1.add(t1_4, c1);
			input[3] = t1_4;

	        // set default text in input columns
	        input[0].setText("9 ginger way");
	        input[1].setText("Sharon");
	        input[2].setText("ma");
	        input[3].setText("02067");

		/*
		██████  ██    ██ ████████ ████████  ██████  ███    ██      █████  ██████  ███████  █████
		██   ██ ██    ██    ██       ██    ██    ██ ████   ██     ██   ██ ██   ██ ██      ██   ██
		██████  ██    ██    ██       ██    ██    ██ ██ ██  ██     ███████ ██████  █████   ███████
		██   ██ ██    ██    ██       ██    ██    ██ ██  ██ ██     ██   ██ ██   ██ ██      ██   ██
		██████   ██████     ██       ██     ██████  ██   ████     ██   ██ ██   ██ ███████ ██   ██
		*/

        JPanel p2 = new JPanel();
        p2.setLayout(new GridBagLayout());
		p2.setBorder(new EmptyBorder(10,20,10,20));
		setGridBagConstraints(c, 0, 1, 1, 1, 0, 0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        f.add(p2, c);

	        JButton b2_1 = new JButton("start");
			startButton = b2_1;
	        p2.add(b2_1);

			JButton b2_2 = new JButton("cancel");
			cancelButton = b2_2;
			cancelButton.setEnabled(false);
			p2.add(b2_2);

		/*
		 ██████  ██    ██ ████████ ██████  ██    ██ ████████      █████  ██████  ███████  █████
		██    ██ ██    ██    ██    ██   ██ ██    ██    ██        ██   ██ ██   ██ ██      ██   ██
		██    ██ ██    ██    ██    ██████  ██    ██    ██        ███████ ██████  █████   ███████
		██    ██ ██    ██    ██    ██      ██    ██    ██        ██   ██ ██   ██ ██      ██   ██
		 ██████   ██████     ██    ██       ██████     ██        ██   ██ ██   ██ ███████ ██   ██
		*/

        JPanel p3 = new JPanel();
        p3.setLayout(new GridBagLayout());
		p3.setBorder(new EmptyBorder(0,20,20,20));
		setGridBagConstraints(c, 0, 2, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST);
        f.add(p3, c);

			GridBagConstraints c3 = new GridBagConstraints();
			for(int i = 0; i < houseInfo.length; i++){
				JLabel l3_1 = new JLabel(houseInfo[i]);
				setGridBagConstraints(c3, 0, i, 1, 1, 0.1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
				p3.add(l3_1, c3);
			}
			targetHouseInfo = new JTextField[houseInfo.length];
			for(int i = 0; i < houseInfo.length; i++){
				JTextField t3_1 = new JTextField();
				t3_1.setEditable(false);
				targetHouseInfo[i] = t3_1;
				setGridBagConstraints(c3, 1, i, 1, 1, 0.4, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
				p3.add(t3_1, c3);
			}

			JTextArea t3_2 = new JTextArea("");
			sampleList = t3_2;
			t3_2.setEditable(false);
			JScrollPane s3_2 = new JScrollPane(t3_2);
			s3_2.setBorder(new EmptyBorder(10,10,10,10));
			setGridBagConstraints(c3, 2, 0, 1, houseInfo.length, 1, 0.5, GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST);
			p3.add(s3_2, c3);

		/*
		███████ ████████  █████  ████████ ██    ██ ███████
		██         ██    ██   ██    ██    ██    ██ ██
		███████    ██    ███████    ██    ██    ██ ███████
		     ██    ██    ██   ██    ██    ██    ██      ██
		███████    ██    ██   ██    ██     ██████  ███████
		*/

		JPanel p4 = new JPanel();
        p4.setLayout(new GridBagLayout());
		p4.setBorder(new EmptyBorder(0,10,10,10));
		setGridBagConstraints(c, 0, 3, 1, 1, 1, 0, GridBagConstraints.BOTH, GridBagConstraints.WEST);
        f.add(p4, c);

			GridBagConstraints c4 = new GridBagConstraints();

	        JLabel l4_1 = new JLabel("Ready");
			// l4_1.setBorder(BorderFactory.createLoweredBevelBorder());
	        setGridBagConstraints(c4, 0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST);
	        p4.add(l4_1, c4);
	        status = l4_1;

	}

    public void addStartListener(ActionListener start){
        startButton.addActionListener(start);
    }
    
    public void addCancelListener(ActionListener cancel){
		cancelButton.addActionListener(cancel);
	}
    public void show(){
        f.setVisible(true);
    }
	public void clean(){
		for(int i = 0; i < houseInfo.length; i++){
			targetHouseInfo[i].setText("");
		}
		sampleList.setText("");
		status.setText("Ready");
	}
	
	public String getInputAddress(){
		return input[0].getText();
	}
	public String getInputCity(){
		return input[1].getText();
	}
	public String getInputState(){
		return input[2].getText();
	}
	public String getInputZip(){
		return input[3].getText();
	}
	
	public void setGridBagConstraints(GridBagConstraints c, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int fill, int anchor){
        c.gridx = gridx;
        c.gridy = gridy;
        c.gridwidth = gridwidth;
        c.gridheight = gridheight;
        c.weightx = weightx;
        c.weighty = weighty;
        c.fill = fill;
        c.anchor = anchor;
	}
}
