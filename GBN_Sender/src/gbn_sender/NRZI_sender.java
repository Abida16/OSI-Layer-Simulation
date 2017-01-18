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
import java.io.FileInputStream;
import java.io.IOException;

public class NRZI_sender
{
    public String line="";

    NRZI_sender(String s) throws FileNotFoundException, IOException
    {
        String binary2 = "";
        String sudo="+";
        for (int i = 0; i <s.length(); i++)
        {
            char val=s.charAt(i);
            //binary2.append((val & 128) == 0 ? "-" : "+");
            if(val=='0')
            {
                if(sudo.equals("+"))
                {
                    binary2+="+";
                    sudo="+";
                }
                else
                {
                    binary2+="-";
                    sudo ="-";
                }
            }
            else
            {
                if(sudo.equals("+"))
                {
                    binary2+="-";
                    sudo="-";
                }
                else
                {
                    binary2+="+";
                    sudo="+";
                }
            }
        }
        line =binary2;
        //  System.out.print("&&&&"+line+"\n");

    }
}

