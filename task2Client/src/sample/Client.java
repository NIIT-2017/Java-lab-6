package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

    String phrase;


    public String getPhrase() {
        return phrase;
    }

    public void goClient(String ip){
        try {
            Socket server = new Socket(ip, 1234);
            System.out.println("Создан серверный сокет на стороне клиента с ip " + ip);
            InputStreamReader inputStreamReader = new InputStreamReader(server.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            phrase = reader.readLine();
            //System.out.println(phrase + " это фраза у клиента");
            reader.close();
            server.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
