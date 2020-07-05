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
        PrintWriter out =
                new PrintWriter(server.getOutputStream());
        BufferedReader inu =
                new BufferedReader(new InputStreamReader(System.in));

        String fserver;

        while (true) {
            String fuser = inu.readLine();
            out.println(fuser);
            out.flush();
            while ((fserver = in.readLine())!= null)
                System.out.println(fserver);
            if (fuser.equalsIgnoreCase("close"))
                break;
            if (fuser.equalsIgnoreCase("exit"))
                break;
        }
/*
        out.close();
        in.close();
        inu.close();
        server.close();*/
    }
}