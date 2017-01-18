/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gbn_receiver;
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
public class RECEIVER
{
    //  static String HOME = "192.168.1.167";
    static String HOME = "127.0.0.1"; //there is no place like this
    static int PORT = 3000;
    private DataOutputStream out;

    //static String fileNameOutput = "output.txt";
    static FileWriter fileWriter = null;
    static BufferedWriter bufferedWriter = null;
    Random rnd = new Random();
    static DataInputStream in = null;
    int sizeOfBuffer(String error, String block,String encoding,String protocol)
    {
        if(error.equals("Hamming") )
        {
            if( encoding.equals("NRZ-L") ||encoding.equals("NRZ-I") )
                return 2625+32;
            else
                return 5250+32;
        }
        else
        {
            if( encoding.equals("NRZ-L") ||encoding.equals("NRZ-I") )
                // return 1520;
                return 1516+32;
            else return 3032+32;
            //return 3040;
        }

    }
    RECEIVER(String error, String block,String encoding,String protocol,String File) throws IOException
    {
        Socket socket = null;
        try {
            socket = new Socket(HOME, PORT);
            in = new DataInputStream(socket.getInputStream());
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        try
        {
            out = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        try {
            fileWriter = new FileWriter("output.txt");
            bufferedWriter = new BufferedWriter(fileWriter);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        int Buffsize=sizeOfBuffer(error, block, encoding, protocol);
        int rn=0,flag=0;
        while (true)
        {
            String chunk = "";
            int read = 0, done = 0;

            for (int i = 0; i <Buffsize; i++)
            {
                read = in.read();
                if(i==0 && read<0 || read>127)
                {
                    flag=1;
                    break;
                }
                else  if(read<0 || read>127)
                    chunk+=" ";
                chunk += (char) read;
            }

            if(flag==1)
                break;
            System.out.println(rn+"**** "+chunk);
            try{
            int seqNo = Integer.parseInt(chunk.substring(0,32), 2);
            if(seqNo != rn)
            {
                System.err.println("Wrong frame: "+seqNo);
                System.err.println("Looking for: "+rn);
                continue;
            }
            chunk= chunk.substring(32,chunk.length());
            }
            catch(Exception e)
            {
                //  out.writeBytes((rn-1) + "\n");
              //  out.writeBytes("EOI");
                break;
            }
            System.err.println("About to ACK: "+(rn));
        try{
            if(rnd.nextInt(100)>20)
            {
                System.err.println("Sending Ack: "+(rn));
                out.writeBytes((rn) + "\n");
               // System.err.println("Sending Ack: "+(rn-1));
            }

            rn++;
}catch(Exception e)
{
     if(encoding.equals("NRZ-L"))  //encoding
            {
                NRZL_receiver en1 = new NRZL_receiver(chunk);
                chunk=en1.line;
            }
            if(encoding.equals("NRZ-I"))
            {
                NRZI_receiver en2 = new NRZI_receiver(chunk);
                chunk=en2.line;
            }

            if(encoding.equals("RZ"))
            {
                rz_receiver en3 = new rz_receiver(chunk);
                chunk=en3.line;
            }
            if(encoding.equals("Manchester"))
            {
                manch_receiver en4 = new manch_receiver(chunk);
                chunk=en4.line;
            }
            if(encoding.equals("Diff. Manch"))
            {
                diff_manchester_receiver en5 = new diff_manchester_receiver(chunk);
                chunk=en5.line;
            }
            if(error.equals("Hamming"))
                chunk = hamming(chunk);
            else if(error.equals("CRC-16"))
                chunk = CRC(chunk);
            chunk=block_decode(chunk);
            chunk= convert_to_char(chunk);
            chunk= physical(chunk);
            try
            {
                bufferedWriter.write(chunk);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
    break;
}
            if(encoding.equals("NRZ-L"))  //encoding
            {
                NRZL_receiver en1 = new NRZL_receiver(chunk);
                chunk=en1.line;
            }
            if(encoding.equals("NRZ-I"))
            {
                NRZI_receiver en2 = new NRZI_receiver(chunk);
                chunk=en2.line;
            }

            if(encoding.equals("RZ"))
            {
                rz_receiver en3 = new rz_receiver(chunk);
                chunk=en3.line;
            }
            if(encoding.equals("Manchester"))
            {
                manch_receiver en4 = new manch_receiver(chunk);
                chunk=en4.line;
            }
            if(encoding.equals("Diff. Manch"))
            {
                diff_manchester_receiver en5 = new diff_manchester_receiver(chunk);
                chunk=en5.line;
            }
            if(error.equals("Hamming"))
                chunk = hamming(chunk);
            else if(error.equals("CRC-16"))
                chunk = CRC(chunk);
            chunk=block_decode(chunk);
            chunk= convert_to_char(chunk);
            chunk= physical(chunk);
            try
            {
                bufferedWriter.write(chunk);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }

        }

        try {
            //System.out.println("Stop please ");
            bufferedWriter.flush();
            bufferedWriter.close();
              socket.close();

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

    }
    String convert_to_char(String s)
    {
        String st="";
        for(int i=0; i<=s.length()-8; i+=8)
        {
            int nxtint= Integer.parseInt(s.substring(i, i+8),2);
            st+=(char)nxtint;
        }
        //System.out.println("**********"+st);
        //st= physical(st);

        return st;
    }
    String emp="";
    String physical(String s)
    {


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
        return(application(s));
    }

    String application(String s)
    {
        s=s.replaceFirst( "A-H",emp);
        //  System.out.println("***********"+s+"**********");
        return(s);
    }

    String block_decode(String s)
    {
        String encode[] = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
        String decode[] = {"11110", "01001", "10100", "10101", "01010", "01011", "01110", "01111", "10010", "10011", "10110", "10111", "11010", "11011", "11100", "11101"};
        String st="",tmp2="";
        for(int i=0; i<=s.length()-5; i+=5 )
        {
            String tmp=s.substring(i, i+5);
            for(int j=0; j<16; j++)
            {
                if(tmp.equals(decode[j]))
                {

                    tmp2=encode[j];

                    break;
                }
            }
            st+=tmp2;
        }
        return st;
    }

    String CRC(String s)
    {
        String st=s;
        char[] divisor= {'1','0','0','0','1','0','0','0','0','0','0','1','0','0','0','0','1'};
        //s=s+"0000000000000000";
        //String tmp= s.substring(0, 17);
        for(int i=0; i<=s.length()-17; i++)
        {
            String tmp= s.substring(i, 17+i);
            String tmp2="";
            if(tmp.charAt(0)=='0')
            {
                // System.out.println("***"+tmp);
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
        System.out.println(s);
        if(s.substring(s.length()-16,s.length()).equals("0000000000000000"))
            st=st+s.substring(s.length()-16,s.length());

        else
            st="101001010110100101011010010101";
        return st;
    }
    String hamming(String s)
    {
        String st="",tmp2="",tmps="";
        int synd[]= {-1,6,5,1,4,3,0,2};
        int oe=0;
        int c1=0;
        String tmp="";
        for(int i=0; i<=s.length()-7; i+=7)
        {
            tmp=s.substring(i, i+7);
            tmps="";
            int s0= (tmp.charAt(1)-48) ^(tmp.charAt(2)-48) ^(tmp.charAt(3)-48) ^(tmp.charAt(6)-48)  ;
            int s1= (tmp.charAt(0)-48) ^(tmp.charAt(1)-48) ^(tmp.charAt(2)-48) ^(tmp.charAt(5)-48)  ;
            int s2= (tmp.charAt(2)-48) ^(tmp.charAt(3)-48) ^(tmp.charAt(0)-48) ^(tmp.charAt(4)-48)  ;

            tmps=tmps+(char)(s2+48)+(char)(s1+48)+(char)(s0+48);
            int x=Integer.parseInt(tmps,2);
            if(synd[x]!=-1)
            {
                int y=synd[x];
                if(tmp.charAt(y)=='0')
                {
                    tmp=tmp.substring(0,y)+'1'+tmp.substring(y+1,tmp.length());
                }
                else if(tmp.charAt(y)=='1')
                {
                    tmp=tmp.substring(0,y)+'0'+tmp.substring(y+1,tmp.length());
                }



            }
            tmp=tmp.substring(0, 4);
            tmp2+=tmp;
        }

        return tmp2;
    }
}

