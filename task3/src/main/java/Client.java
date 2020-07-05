import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        System.out.println("Клиент стартовал");
        Socket server = null;

        if (args.length==0) {
            System.out.println("Использование: java Client hostname");
            System.exit(-1);
        }

        System.out.println("Соединяемся с сервером "+args[0]);

        server = new Socket(args[0],1235);
        BufferedReader in  = new BufferedReader(
                new  InputStreamReader(server.getInputStream()));


        String fserver;

        while (true) {
            fserver = in.readLine();
            System.out.println(fserver);
        }
    }
}