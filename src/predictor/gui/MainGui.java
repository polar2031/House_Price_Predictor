package predictor.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;

public class MainGui {

	public JFrame frame;
	private JTabbedPane tabbedPaneInput;
	private CardLayout mainCard;
	
	private JTextField txtAddress;
	private JTextField txtCitytown;
	private JTextField txtState;
	private JTextField txtZip;
	private JTextField txtZip_1;
	
	private JButton btnStart;
	private JButton btnCancel;
	private JButton btnRestart;
	private JButton btnRestart_1;
	private JCheckBox chckbxPrice;
	private JCheckBox chckbxFloorSize;
	private JCheckBox chckbxLotSize;
	private JCheckBox chckbxBedroomNumber;
	private JCheckBox chckbxBathroomNumber;
	private JCheckBox chckbxHouseAge;
	private JCheckBox chckbxTransferPrice;
	private JCheckBox chckbxTransferFloorSize;
	private JCheckBox chckbxTransferLotSize;
	private JCheckBox chckbxUseBasePrice;
	private JProgressBar processingProgressBar;
	private JLabel processingStatus;
	private JSpinner sampleNumberSpinner;
	private JComboBox<String> comboBoxMethod;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					MainGui window = new MainGui();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public MainGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e){}
		
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnOther = new JMenu("Other");
		menuBar.add(mnOther);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnOther.add(mntmAbout);
		mainCard = new CardLayout(0, 0);
		frame.getContentPane().setLayout(mainCard);
		
		JPanel panelInput = new JPanel();
		frame.getContentPane().add(panelInput, "panelInput");
		panelInput.setLayout(new BorderLayout(0, 0));
		
		tabbedPaneInput = new JTabbedPane(JTabbedPane.TOP);
		tabbedPaneInput.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelInput.add(tabbedPaneInput, BorderLayout.NORTH);
		
		JPanel panelInputFullAddress = new JPanel();
		panelInputFullAddress.setBorder(new EmptyBorder(5, 10, 5, 10));
		tabbedPaneInput.addTab("Address", null, panelInputFullAddress, null);
		GridBagLayout gbl_panelInputFullAddress = new GridBagLayout();
		gbl_panelInputFullAddress.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelInputFullAddress.rowHeights = new int[]{0, 0, 0};
		gbl_panelInputFullAddress.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panelInputFullAddress.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panelInputFullAddress.setLayout(gbl_panelInputFullAddress);
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setPreferredSize(new Dimension(45, 20));
		lblAddress.setMinimumSize(new Dimension(45, 20));
		lblAddress.setMaximumSize(new Dimension(45, 20));
		GridBagConstraints gbc_lblAddress = new GridBagConstraints();
		gbc_lblAddress.anchor = GridBagConstraints.WEST;
		gbc_lblAddress.insets = new Insets(0, 5, 5, 5);
		gbc_lblAddress.gridx = 0;
		gbc_lblAddress.gridy = 0;
		panelInputFullAddress.add(lblAddress, gbc_lblAddress);
		
		txtAddress = new JTextField();
		txtAddress.setText("405 N Main St");
		GridBagConstraints gbc_txtAddress = new GridBagConstraints();
		gbc_txtAddress.insets = new Insets(5, 5, 5, 5);
		gbc_txtAddress.gridwidth = 11;
		gbc_txtAddress.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAddress.gridx = 1;
		gbc_txtAddress.gridy = 0;
		panelInputFullAddress.add(txtAddress, gbc_txtAddress);
		txtAddress.setColumns(10);
		
		JLabel lblCitytown = new JLabel("City/Town");
		lblCitytown.setPreferredSize(new Dimension(65, 20));
		lblCitytown.setMinimumSize(new Dimension(65, 20));
		lblCitytown.setMaximumSize(new Dimension(65, 20));
		GridBagConstraints gbc_lblCitytown = new GridBagConstraints();
		gbc_lblCitytown.anchor = GridBagConstraints.WEST;
		gbc_lblCitytown.insets = new Insets(5, 5, 5, 5);
		gbc_lblCitytown.gridx = 0;
		gbc_lblCitytown.gridy = 1;
		panelInputFullAddress.add(lblCitytown, gbc_lblCitytown);
		
		txtCitytown = new JTextField();
		txtCitytown.setText("Sharon");
		GridBagConstraints gbc_txtCitytown = new GridBagConstraints();
		gbc_txtCitytown.insets = new Insets(5, 5, 5, 5);
		gbc_txtCitytown.gridwidth = 3;
		gbc_txtCitytown.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCitytown.gridx = 1;
		gbc_txtCitytown.gridy = 1;
		panelInputFullAddress.add(txtCitytown, gbc_txtCitytown);
		txtCitytown.setColumns(10);
		
		JLabel lblState = new JLabel("State");
		GridBagConstraints gbc_lblState = new GridBagConstraints();
		gbc_lblState.insets = new Insets(5, 5, 5, 5);
		gbc_lblState.anchor = GridBagConstraints.WEST;
		gbc_lblState.gridx = 4;
		gbc_lblState.gridy = 1;
		panelInputFullAddress.add(lblState, gbc_lblState);
		
		txtState = new JTextField();
		txtState.setText("MA");
		GridBagConstraints gbc_txtState = new GridBagConstraints();
		gbc_txtState.insets = new Insets(5, 5, 5, 5);
		gbc_txtState.gridwidth = 3;
		gbc_txtState.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtState.gridx = 5;
		gbc_txtState.gridy = 1;
		panelInputFullAddress.add(txtState, gbc_txtState);
		txtState.setColumns(10);
		
		JLabel lblZip = new JLabel("Zip");
		GridBagConstraints gbc_lblZip = new GridBagConstraints();
		gbc_lblZip.insets = new Insets(5, 5, 5, 5);
		gbc_lblZip.anchor = GridBagConstraints.WEST;
		gbc_lblZip.gridx = 8;
		gbc_lblZip.gridy = 1;
		panelInputFullAddress.add(lblZip, gbc_lblZip);
		
		txtZip = new JTextField();
		txtZip.setText("02067");
		GridBagConstraints gbc_txtZip = new GridBagConstraints();
		gbc_txtZip.insets = new Insets(5, 5, 5, 5);
		gbc_txtZip.gridwidth = 3;
		gbc_txtZip.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtZip.gridx = 9;
		gbc_txtZip.gridy = 1;
		panelInputFullAddress.add(txtZip, gbc_txtZip);
		txtZip.setColumns(10);
		
		JPanel panelInputZip = new JPanel();
		panelInputZip.setBorder(new EmptyBorder(5, 10, 5, 10));
		tabbedPaneInput.addTab("City/Town", null, panelInputZip, null);
		GridBagLayout gbl_panelInputZip = new GridBagLayout();
		gbl_panelInputZip.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panelInputZip.rowHeights = new int[]{0, 0, 0};
		gbl_panelInputZip.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelInputZip.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panelInputZip.setLayout(gbl_panelInputZip);
		
		JLabel lblZip_1 = new JLabel("Zip");
		GridBagConstraints gbc_lblZip_1 = new GridBagConstraints();
		gbc_lblZip_1.insets = new Insets(5, 5, 5, 5);
		gbc_lblZip_1.gridx = 0;
		gbc_lblZip_1.gridy = 0;
		panelInputZip.add(lblZip_1, gbc_lblZip_1);
		
		txtZip_1 = new JTextField();
		GridBagConstraints gbc_txtZip_1 = new GridBagConstraints();
		gbc_txtZip_1.insets = new Insets(5, 5, 5, 5);
		gbc_txtZip_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtZip_1.gridx = 1;
		gbc_txtZip_1.gridy = 0;
		panelInputZip.add(txtZip_1, gbc_txtZip_1);
		txtZip_1.setColumns(10);
		
		JLabel lblBlend = new JLabel("Blend");
		lblBlend.setVisible(false);
		GridBagConstraints gbc_lblBlend = new GridBagConstraints();
		gbc_lblBlend.gridwidth = 10;
		gbc_lblBlend.insets = new Insets(0, 0, 0, 5);
		gbc_lblBlend.gridx = 2;
		gbc_lblBlend.gridy = 0;
		panelInputZip.add(lblBlend, gbc_lblBlend);
		
		JPanel panelInputOption = new JPanel();
		panelInput.add(panelInputOption, BorderLayout.CENTER);
		panelInputOption.setLayout(new BorderLayout(0, 0));
		
		JPanel panelMethod = new JPanel();
		panelMethod.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelInputOption.add(panelMethod, BorderLayout.NORTH);
		panelMethod.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblMethod = new JLabel("Method");
		lblMethod.setHorizontalAlignment(SwingConstants.LEFT);
		lblMethod.setBorder(new EmptyBorder(0, 0, 0, 5));
		panelMethod.add(lblMethod);
		
		comboBoxMethod = new JComboBox<String>();
		panelMethod.add(comboBoxMethod);
		comboBoxMethod.addItem("Price Per Square Feet");
		comboBoxMethod.addItem("Linear Regression");
		comboBoxMethod.addItem("Semi-Log (Log-Linear) Regression");
		comboBoxMethod.addItem("Semi-Log (Linear-Log) Regression");
		comboBoxMethod.addItem("Log-Log Regression");
		comboBoxMethod.addItem("Box-Cox Transform Regression");
		comboBoxMethod.addActionListener(new ActionListener() {
			@Override
		    public void actionPerformed(ActionEvent e) {
				int index = comboBoxMethod.getSelectedIndex();
		    	if(index == 5){
		    		chckbxPrice.setVisible(true);
		    		chckbxFloorSize.setVisible(true);
		    		chckbxLotSize.setVisible(true);
		    		chckbxBedroomNumber.setVisible(true);
		    		chckbxBathroomNumber.setVisible(true);
		    		chckbxHouseAge.setVisible(true);
		    		chckbxUseBasePrice.setVisible(true);
		    		chckbxTransferPrice.setVisible(true);
		    		chckbxTransferFloorSize.setVisible(true);
		    		chckbxTransferLotSize.setVisible(true);
		    	}
		    	else if(index == 0){
		    		chckbxPrice.setVisible(false);
		    		chckbxFloorSize.setVisible(false);
		    		chckbxLotSize.setVisible(false);
		    		chckbxBedroomNumber.setVisible(false);
		    		chckbxBathroomNumber.setVisible(false);
		    		chckbxHouseAge.setVisible(false);
		    		chckbxUseBasePrice.setVisible(false);
		    		chckbxTransferPrice.setVisible(false);
		    		chckbxTransferFloorSize.setVisible(false);
		    		chckbxTransferLotSize.setVisible(false);
		    	}
		    	else{
		    		chckbxPrice.setVisible(true);
		    		chckbxFloorSize.setVisible(true);
		    		chckbxLotSize.setVisible(true);
		    		chckbxBedroomNumber.setVisible(true);
		    		chckbxBathroomNumber.setVisible(true);
		    		chckbxHouseAge.setVisible(true);
		    		chckbxUseBasePrice.setVisible(true);
		    		chckbxTransferPrice.setVisible(false);
		    		chckbxTransferFloorSize.setVisible(false);
		    		chckbxTransferLotSize.setVisible(false);
		    	}
		    }
		});
		
		JPanel panelOption = new JPanel();
		panelOption.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelInputOption.add(panelOption, BorderLayout.CENTER);
		GridBagLayout gbl_panelOption = new GridBagLayout();
		gbl_panelOption.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panelOption.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelOption.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelOption.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelOption.setLayout(gbl_panelOption);
		
		JLabel lblOption = new JLabel("Option");
		GridBagConstraints gbc_lblOption = new GridBagConstraints();
		gbc_lblOption.anchor = GridBagConstraints.WEST;
		gbc_lblOption.insets = new Insets(0, 5, 5, 5);
		gbc_lblOption.gridx = 0;
		gbc_lblOption.gridy = 0;
		panelOption.add(lblOption, gbc_lblOption);
		
		chckbxPrice = new JCheckBox("Price");
		chckbxPrice.setVisible(false);
		chckbxPrice.setSelected(true);
		chckbxPrice.setEnabled(false);
		GridBagConstraints gbc_chckbxPrice = new GridBagConstraints();
		gbc_chckbxPrice.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxPrice.anchor = GridBagConstraints.WEST;
		gbc_chckbxPrice.gridx = 0;
		gbc_chckbxPrice.gridy = 1;
		panelOption.add(chckbxPrice, gbc_chckbxPrice);
		
		chckbxTransferPrice = new JCheckBox("Transfer Price");
		chckbxTransferPrice.setVisible(false);
		GridBagConstraints gbc_chckbxTransferPrice = new GridBagConstraints();
		gbc_chckbxTransferPrice.anchor = GridBagConstraints.WEST;
		gbc_chckbxTransferPrice.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxTransferPrice.gridx = 1;
		gbc_chckbxTransferPrice.gridy = 1;
		panelOption.add(chckbxTransferPrice, gbc_chckbxTransferPrice);
		
		chckbxFloorSize = new JCheckBox("Floor Size");
		chckbxFloorSize.setVisible(false);
		chckbxFloorSize.setEnabled(false);
		chckbxFloorSize.setSelected(true);
		GridBagConstraints gbc_chckbxFloorSize = new GridBagConstraints();
		gbc_chckbxFloorSize.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxFloorSize.anchor = GridBagConstraints.WEST;
		gbc_chckbxFloorSize.gridx = 0;
		gbc_chckbxFloorSize.gridy = 2;
		panelOption.add(chckbxFloorSize, gbc_chckbxFloorSize);
		
		chckbxTransferFloorSize = new JCheckBox("Transfer Floor Size");
		chckbxTransferFloorSize.setVisible(false);
		GridBagConstraints gbc_chckbxTransferFloorSize = new GridBagConstraints();
		gbc_chckbxTransferFloorSize.anchor = GridBagConstraints.WEST;
		gbc_chckbxTransferFloorSize.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxTransferFloorSize.gridx = 1;
		gbc_chckbxTransferFloorSize.gridy = 2;
		panelOption.add(chckbxTransferFloorSize, gbc_chckbxTransferFloorSize);
		
		chckbxLotSize = new JCheckBox("Lot Size");
		chckbxLotSize.setVisible(false);
		GridBagConstraints gbc_chckbxLotSize = new GridBagConstraints();
		gbc_chckbxLotSize.anchor = GridBagConstraints.WEST;
		gbc_chckbxLotSize.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxLotSize.gridx = 0;
		gbc_chckbxLotSize.gridy = 3;
		panelOption.add(chckbxLotSize, gbc_chckbxLotSize);
		
		chckbxTransferLotSize = new JCheckBox("Transfer Lot Size");
		chckbxTransferLotSize.setVisible(false);
		GridBagConstraints gbc_chckbxTransferLotSize = new GridBagConstraints();
		gbc_chckbxTransferLotSize.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxTransferLotSize.anchor = GridBagConstraints.WEST;
		gbc_chckbxTransferLotSize.gridx = 1;
		gbc_chckbxTransferLotSize.gridy = 3;
		panelOption.add(chckbxTransferLotSize, gbc_chckbxTransferLotSize);
		
		JLabel lblBlend_1 = new JLabel(" ");
		GridBagConstraints gbc_lblBlend_1 = new GridBagConstraints();
		gbc_lblBlend_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblBlend_1.gridx = 0;
		gbc_lblBlend_1.gridy = 4;
		panelOption.add(lblBlend_1, gbc_lblBlend_1);
		
		chckbxBedroomNumber = new JCheckBox("Bedroom Number");
		chckbxBedroomNumber.setVisible(false);
		GridBagConstraints gbc_chckbxBedroomNumber = new GridBagConstraints();
		gbc_chckbxBedroomNumber.anchor = GridBagConstraints.WEST;
		gbc_chckbxBedroomNumber.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxBedroomNumber.gridx = 0;
		gbc_chckbxBedroomNumber.gridy = 5;
		panelOption.add(chckbxBedroomNumber, gbc_chckbxBedroomNumber);
		
		chckbxBathroomNumber = new JCheckBox("Bathroom Number");
		chckbxBathroomNumber.setVisible(false);
		GridBagConstraints gbc_chckbxBathroomNumber = new GridBagConstraints();
		gbc_chckbxBathroomNumber.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxBathroomNumber.anchor = GridBagConstraints.WEST;
		gbc_chckbxBathroomNumber.gridx = 1;
		gbc_chckbxBathroomNumber.gridy = 5;
		panelOption.add(chckbxBathroomNumber, gbc_chckbxBathroomNumber);
		
		chckbxHouseAge = new JCheckBox("House Age");
		chckbxHouseAge.setVisible(false);
		GridBagConstraints gbc_chckbxHouseAge = new GridBagConstraints();
		gbc_chckbxHouseAge.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxHouseAge.anchor = GridBagConstraints.WEST;
		gbc_chckbxHouseAge.gridx = 2;
		gbc_chckbxHouseAge.gridy = 5;
		panelOption.add(chckbxHouseAge, gbc_chckbxHouseAge);
		
		chckbxUseBasePrice = new JCheckBox("Use Base Price on Regression");
		chckbxUseBasePrice.setVisible(false);
		GridBagConstraints gbc_chckbxUseBasePrice = new GridBagConstraints();
		gbc_chckbxUseBasePrice.anchor = GridBagConstraints.WEST;
		gbc_chckbxUseBasePrice.gridwidth = 2;
		gbc_chckbxUseBasePrice.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxUseBasePrice.gridx = 0;
		gbc_chckbxUseBasePrice.gridy = 6;
		panelOption.add(chckbxUseBasePrice, gbc_chckbxUseBasePrice);
		
		JLabel lblSampleNumber = new JLabel("Sample Number");
		GridBagConstraints gbc_lblSampleNumber = new GridBagConstraints();
		gbc_lblSampleNumber.anchor = GridBagConstraints.WEST;
		gbc_lblSampleNumber.insets = new Insets(0, 5, 0, 5);
		gbc_lblSampleNumber.gridx = 0;
		gbc_lblSampleNumber.gridy = 7;
		panelOption.add(lblSampleNumber, gbc_lblSampleNumber);
		
		sampleNumberSpinner = new JSpinner();
		sampleNumberSpinner.setModel(new SpinnerNumberModel(1, 1, 200, 1));
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.anchor = GridBagConstraints.WEST;
		gbc_spinner.insets = new Insets(0, 0, 0, 5);
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 7;
		panelOption.add(sampleNumberSpinner, gbc_spinner);
		
		JPanel panelStartButton = new JPanel();
		panelInputOption.add(panelStartButton, BorderLayout.SOUTH);
		
		btnStart = new JButton("Start");
		panelStartButton.add(btnStart);
		
		JPanel panelProcessing = new JPanel();
		panelProcessing.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.getContentPane().add(panelProcessing, "panelProcessing");
		GridBagLayout gbl_panelProcessing = new GridBagLayout();
		gbl_panelProcessing.columnWidths = new int[]{0, 0};
		gbl_panelProcessing.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panelProcessing.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelProcessing.rowWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		panelProcessing.setLayout(gbl_panelProcessing);
		
		processingStatus = new JLabel("Processing...");
		GridBagConstraints gbc_lblProcessing = new GridBagConstraints();
		gbc_lblProcessing.weighty = 2.0;
		gbc_lblProcessing.anchor = GridBagConstraints.SOUTH;
		gbc_lblProcessing.insets = new Insets(0, 0, 5, 0);
		gbc_lblProcessing.gridx = 0;
		gbc_lblProcessing.gridy = 0;
		panelProcessing.add(processingStatus, gbc_lblProcessing);
		
		processingProgressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.weighty = 1.0;
		gbc_progressBar.insets = new Insets(0, 10, 5, 10);
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 1;
		panelProcessing.add(processingProgressBar, gbc_progressBar);
		
		btnCancel = new JButton("Cancel");

		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.weighty = 2.0;
		gbc_btnCancel.anchor = GridBagConstraints.NORTH;
		gbc_btnCancel.gridx = 0;
		gbc_btnCancel.gridy = 2;
		panelProcessing.add(btnCancel, gbc_btnCancel);
		
		JPanel panelResult = new JPanel();
		frame.getContentPane().add(panelResult, "panelResult");
		panelResult.setLayout(new BorderLayout(0, 0));
		
		JPanel panelResultRestart = new JPanel();
		panelResult.add(panelResultRestart, BorderLayout.SOUTH);
		
		btnRestart = new JButton("Restart");
		panelResultRestart.add(btnRestart);
		
		JPanel panelAreaResult = new JPanel();
		frame.getContentPane().add(panelAreaResult, "panelAreaResult");
		panelAreaResult.setLayout(new BorderLayout(0, 0));
		
		JPanel panelAreaResultRestart = new JPanel();
		panelAreaResult.add(panelAreaResultRestart, BorderLayout.SOUTH);
		
		btnRestart_1 = new JButton("Restart");
		panelAreaResultRestart.add(btnRestart_1);
	}
	
    public void showInputCard() {
    	mainCard.show(frame.getContentPane(), "panelInput");
    }
    public void showProcessingCard() {
    	mainCard.show(frame.getContentPane(), "panelProcessing");
    }
    public void showResultCard() {
    	mainCard.show(frame.getContentPane(), "panelResult");
    }
    public void showAreaResultCard() {
    	mainCard.show(frame.getContentPane(), "panelAreaResult");
    }
	
	
	
	public void addStartListener(ActionListener a){
    	btnStart.addActionListener(a);
    }
	public void addCancelListener(ActionListener a){
    	btnCancel.addActionListener(a);
    }
	public void addRestartListener(ActionListener a){
		btnRestart.addActionListener(a);
		btnRestart_1.addActionListener(a);
    }
	
	public int getSelectedMode() {
		return tabbedPaneInput.getSelectedIndex();
	}
	
    public String getInputAddress() {
    	return txtAddress.getText();
    }
    public String getInputCity() {
    	return txtCitytown.getText();
    }
    public String getInputState() {
    	return txtState.getText();
    }
    public String getInputZip() {
    	return txtZip.getText();
    }
    public String getInputAreaZip() {
    	return txtZip_1.getText();
    }
    
    
    
	public boolean[] getVariableOption() {
		boolean[] o = new boolean[6];
		o[0] = chckbxFloorSize.isSelected();
		o[1] = chckbxLotSize.isSelected();
		o[2] = chckbxBedroomNumber.isSelected();
		o[3] = chckbxBathroomNumber.isSelected();
		o[4] = chckbxHouseAge.isSelected();
		o[5] = chckbxUseBasePrice.isSelected();
		return o;
	}
	
	public boolean[] getTransferOption() {
		boolean[] o = new boolean[6];
		o[1] = chckbxTransferFloorSize.isSelected();
		o[2] = chckbxTransferLotSize.isSelected();
		return o;
	}
	
	public JCheckBox getChckbxPrice() {
		return chckbxPrice;
	}
	public JCheckBox getChckbxFloorSize() {
		return chckbxFloorSize;
	}
	public JCheckBox getChckbxLotSize() {
		return chckbxLotSize;
	}
	public JCheckBox getChckbxBedroomNumber() {
		return chckbxBedroomNumber;
	}
	public JCheckBox getChckbxBathroomNumber() {
		return chckbxBathroomNumber;
	}
	public JCheckBox getChckbxHouseAge() {
		return chckbxHouseAge;
	}
	public JCheckBox getChckbxUseBasePrice() {
		return chckbxUseBasePrice;
	}
	public JCheckBox getChckbxTransferPrice() {
		return chckbxTransferPrice;
	}
	public JCheckBox getChckbxTransferFloorSize() {
		return chckbxTransferFloorSize;
	}
	public JCheckBox getChckbxTransferLotSize() {
		return chckbxTransferLotSize;
	}
	public int getSampleNumber() {
		return (int)sampleNumberSpinner.getValue();
	}
	public boolean[] getVariableOptions() {
		boolean[] o = new boolean[6];
		o[0] = chckbxFloorSize.isSelected();
		o[1] = chckbxLotSize.isSelected();
		o[2] = chckbxBedroomNumber.isSelected();
		o[3] = chckbxBathroomNumber.isSelected();
		o[4] = chckbxHouseAge.isSelected();
		o[5] = chckbxUseBasePrice.isSelected();
		return o;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}
	
	public void setStatusText(String s){
		processingStatus.setText(s);
	}
	public void setProgressBar(int progress){
		processingProgressBar.setValue(progress);
	}
	
	public JComboBox<String> getComboBoxMethod() {
		return comboBoxMethod;
	}
	public int getComboBoxMethodIndex() {
		return comboBoxMethod.getSelectedIndex();
	}
}
