package digitalWatch.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalTime;


public class ClientTime {
        private Socket client = null;
        private OutputStream out = null;
        private ObjectInputStream in  = null;

    public ClientTime(String host,int port) {
        try {
            client = new Socket(host, port);
        } catch (IOException e) {
            System.out.println("server didn't find");
            System.exit(-1);
        }
        //initialize stream of client
        try {
            out = client.getOutputStream();
            in = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            System.out.println("can't get streams");
        }
    }

    public LocalTime getTime() {
        LocalTime time;
        //send request
        try {
            out.write("get time".getBytes());
        } catch (IOException e) {
            System.out.println("can't write into stream");
        }
        //waiting answer
        while (true) {
            try {
                if (in != null) {
                    time = (LocalTime) in.readObject();
                    System.out.println(time.toString());
                    break;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return time;
    }
}
