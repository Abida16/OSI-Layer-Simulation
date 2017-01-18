/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gbn_sender;

import java.io.*;

/**
 *
 * @author ABIDA
 */
public class diff_manchester_sender
{
    public String line="";
    diff_manchester_sender(String s) throws IOException
    {

        // StringBuilder bin = new StringBuilder();


        char sudo='+';
        for(int i=0; i<s.length(); i++)
        {
            if(s.charAt(i)=='1')
            {
                line+=(sudo=='+'?"+-":"-+");
                sudo= (sudo=='+'?'-':'+');
            }
            else if(s.charAt(i)=='0')
            {
                line+=(sudo=='+'?"-+":"+-");
                sudo= (sudo=='+'?'+':'-');
            }
        }

    }
}
