/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gbn_sender;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class CustomFrame extends JFrame
{

    OptionChooserPanel ocf;
    JButton submit;
    String SorR;
    String error;
    String block;
    String encoding;
    String protocol;
    String FilePath;
    public CustomFrame()
    {
        super("Custom JFrame for Data Communication Lab");
        setLayout(new BorderLayout(5,5));
        ocf = new OptionChooserPanel();
        add(ocf,BorderLayout.CENTER);

        submit = new JButton("SUBMIT");
        submit.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                SorR = (String) ocf.SorRCombo.getSelectedItem();
                error = (String) ocf.errorCombo.getSelectedItem();
                block = "4B/5B";
                encoding = (String) ocf.encodeCombo.getSelectedItem();
                protocol = (String) ocf.protocolCombo.getSelectedItem();
                FilePath = (String)ocf.address.getText();
                System.out.printf("%s %s %s %s\n", error, block, encoding, protocol,FilePath);
                //add call to sender here.

                if(SorR.equals("Sender"))
                {

                    SENDER s = new SENDER(error,block,encoding,protocol,FilePath);
                }
                else  if(SorR.equals("Receiver"))
                {
                    try
                    {
                        RECEIVER r = new RECEIVER(error,block,encoding,protocol,FilePath);
                    }
                    catch (IOException ex)
                    {
                        Logger.getLogger(CustomFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

        });
        add(submit,BorderLayout.SOUTH);
    }
}
