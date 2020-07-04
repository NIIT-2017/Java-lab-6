package sample;

import java.io.*;
import java.net.Socket;

public class ServerObserver {
    private Socket client;
    private PrintWriter out;

    ServerObserver(Socket s) throws IOException {
        this.client=s;
        out=new PrintWriter(client.getOutputStream(),true);
    }

}
