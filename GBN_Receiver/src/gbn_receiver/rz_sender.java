

package gbn_receiver;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.IOException;

public class rz_sender
{
    public String line="";
    rz_sender(String s) throws FileNotFoundException, IOException
    {
        int p;
        String volS="";
        String volS2="";

       
        for (int i = 0; i <s.length(); i++)
        {
         char val=s.charAt(i);
        if(val =='0')
        {
            volS=volS+"0";
            volS=volS+"-";
        }
        else
        {
            volS=volS+"+";
            volS=volS+"0";
        }
        }
        line = volS;
        
    }
}
