package senderTime;

import java.io.*;

import java.net.Socket;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class ServerSoket extends Thread{
    private ObjectOutputStream out;
    private InputStream in;
    private ArrayList<String> phrases;

    ServerSoket(Socket soket) {
        try {
            out = new ObjectOutputStream(soket.getOutputStream());
            in = soket.getInputStream();
        } catch (IOException e) {
            System.out.println("can't get out stream");
        }
        //load phrases
        phrases = getPhase();
        this.start();
    }

    public void run() {
        while(true) {
            try {
                byte [] message= new byte[30];
                if(in.read(message)>0) {
                    //get time
                    if (Arrays.toString(message).equals("get time")) {
                        System.out.println("get time");
                        out.writeObject(LocalTime.now());
                        break;
                    }

                    //get phrase
                    if (Arrays.toString(message).equals("get phrase")) {
                        System.out.println(" get phrase");
                        Random random = new Random();
                        int i = random.nextInt(phrases.size());
                        out.writeObject(phrases.get(i));
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("can't read message");
            }
        }
        System.out.println("clint missed");
    }

    private ArrayList<String> getPhase() {
        try {
            BufferedReader reader = new BufferedReader(
                                    new FileReader(new File(getClass().getResource("phrases.txt").toURI())));
            System.out.println(reader.readLine());
            return (ArrayList<String>) reader.lines().collect(Collectors.toList());

        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
