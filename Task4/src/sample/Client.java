package sample;

import javafx.scene.image.Image;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    private Socket socket = new Socket();
    private String host = "www.catsthatlooklikehitler.com";
    private PrintWriter out = null;
    private BufferedReader in = null;
    private String name;
    private String owner;
    private String time;
    private String date;

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public void connect(){
        try {
            socket.connect(new InetSocketAddress(host, 80));
            System.out.println("соединение установлено!");
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getResponse(){
        String request = "GET http://www.catsthatlooklikehitler.com/cgi-bin/seigmiaow.pl? HTTP/1.0\r\n\r\n";
        String response="";
        String newLine = "";
        out.println(request);
        System.out.println("Запрос отправлен");
        try {
            while (newLine!=null){
               newLine = in.readLine();
               if(newLine.contains("owned by")) {
                   response += newLine;
                   break;
               }
           }
            name = response.replaceAll("^.*</A><BR><FONT SIZE=-2 FACE=Verdana>(.*) \\(owned.*$", "$1");
            owner = response.replaceAll("^.*owned by (.*)\\).*\\).*$", "$1");
            time = response.replaceAll(".*at (.*)$", "$1");
            date = response.replaceAll(".*>(.*) at.*$", "$1");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
