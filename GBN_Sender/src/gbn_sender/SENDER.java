/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gbn_sender;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SENDER
{
    Random rn = new Random();
    	protected ArrayList<String> frames;

    // static String fileNameInput = "in.txt";
   // static int PORT = 3000;
    String encode[] = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
    String decode[] = {"11110", "01001", "10100", "10101", "01010", "01011", "01110", "01111", "10010", "10011", "10110", "10111", "11010", "11011", "11100", "11101"};
    SENDER(String error, String block,String encoding,String protocol,String File)
    {

        /*(ServerSocket serverSocket = null;
        Socket socket = null;

        try
        {

            serverSocket = new ServerSocket(PORT);
            socket = serverSocket.accept();

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }*/
frames = new ArrayList<String>();
        String chunk = "";
        try
        {
            FileReader fileReader = new FileReader("in.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
           // DataOutputStream out = null;
          /*  try
            {
                out = new DataOutputStream(socket.getOutputStream());
            }
            catch (IOException e)
            {
                System.out.println(e);
            }*/

            int done = 0, read = 0;
            while (true)
            {
                for (int i = 0; i < 138; i++)
                {
                    read = bufferedReader.read();
                    if( read == -1 )
                    {
                        done=1;
                    
                        break;
                    }
                    else {chunk += (char) read;}
                }

                if(chunk.length()!=0)
                {
                    chunk = application(chunk);
                    

                    //if(block.equals("4B/5B"))
                     chunk=encode(chunk);
                    if(error.equals("Hamming"))
                        chunk=Hamming(chunk);


                    else if(error.equals("CRC-16"))
                        chunk=CRC(chunk);
                      chunk = ins_error(chunk);

                 
                    // System.out.println(chunk);
                    if(encoding.equals("NRZ-L"))
                    {
                        NRZL_sender en1 = new NRZL_sender(chunk);
                        chunk=en1.line;
                        // System.out.println(chunk);
                    }
                    if(encoding.equals("NRZ-I"))
                    {
                        NRZI_sender en2 = new NRZI_sender(chunk);
                        chunk=en2.line;
                    }

                    if(encoding.equals("RZ"))
                    {
                        rz_sender en3 = new rz_sender(chunk);
                        chunk=en3.line;
                    }
                    if(encoding.equals("Manchester"))
                    {
                        manch_sender en4 = new manch_sender(chunk);
                        chunk=en4.line;
                    }
                    if(encoding.equals("Diff. Manch"))
                    {
                        diff_manchester_sender en5 = new diff_manchester_sender(chunk);
                        chunk=en5.line;
                    }
                       
                    frames.add(chunk);
                     
                    chunk = "";
                    
                }
                if (done == 1)
                    {
                       //  System.out.println(frames.size());
                         if(protocol.equals("go-back-N"))
                       new send_by_gbn(frames);
                         else{
                           System.out.println(frames.size());
                               new send_by_sr(frames);
                         }
                        break;
                        
                        //break;
                    }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    String ins_error(String s)
    {
        String st="";
        int x= rn.nextInt(99)+1;
        st=s;
        if(x<20)
        {

            int  y=rn.nextInt(2);
            // System.out.print(y);
            if(s.charAt(y)=='1')
                st = s.substring(0, y)+'0'+s.substring(y+1,s.length());
            else if(s.charAt(y)=='0')
                st = s.substring(0, y)+'1'+s.substring(y+1,s.length());

        }
        s=st;
        return s;
    }
    String application(String s) throws IOException
    {
        s="A-H"+s;
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
        s="D-H"+s;
        return physical(s);
    }
    String physical(String s) throws IOException
    {
       // System.out.println(s);
        return convert_to_binary(s);
    }
    String convert_to_binary(String s)
    {
        byte bytes[]= s.getBytes();
        StringBuilder bin=new StringBuilder();
for(byte b : bytes)
        {
            int val = b;
            for(int i=0; i<8; i++)
            {
                bin.append((val & 128)==0 ? '0': '1');
                val <<= 1;
            }
        }
        s =bin.toString();
        //System.out.println(s);
        //s=encode(s);
        return s;
    }
    String Hamming(String s)
    {
        String st="",tmp2="";
        for(int i=0; i<=s.length()-4; i+=4)
        {
            String tmp= s.substring(i, i+4);
            // for(int j=0;j<16;j++)
            //{
            int r0=(tmp.charAt(1)-48)^(tmp.charAt(2)-48)^(tmp.charAt(3)-48);
            int r1= (tmp.charAt(0)-48)^(tmp.charAt(1)-48)^(tmp.charAt(2)-48);
            int r2= (tmp.charAt(2)-48)^(tmp.charAt(3)-48)^(tmp.charAt(0)-48);
            tmp2=tmp+(char)(r2+48)+(char)(r1+48)+(char)(r0+48);

            //System.out.println(tmp+" "+tmp2);
            //}
            st+=tmp2;
        }
        // st=encode(st);
        return st;
    }
    String encode(String s)
    {
        String st="",tmp2="";
        for(int i=0; i<=s.length()-4; i+=4)
        {
            String tmp= s.substring(i, i+4);
            for(int j=0; j<16; j++)
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
        // st=st+s.substring(s.length()-3,s.length());
        return st;
    }
    String CRC(String s)
    {
        char[] divisor= {'1','0','0','0','1','0','0','0','0','0','0','1','0','0','0','0','1'};

        String st=s;
        s=s+"0000000000000000";
        //String tmp= s.substring(0, 17);
        for(int i=0; i<=s.length()-17; i++)
        {
            String tmp= s.substring(i, 17+i);
            String tmp2="";
            if(tmp.charAt(0)=='0')
            {
                continue;
            }
            //  System.out.println("***"+tmp);
            for(int j=0; j<17; j++)
            {
                int x = (tmp.charAt(j)-48) ^ (divisor[j]-48);
                tmp2+=(char)(x+48);
                //System.out.print(x+" ");
            }
            //  System.out.println();
            s=s.substring(0,i)+tmp2+s.substring(i+17, s.length());
            //    System.out.println(tmp2);
        }
        //  System.out.println(s);
        st=st+s.substring(s.length()-16,s.length());
         System.out.println(st.length());
        return st;
    }
}

