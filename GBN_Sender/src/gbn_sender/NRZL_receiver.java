/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
public class NRZL_receiver
{
    public String line="";

    NRZL_receiver(String s) throws FileNotFoundException, IOException
    {
        System.out.println(s);
        for(int i=0; i<s.length(); i++)
        {
            char character = s.charAt(i);

            if(character=='+')
                character='1';
            else
                character='0';
            line=line+character;

        }
        System.out.print(line);
    }
}
