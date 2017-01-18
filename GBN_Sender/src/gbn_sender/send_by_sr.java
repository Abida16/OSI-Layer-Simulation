/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gbn_sender;

import static gbn_sender.send_by_gbn.PORT;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Abida
 */
public class send_by_sr
{

    int sn=0,sf=0,sw=4;
    int x;
    ArrayList<String>fr;
    ServerSocket serverSocket = null;
    Socket socket = null;
    private DataInputStream in;
    static int PORT = 3000;
    int running=1;
    int timer[];
    private long idleTimer;
    String StoreFrames[];
    LinkedList<String> ackBuffer;
    LinkedList<String> nakBuffer;
    private long init_time[] ;
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
                    if(ack.charAt(0)=='N')
                        nakBuffer.add(ack.substring(1, ack.length()));
                    else
                    {
                        ackBuffer.add(ack.substring(1, ack.length()));
                         //System.err.println("ack****####******"+ack);
                        if(Integer.parseInt(ack.substring(1,ack.length()))==fr.size()-1 )
                    {
                          System.out.println("ack of "+Integer.parseInt(ack.substring(1,ack.length()))+" arrived");
                        System.err.println("Buffer Size from Thread: "+ackBuffer.size());
                        running = 0;
                        break;
                    }

                    }
                    System.err.println("Buffer Size from Thread: "+ackBuffer.size());
                   
                }
                catch(Exception e)
                {
                    System.out.println("Problem in ack occured");
                    continue;
                }

            }
        }

    }
    send_by_sr(ArrayList<String> frames) throws IOException
    {
        StoreFrames = new String[frames.size()+2];
        init_time = new long[frames.size()+2];
        timer= new int[frames.size()+2];
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
        nakBuffer = new LinkedList<String>();

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
            if(running == 0)
            {
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
                if(timer[sn]==0)
                {
                    timer[sn]=1;
                    init_time[sn ]=System.currentTimeMillis();
                }
                idleTimer = System.currentTimeMillis();

            }
            if(!nakBuffer.isEmpty())
            {
                int x= Integer.parseInt(nakBuffer.remove());
                System.out.println("negetive ack "+x);
               System.out.println("#### resending frame no "+x+"  "+StoreFrames[x]);
                out.writeBytes(StoreFrames[x]);
                timer[x]=1;
                init_time[x]= System.currentTimeMillis();
            }
            if(!ackBuffer.isEmpty() ) //can be is size
            {
                // int flg=0;
                x = Integer.parseInt(ackBuffer.remove());

                System.out.println("Ack of frame "+ x+" is received");
                while(sf<=x)
                {
                    StoreFrames[sf]=null;
                    timer[sf]=0;
                    sf++;
                }
                idleTimer = System.currentTimeMillis();
            }
            
            for(int i=sf; i<=sn && i <frames.size(); i++)
            {
                if(System.currentTimeMillis() - init_time[i]> 500&&timer[i]==1)
                {
                    init_time[i] = System.currentTimeMillis();
                    timer[i]=1;
                    System.err.println("timeout of "+i);

                    //Resend frames
                    System.out.println("#### resending frame no "+i+"  "+StoreFrames[i]);
                    out.writeBytes(StoreFrames[i]);
                    System.err.println("Resending frame: "+i);

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
        /*if(System.currentTimeMillis() - init_time> 500&&timer==1)
         {
             init_time = System.currentTimeMillis();
             timer=1;
             System.err.println("timeout at: " + sf);
             int temp = sf;
             while(temp < sn)
             {
                 //Resend frames
                   System.out.println("#### resending frame "+StoreFrames[temp]);
                 out.writeBytes(StoreFrames[temp]);
                 System.err.println("Resending frame: "+temp);
                 temp++;
             }
             idleTimer = System.currentTimeMillis();
         }*/

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

