package client;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private  static Socket socket;
    private Scanner in;
    private PrintWriter out;

    public Client() throws IOException {
        socket = new Socket("localhost",1234);
    }
    public Scanner getIn() throws IOException {
        in = new Scanner(socket.getInputStream(),"UTF-8");
        return in;
    }
    public PrintWriter getOut() throws IOException {
        out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
        return out;
    }
    public Socket getSocket() {
        return socket;
    }
}
