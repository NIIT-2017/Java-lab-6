package server;

import java.io.*;
import java.net.Socket;
import java.util.Random;



    public class ThreadedHandler implements Runnable
{
    private Socket incoming;
    private int count;

    public ThreadedHandler(Socket incoming) throws IOException
    {
        this.incoming = incoming;
    }
    private String getAphorism() throws IOException
    {
        String file = "aphorism.txt";
        Random rand = new Random();
        RandomAccessFile raf = new RandomAccessFile(file,"r");
        String line="";
        while ((raf.readLine())!=null)
            count++;
        int randomPos = rand.nextInt(count-1);
        int i=0;
        raf.seek(0);
        while (i!=randomPos+1)
        {
            line = raf.readLine();
            i++;
        }
        count = 0;
        return line;
    }

    public void run()
    {
        try
        {
            while (true)
            {
                PrintWriter out = new PrintWriter(incoming.getOutputStream());
                out.println(getAphorism());
                out.flush();
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}

