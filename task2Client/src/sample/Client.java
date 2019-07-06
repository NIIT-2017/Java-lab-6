package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    String phrase;
    final int PORT = 1234;
    final String IP;
    Socket server;
    private InputStreamReader inputStreamReaderCl;
    private BufferedReader readerCl;
    private PrintWriter writerCl;


    //client constructor with opening of socket with server
    public Client(String ip){
        System.out.println("Клиент создан");
        this.IP = ip;
        try {
            server = new Socket(ip, PORT);
            System.out.println("Создан серверный сокет на стороне клиента с ip " + ip);
            String input, output;
            inputStreamReaderCl = new InputStreamReader(server.getInputStream());
            readerCl = new BufferedReader(inputStreamReaderCl);
            writerCl = new PrintWriter(server.getOutputStream(), true);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getPhrase() {
        return phrase;
    }

    public void goClient(String message){
        System.out.println(message);//testing w
        try {
            writerCl.println(message);
            if (!message.equals("exit")){
                phrase = readerCl.readLine();
                System.out.println("Клиент получил фразу: " + phrase);
            }
            else{
                readerCl.close();
                inputStreamReaderCl.close();
                writerCl.close();
                server.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
            writerCl.close();
        }
    }
}
