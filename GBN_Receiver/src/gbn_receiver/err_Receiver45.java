/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gbn_receiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author ABIDA
 */
public class err_Receiver45 {
    Random rn = new Random();
     int c=0,cnt=0,byt=0,pt=0;
    String encode[] = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
    String decode[] = {"11110", "01001", "10100", "10101", "01010", "01011", "01110", "01111", "10010", "10011", "10110", "10111", "11010", "11011", "11100", "11101"};
    err_Receiver45()throws FileNotFoundException, IOException
    {
        
        FileReader r = new FileReader("45B.txt");
        File fw = new File("lab_out.txt");
        File fw2 = new File("rv_temp.txt");
        FileReader r2 = new FileReader("rv_temp.txt");
        FileWriter fout = new FileWriter(fw);
        FileWriter ftemp = new FileWriter(fw2);
        String tmp="",s="",tot_st="";
        int count=0;
        while(true)
        {
            int chr= r.read();
            if(chr!=-1)
            {
                tmp+=(char)chr;
                count++;
                if(count==1750)
                {
                     tmp=ins_error(tmp);
                    String sp=decode(tmp);
                    // System.out.println(convert(s));
                    count=0;
                    tmp="";
                    tot_st+=sp;
                }
            }
            else if(chr==-1)
            {
                if(tmp.length()!=0)
                {
                     tmp=ins_error(tmp);
                    String sp=decode(tmp);
                    // System.out.print(s);
                    count=0;
                    tmp="";
                    tot_st+=sp;
                }
                break;
            }
        }
        ftemp.write(tot_st);
        ftemp.close();
        count=0;
        while(true)
        {
            int chr= r2.read();
            if(chr!=-1)
            {
                s+=(char)chr;
                count++;
                if(count==1400)
                {
                   
                    s=convert(s);

                    fout.write(s);
                    s="";
                    count=0;
                }
            }
            else
            {
                if(s.length()!=0)
                {
                    
                    s=convert(s);

                    fout.write(s);
                }
                break;
            }
        }
      //  System.out.println(cnt+"  "+byt+"  "+pt);
        //System.out.println(c);
        float pr=((float)pt/(float)byt)*(float)100;
        System.out.println(pr);
        fout.close();

    }

    String ins_error(String s)
    {
        for(int p=0;p<=s.length()-5;p+=5){
        int y;
        int x= rn.nextInt(99)+1;
        if(x<5)
        {
            pt++;
            y=rn.nextInt(5);
           // System.out.print(y);
            String tmp=s.substring(p, p+5);
          //  System.out.print("  "+y);
           if(tmp.charAt(y)=='0')
                tmp=tmp.substring(0, y)+'1'+tmp.substring(y+1, tmp.length());
          //  else if(tmp.charAt(y)=='0' && y==0)
            //     tmp='1'+tmp.substring(y+1, tmp.length());
           else if(tmp.charAt(y)=='1' )
                tmp=tmp.substring(0, y)+'0'+tmp.substring(y+1, tmp.length());
          //  else if(tmp.charAt(y)=='1' && y==0)
            //     tmp='0'+tmp.substring(y+1, tmp.length());
             //System.out.println("  "+tmp);
            s=s.substring(0, p)+tmp+s.substring(p+5, s.length());
        }
        
        }
        return s;
    }
   
    String decode(String s)
    {
        String st="",tmp2="";
        for(int i=0; i<=s.length()-5; i+=5)
        {
            byt++;
           c=0;
            String tmp=s.substring(i, i+5);
            for(int j=0; j<16; j++)
            {
                if(tmp.equals(decode[j]))
                {
                    
                    tmp2=encode[j];
                    c++;
                    
                    break;
                }
            }
            st+=tmp2;
            if(c==0)
                cnt++;
        }
        return st;
    }
    String convert(String s)
    {
        String st="";
        for(int i=0; i<=s.length()-8; i+=8)
        {
            int nxtint= Integer.parseInt(s.substring(i, i+8),2);
            st+=(char)nxtint;
        }
         //System.out.println("**********"+st);
        st= physical(st);
        
        return st;
    }
    String emp="";
    String physical(String s)
    {

        // s=convert(s);
        s=s.substring(0, s.length()-3);
        //System.out.println(s);
        s=s.replaceFirst( "Ph-H",emp);
        //System.out.println("*******************"+s);

        return(data_link(s));
    }
    String data_link(String s)
    {
        s=s.replaceFirst( "D-H",emp);

        return(network(s));
    }
    String network(String s)
    {
        s=s.replaceFirst( "N-H",emp);
        return(transport(s));
    }
    String transport(String s)
    {
        s=s.replaceFirst( "T-H",emp);
        return(session(s));
    }
    String session(String s)
    {
        s=s.replaceFirst( "S-H",emp);
        return(presentation(s));
    }
    String presentation(String s)
    {
        s=s.replaceFirst( "P-H",emp);
        return(application(s));
    }
    String application(String s)
    {
        s=s.replaceFirst( "A-H",emp);
        //  System.out.println("***********"+s+"**********");
        return(s);
    }
}
