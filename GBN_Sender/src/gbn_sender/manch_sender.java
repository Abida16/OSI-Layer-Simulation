package gbn_sender;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.IOException;

public class manch_sender
{
    public String line="";
    manch_sender(String s) throws FileNotFoundException, IOException
    {
        StringBuilder binary2 = new StringBuilder();
        for (int i = 0; i <s.length(); i++)
        {
            char val=s.charAt(i);
            binary2.append(val  == '0' ? "+-" : "-+");

        }
        line=binary2.toString();

    }
}
