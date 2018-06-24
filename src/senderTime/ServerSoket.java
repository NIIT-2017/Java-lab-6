package senderTime;

import java.io.*;

import java.net.Socket;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class ServerSoket extends Thread{
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ArrayList<String> phrases;

    ServerSoket(Socket soket) {
        try {
            out = new ObjectOutputStream(soket.getOutputStream());
            in = new ObjectInputStream(soket.getInputStream());
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
                String request = (String) in.readObject();
                //get time
                if (request.equals("get time")) {
                    out.writeObject(LocalTime.now());
                    break;
                }

                //get phrase
                if (request.equals("get phrase")) {
                    Random random = new Random();
                    int i = random.nextInt(phrases.size());
                    out.writeObject(phrases.get(i));
                    break;
                }

            } catch (IOException e) {
                System.out.println("can't read message");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("clint missed");
    }

    private ArrayList<String> getPhase() {
        try {
            BufferedReader reader = new BufferedReader(
                                    new FileReader(new File(getClass().getResource("phrases.txt").toURI())));
            return (ArrayList<String>) reader.lines().collect(Collectors.toList());

        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
