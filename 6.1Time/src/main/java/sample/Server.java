package sample;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Server {
    public static void main(String[] args) throws IOException{

        BufferedReader in=null;
        PrintWriter out=null;
        ServerSocket server = null;
        Socket client=null;

        try {
            server=new ServerSocket(2345);
        } catch (IOException e){
            System.out.println("Connection error with port 2345");
            System.exit(-1);
        }

        try{
            System.out.println("Waiting connection");
            client=server.accept();
            System.out.println("Client connected");
        }catch (IOException e){
            System.out.println("Unable to establish connection");
            System.exit(-1);
        }

        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out=new PrintWriter(client.getOutputStream(),true);
        String input;

        while((input=in.readLine())!=null) {

            if(input.equalsIgnoreCase("stop"))
                break;

            String date = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
            String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
            out.println(time + date);
            out.flush();
            System.out.println(time + " " + date);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace(); }
        }
        out.close();
        in.close();
        client.close();
        server.close();
    }

}

