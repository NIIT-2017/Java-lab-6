package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalTime;


public class Client {

    private Socket client = null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in  = null;

    public Client(String host, int port) {
        try {
            client = new Socket(host, port);
        } catch (IOException e) {
            System.out.println("server didn't find");
            System.exit(-1);
        }
        //initialize stream of client
        try {
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            System.out.println("can't get streams");
        }


    }

    public LocalTime receiveTime() {
        LocalTime time;
        //send request time
        try {
            out.writeObject("get time");
        } catch (IOException e) {
            System.out.println("can't write into stream");
        }
        //waiting answer
        while (true) {
            try {
                if (in != null) {
                    time = (LocalTime) in.readObject();
                    break;
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("can't receive time");
            }
        }
        return time;
    }

    public String receivePhrase() {
        String phrase;
        //send request phrase
        try {
            out.writeObject("get phrase");

        } catch (IOException e) {
            System.out.println("can't write into stream");
        }
        while (true) {
            if (in != null) {
                try {
                    phrase = (String) in.readObject();
                    break;
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("can't receive phrase");
                }
            }
        }
        return phrase;
    }

}
