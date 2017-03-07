import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class GUI{
	public JFrame f;
	public JTextField input[];
    public JLabel status;
    public JButton startButton;

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
								"predict Price"
							}

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
		p1.setBorder(new EmptyBorder(20,20,0,20));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
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
			c1.gridwidth = 5;
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
			c1.gridx = 2;
			c1.gridy = 1;
			c1.gridwidth = 1;
			c1.gridheight = 1;
			c1.weightx = 0.2;
			c1.weighty = 0;
			c1.fill = GridBagConstraints.NONE;
			c1.anchor = GridBagConstraints.CENTER;
			p1.add(l1_3, c1);

			JTextField t1_3 = new JTextField();
			c1.gridx = 3;
			c1.gridy = 1;
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
			c1.gridx = 4;
			c1.gridy = 1;
			c1.gridwidth = 1;
			c1.gridheight = 1;
			c1.weightx = 0.2;
			c1.weighty = 0;
			c1.fill = GridBagConstraints.NONE;
			c1.anchor = GridBagConstraints.CENTER;
			p1.add(l1_4, c1);

			JTextField t1_4 = new JTextField();
			c1.gridx = 5;
			c1.gridy = 1;
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
	        input[1].setText("Sharon");
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
        c.weighty = 0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        f.add(p2, c);

	        JButton b2_1 = new JButton("start");
			startButton = b2_1;
	        p2.add(b2_1);

			JButton b2_2 = new JButton("cancel");
			p2.add(b2_2);

        // set output area
        JPanel p3 = new JPanel();
        p3.setLayout(new GridBagLayout());
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

			GridBagConstraints c3 = new GridBagConstraints();

			JLabel l3_1 = new JLabel("Floor Size: ");
			c3.gridx = 0;
			c3.gridy = 0;
			c3.gridwidth = 1;
			c3.gridheight = 1;
			c3.weightx = 0.2;
			c3.weighty = 0;
			c3.fill = GridBagConstraints.NONE;
			c3.anchor = GridBagConstraints.CENTER;
			p3.add(l3_1, c3);

		JPanel p4 = new JPanel();
        p4.setLayout(new GridBagLayout());
		p4.setBorder(new EmptyBorder(0,10,10,10));
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.WEST;
        f.add(p4, c);

			GridBagConstraints c4 = new GridBagConstraints();

	        JLabel l4_1 = new JLabel("Ready");
			// l4_1.setBorder(BorderFactory.createLoweredBevelBorder());
			c4.gridx = 0;
			c4.gridy = 0;
			c4.gridwidth = 1;
			c4.gridheight = 1;
			c4.weightx = 1;
			c4.weighty = 1;
			c4.fill = GridBagConstraints.BOTH;
			c4.anchor = GridBagConstraints.WEST;
	        p4.add(l4_1, c4);
	        status = l4_1;

	}

    public void addStartListener(ActionListener start){
        startButton.addActionListener(start);
    }
    public void addCancelListener(ActionListener cancel){}


    public void show(){
        f.setVisible(true);
    }
}
