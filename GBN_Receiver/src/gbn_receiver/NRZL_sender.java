package gbn_receiver;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.IOException;

public class NRZL_sender
{
     public String line="";
    NRZL_sender(String s){
  
        String binary2 = new String();
          for (int i = 0; i <s.length(); i++)
        {
             char val=s.charAt(i);
          if(val=='0')
               line+='-';
           else
               line+='+';
        }

      
    }
}
