/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gbn_sender;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author ABIDA
 */
public class sender_45B {
    File f1= new File("temp_bin.txt");
    FileWriter ftemp_bin = new FileWriter(f1);
    String encode[] = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
    String decode[] = {"11110", "01001", "10100", "10101", "01010", "01011", "01110", "01111", "10010", "10011", "10110", "10111", "11010", "11011", "11100", "11101"};
    sender_45B() throws FileNotFoundException, IOException
    {
        FileReader r = new FileReader("temp1.txt");
        File fout= new File("temp2.txt");
    FileWriter ftemp = new FileWriter(fout);
        int count=0;
        String s="";
        while(true)
        {
            int chr = r.read();
            if(chr != -1)
            {
                s+=(char)chr;
                count++;
                if(count==150)
                {
                    //System.out.println(s);
                    s=application(s);
                    ftemp.write(s);
                    s="";
                    count=0;
                }
            }
            else
            {
                if(s.length()!=0)
                {
                   // System.out.println(s);
                    s=application(s);
                    ftemp.write(s);
                    s="";
                    count=0;
                }
                break;
            }
        }
        ftemp.close();
        ftemp_bin.close();
    }
     String application(String s) throws IOException
    {
        s="A-H"+s;
        return presentation(s);
    }
    String presentation(String s) throws IOException
    {
        s="P-H"+s;
        return session(s);
    }
    String session(String s) throws IOException
    {
        s="S-H"+s;
        return transport(s);
    }
    String transport(String s) throws IOException
    {
        s="T-H"+s;
        return network(s);
    }
    String network(String s) throws IOException
    {
        s="N-H"+s;
        return data_link(s);
    }
    String data_link(String s) throws IOException
    {
        s="D-H"+s+"D-T";
        return physical(s);
    }
    String physical(String s) throws IOException
    {
        s="Ph-H"+s;
        //System.out.println(s);
        return convert_to_binary(s);
    }
    String convert_to_binary(String s)
    {
        byte bytes[]= s.getBytes();
        StringBuilder bin=new StringBuilder();
        for(byte b : bytes)
        {
            int val = b;
            for(int i=0;i<8;i++){
            bin.append((val & 128)==0 ? '0': '1');
            val <<= 1;
            }
        }
         s =bin.toString();
        // System.out.println(s);
        s=encode(s);
        return s;
    }
    String encode(String s)
    {
        String st="",tmp2="";
        for(int i=0;i<=s.length()-4;i+=4)
        {
            String tmp= s.substring(i, i+4);
            for(int j=0;j<16;j++)
            {
                if(tmp.equals(encode[j]))
                {
                  //  System.out.print(i+" "+tmp+" ");
                    tmp2=decode[j];
                  //  System.out.println(tmp);
                    break;
                }
            }
            st+=tmp2;
        }
        return st;
    }
    
    
}
