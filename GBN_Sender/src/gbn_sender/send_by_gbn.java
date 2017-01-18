/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gbn_sender;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abida
 */

public class send_by_gbn
{
    int sn=0,sf=0,sw=4;
    int x;
    ArrayList<String>fr;
    ServerSocket serverSocket = null;
    Socket socket = null;
    private DataInputStream in;
    static int PORT = 3000;
    int running=1,timer;
    private long init_time = 0,idleTimer;
    String StoreFrames[];
    LinkedList<String> ackBuffer;
    class AckReceiver implements Runnable
    {
        AckReceiver()
        {
            Thread t = new Thread(this,"Ack Thread");
            t.start();
        }


        @Override
        public void run()
        {
            while(true)
            {
                try
                {
                    socket.setSoTimeout(1000); //why??
                    String ack= in.readLine();
                    System.err.println(ack);
                    ackBuffer.add(ack);
                    System.err.println("Buffer Size from Thread: "+ackBuffer.size());
                    if(Integer.parseInt(ack)==fr.size()-1)
                    {

                         System.err.println("Buffer Size from Thread: "+ackBuffer.size());
                        running = 0;
                        break;
                    }
                    timer=0;
                }
                catch(Exception e)
                {
                     // System.out.println("getting excp Ack of frame "+ (fr.size()-1)+" is received");
                  // running =0;
                    //break;
                }

            }
        }

    }
    send_by_gbn(ArrayList<String> frames) throws IOException
    {
        StoreFrames = new String[frames.size()];
        fr=frames;
        try
        {
            serverSocket = new ServerSocket(PORT);
            socket = serverSocket.accept();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        try
        {
            in = new DataInputStream(socket.getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        ackBuffer = new LinkedList<String>();
        new AckReceiver();

        // String chunk = "";
        DataOutputStream out = null;
        try
        {
            out = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException e)
        {
            System.out.println(e);
        }

        while(true)
        {
            /*if(sn==frames.size()-1)
            {
                System.out.println("Breaking");
                break;
            }*/
            if(running == 0){
                 //
                System.out.println("break ");
                break;
            }
           //  if(idleTimer- System.currentTimeMillis()>2000)
             //  break;
            if((sn-sf)<=sw && sn != frames.size() )
            {
                String chunk = frames.get(sn);
                chunk=binary32(sn)+chunk;
                StoreFrames[sn] = chunk;
                System.out.println(sn+"**** "+chunk);
                out.writeBytes(chunk);

                //System.err.println("Sending Frame: "+sn);
             //   System.out.println(sn+"**** "+chunk);
                sn++;
                if(timer==0)
                {
                    timer=1;
                    init_time=System.currentTimeMillis();
                }
                idleTimer = System.currentTimeMillis();

            }
            if(!ackBuffer.isEmpty()) //can be is size
            {
                // int flg=0;
                x = Integer.parseInt(ackBuffer.remove());

                System.out.println("Ack of frame "+ x+" is received");
                while(sf<=x)
                {
                    StoreFrames[sf]=null;
                    //  flg=1;
                    sf++;
                }
                idleTimer = System.currentTimeMillis();


            }
            if(System.currentTimeMillis() - init_time> 500&&timer==1)
            {
                init_time = System.currentTimeMillis();
                timer=1;
                System.err.println("timeout at: " + sf);
                int temp = sf;
                while(temp <= sn)
                {
                     try{
                      System.out.println("#### resending frame "+StoreFrames[temp]);

                    out.writeBytes(StoreFrames[temp]);
                      }catch(Exception e)
                      {
                          break;
                      }
                    System.err.println("Resending frame: "+temp);
                    temp++;
                }
                idleTimer = System.currentTimeMillis();
            }

        }
        try {
            socket.close();
            serverSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public String binary32(int value)
    {
        String result = "";
        result = Integer.toBinaryString(value);
        while(result.length()<32)
        {
            result = '0'+result;
        }
        return result;
    }

}
