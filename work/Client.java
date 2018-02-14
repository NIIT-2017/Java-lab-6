//Задание 2
import java.io.*;
import java.net.*;


public class Client {
    public static void main(String[] args) throws IOException {
        System.out.println("Клиент стартовал");
        String host = "localhost";
        System.out.println("Соединяемся с сервером ");


           Socket server = new Socket(host, 3345);

           BufferedReader in = new BufferedReader(
                   new InputStreamReader(server.getInputStream()));
           PrintWriter out =
                   new PrintWriter(server.getOutputStream(), true);
           BufferedReader inu =
                   new BufferedReader(new InputStreamReader(System.in));

           String fuser, fserver;

           while ((fuser = inu.readLine()) != null) {
               out.println(fuser);
               fserver = in.readLine();
               System.out.println(fserver);
               if (fuser.equalsIgnoreCase("close"))
                   break;
               if (fuser.equalsIgnoreCase("exit"))
                   break;
           }
           out.close();
           in.close();
           inu.close();
           server.close();
       }}




