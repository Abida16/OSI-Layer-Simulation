/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gbn_receiver;

import java.io.*;

/**
 *
 * @author ABIDA
 */
public class diff_manchester_receiver 
{
    String line="";
    diff_manchester_receiver(String s) throws FileNotFoundException, IOException
    {
        
        char sudo='+';
        String st="";
        int count=0;
       for(int i=0;i<=s.length()-2;i+=2)
        {
            int chr1= s.charAt(i);
           
                int chr2=s.charAt(i+1);
                line+=((sudo==(char)chr1)?'1':'0');
                sudo=(char)chr2;
                
                
            }
        }
}

    

