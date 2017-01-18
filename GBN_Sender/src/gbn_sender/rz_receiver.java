/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gbn_sender;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import java.util.Random;
public class rz_receiver
{
    public String line="";

    rz_receiver(String s) throws FileNotFoundException, IOException
    {
        char chr;
        for(int i=0; i<s.length(); i++)
        {
            char ch1 = s.charAt(i);
            char ch2 = s.charAt(i+1);
            i++;

            if(ch1=='+')
                chr='1';
            else
                chr='0';
            line=line+chr;
        }
    }

}
