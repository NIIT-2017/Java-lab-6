package server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;

public class ThreadedHandler implements Runnable
{
    private Socket incoming;
    public ThreadedHandler(Socket incoming)
    {
        this.incoming = incoming;
    }
    @Override
    public void run()
    {
        try
        {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(incoming.getOutputStream()));
            while (incoming.isConnected())
            {
                LocalDateTime dt = LocalDateTime.now();
                int sec = dt.getSecond();
                int min = dt.getMinute();
                int hour = dt.getHour();
                int day = dt.getDayOfMonth();
                int month = dt.getMonthValue();
                int year = dt.getYear();
                String secString = appendNull(sec);
                String minString = appendNull(min);
                String hourString = appendNull(hour);
                String dayString = appendNull(day);
                String monthString = appendNull(month);
                String yearString = Integer.toString(year);
                String date_time = hourString+":"+minString+":"+secString+"t"+dayString+"."+monthString+"."+yearString;
                out.writeUTF(date_time);
                out.flush();
            }
        }catch (SocketException se){
            System.out.println("Соединение прервано");

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    private String appendNull(int time)
    {
        String timeString = Integer.toString(time);
        if (timeString.length()==1)
            timeString = "0"+timeString;
        return timeString;
    }
}
