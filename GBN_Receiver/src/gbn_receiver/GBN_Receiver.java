/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gbn_receiver;

import javax.swing.JFrame;

/**
 *
 * @author Abida
 */
public class GBN_Receiver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CustomFrame cf = new CustomFrame();
		cf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cf.setSize(800,600);
		cf.setVisible(true);
    }
    
}
