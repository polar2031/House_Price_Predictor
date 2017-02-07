import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class GUI {
	private JFrame f;
	private JTextField input[];
    private JTextArea output;
    // Input columns' name
    String name[] = {"Address",
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
        f.setSize(1200, 800);
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
        c.weighty = 0.25;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        f.add(p1, c);

        // set input area
        GridBagConstraints c1 = new GridBagConstraints();
        input = new JTextField[name.length];
        for(int i = 0; i < name.length; i++){
            JLabel l1_1 = new JLabel(name[i]);
            c1.gridx = 0;
            c1.gridy = i;
            c1.gridwidth = 1;
            c1.gridheight = 1;
            c1.weightx = 1;
            c1.weighty = 0;
            c1.fill = GridBagConstraints.NONE;
            c1.anchor = GridBagConstraints.WEST;
            p1.add(l1_1, c1);

            JTextField t1_1 = new JTextField();
            c1.gridx = 1;
            c1.gridy = i;
            c1.gridwidth = 10;
            c1.gridheight = 1;
            c1.weightx = 10;
            c1.weighty = 0;
            c1.fill = GridBagConstraints.BOTH;
            c1.anchor = GridBagConstraints.WEST;
            p1.add(t1_1, c1);

            input[i] = t1_1;
        }

        // set default text in input columns
        input[0].setText("34 Longmeadow Ln");
        input[1].setText("Town of Sharon");
        input[2].setText("ma");
        input[3].setText("02067");
        // input[4].setText("2116");
        // input[5].setText("3");
        // input[6].setText("2");


        // set button area
        JPanel p2 = new JPanel();
        p2.setLayout(new GridBagLayout());
		p2.setBorder(new EmptyBorder(10,20,10,20));
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0.05;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.EAST;
        f.add(p2, c);

        JButton b2_1 = new JButton("start");
        p2.add(b2_1);
        b2_1.addActionListener(new StartLitener());

        // set output area
        JPanel p3 = new JPanel();
        p3.setLayout(new BorderLayout(5, 5));
		p3.setBorder(new EmptyBorder(0,20,10,20));
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.WEST;
        f.add(p3, c);


        JTextArea t3_1 = new JTextArea(5, 20);
        //t3_1.setEditable(false);
        p3.add(new JScrollPane(t3_1));
        output = t3_1;
        PrintStream printStream = new PrintStream(new CustomOutputStream(output));
        System.setOut(printStream);
        // System.setErr(printStream);

        // turn frame visible
		f.setVisible(true);
	}

    class StartLitener implements ActionListener{
        public void actionPerformed(ActionEvent event) {
            // build target object

            House h = new House(input[0].getText(),
                                input[1].getText(),
                                input[2].getText(),
                                input[3].getText());
			PricePredictor p = new PricePredictor();
			p.start(h);


        }
    }
}
