/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gbn_receiver;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class OptionChooserPanel extends JPanel {
	
	private JPanel fileAddressPanel;
	protected JTextArea address;
	private JButton addressSelect;
	private JFileChooser flc = new JFileChooser();
	
	protected JComboBox SorRCombo;
	private String[] SorRList = {"Sender" , "Receiver"};
	
	protected JComboBox errorCombo;
	private String[] errorList = {"Hamming" , "CRC-16"};
	
	protected JComboBox protocolCombo;
	private String[] protocolList = {"Selective Repeat","go-back-N" };
	
	protected JComboBox blockCombo;
	private String[] blockList = {"4B/5B" , "8B/10B"};
	
	protected JComboBox encodeCombo;
	private String[] encodeList = {"NRZ-L" , "NRZ-I", "RZ", "Manchester", "Diff. Manch"};
	
	
	
	public OptionChooserPanel(){
		setLayout( new GridLayout(6,2));
		SorRChooser();
		addFileChooser();
		addErrorChooser();
		addProtocolChooser();
		addBlockChooser();
		addEncodingChooser();
	}
	private void SorRChooser(){
		add(new JLabel("Sender/Receiver: " ));
		
		SorRCombo = new JComboBox(SorRList);
		SorRCombo.setMaximumRowCount(SorRList.length);
		add(SorRCombo);
	}
	private void addFileChooser(){
		add(new JLabel("File: " ));
		fileAddressPanel = new JPanel();
		fileAddressPanel.setLayout(new GridLayout(1,2));
		address = new JTextArea();
		
		addressSelect = new JButton();
		addressSelect.setText("Select");
		address.setEditable(false);
		address.setLineWrap(true);
		addressSelect.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = flc.showOpenDialog(null);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	               java.io.File file = flc.getSelectedFile();
	               address.setText(file.getAbsolutePath());
	            }
	            else{
	               System.err.println("Cancelled by user");      
	            }
				
			}
			
		});
		fileAddressPanel.add(address);
		fileAddressPanel.add(addressSelect);
		add(fileAddressPanel);
	}
	private void addErrorChooser(){
		add(new JLabel("Error: "));
		
		errorCombo = new JComboBox(errorList);
		errorCombo.setMaximumRowCount(errorList.length);
		add(errorCombo);
		
	}
	private void addProtocolChooser(){
		add(new JLabel("Protocol: "));
		
		protocolCombo = new JComboBox(protocolList);
		protocolCombo.setMaximumRowCount(protocolList.length);
		add(protocolCombo);
	}
	private void addBlockChooser(){
		add(new JLabel("Block Coding: "));
		
		blockCombo = new JComboBox(blockList);
		blockCombo.setMaximumRowCount(blockList.length);
		add(blockCombo);
	}
	private void addEncodingChooser(){
		add(new JLabel("Encoding: "));
		
		encodeCombo = new JComboBox(encodeList);
		encodeCombo.setMaximumRowCount(encodeList.length);
		add(encodeCombo);
	}
}
