package senderTime;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.time.LocalTime;

public class ServerSoket extends Thread{
    private Socket soket;
    private int count;
    private ObjectOutputStream out;
    private InputStream in;
    private LocalTime lastQuery;

    public ServerSoket(Socket soket) {
        this.soket = soket;
        try {
            out = new ObjectOutputStream(soket.getOutputStream());
            in = soket.getInputStream();
        } catch (IOException e) {
            System.out.println("can't get out stream");
        }
        count = 0;
        this.start();
    }

    public void run() {
        while(true) {
            try {
                byte [] message= new byte[30];
                int lenght;
                if ((lenght = in.read(message)) >0){
                    lastQuery = LocalTime.now();
                    System.out.write(message,0,lenght);
                    System.out.println();
                    out.writeObject(LocalTime.now());
                    break;
                }
            } catch (IOException e) {
                System.out.println("can't read message");
            }
        }
        System.out.println("clint missed");
    }
}
