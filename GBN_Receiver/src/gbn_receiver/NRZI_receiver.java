/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gbn_receiver;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import java.util.Random;
public class NRZI_receiver
{
    public String line="";
    
    NRZI_receiver(String s) throws FileNotFoundException, IOException
    {
         char sudo='+';
        for(int i=0;i<s.length();i++)
        {
            char ch = s.charAt(i);
            
                if(ch==sudo)
                    ch='0';
                else
                {
                    sudo=ch;
                    ch='1';

                }
                line=line+ch;
        }
               
    }
}

