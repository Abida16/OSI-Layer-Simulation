/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
public class manch_receiver
{
    public String line="";
    
    manch_receiver(String s) throws FileNotFoundException, IOException
    {
       String bin = "";
        int len = s.length();
        for (int i = 0; i <= len - 2; i += 2)
        {
                if (s.substring(i, i + 2).equals("+-") == true)
                        bin += '0';
                else
                        bin += '1';
        }
        line = bin;
    }
}
