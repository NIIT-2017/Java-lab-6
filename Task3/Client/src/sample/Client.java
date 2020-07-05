package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private int id;
    private Socket socket;
    private ObservableList<ClientMessage> clientMessages = FXCollections.observableArrayList();
    private BufferedReader in; // stream for getting messages from server
    private PrintWriter out;  //stream for initial sending id to server


    public Client(int id){
        this.id = id;
    }

    public ObservableList<ClientMessage> getClientMessages() {
        return clientMessages;
    }

    public int getId() {
        return id;
    }

    public void setConnectionClient(String ip){
        try {
            socket = new Socket(ip, 1558);
            System.out.println("New socket was created with ip " + ip);
            //creating streams

            out = new PrintWriter(socket.getOutputStream(),true);
            //sending id to server
            out.println(id);
            System.out.println("Connection for client " + id + " was set, id was sent");

            //creating new thread to divide renovating of form from getting messages
            Thread t = new Thread (new Runnable(){
                public void run(){
                    try {
                        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                        in = new BufferedReader(inputStreamReader);
                        String line;
                        while ((line = in.readLine()) != null) {
                            System.out.println("Client got the messages: " + line);
                            String[] messForParsing = line.split("/");
                            ClientMessage mess = new ClientMessage(messForParsing[0], messForParsing[1]);
                            clientMessages.add(mess);
                        }
                        in.close();
                        out.close();
                        socket.close();
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    public void closeClient(){
        //System.out.println("The user pressed the button Exit, we are closing the programm");
        //sending exit to server
        //out.println("exit");
        //creating new thread to divide renovating of form from getting messages
        Thread t = new Thread (new Runnable(){
            public void run(){
                try {
                    System.out.println("The user pressed the button Exit, we are closing the programm");
                    //sending exit to server
                    out.println("exit");
                    System.out.println("Sending exit signal to server");
                    in.close();
                    out.close();
                    socket.close();
                    System.out.println("Socket is closed: " + socket.isClosed());
                    System.exit(0);
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }


}

