package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class jdsServer {
    static final int PORT=1234;
    static private ArrayList<String> messageArr=new ArrayList<String>();

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket=null;
        try{
               URL resource=ServerObserver.class.getResource("/message.txt");
               File file= Paths.get(resource.toURI()).toFile();
               FileReader fr= new FileReader(file);
               BufferedReader reader=new BufferedReader(fr);
               String line=null;
               while((line=reader.readLine())!=null){
                    messageArr.add(line);
                }
            }
             catch (IOException e){
                e.printStackTrace();
            } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        int size=messageArr.size()-1;

        try {
        serverSocket=new ServerSocket(PORT);
        } catch (IOException e){
            System.out.println("Ошибка связывания с портом "+PORT);
            System.exit(-1);
        }
        System.out.println("Многопоточный сервер стартовал!");

        try{
            while(true){

                Socket client=serverSocket.accept();
                try{
                    System.out.println("Новое соединение установлено!");
                    System.out.println("Данные клиента: "+client.getInetAddress());
                    new ServerOne(client, messageArr,size);
                }catch (IOException e){
                    client.close();
                }
            }
        }finally {
            serverSocket.close();
        }
    }
}

class ServerOne extends Thread{
    private Socket client;
    private PrintWriter out;
    private ServerObserver cu;
    private ArrayList<String> messageArr;
    private int max;


    public ServerOne(Socket s, ArrayList<String> messageArr, int max) throws IOException{
        client=s;
        this.messageArr=messageArr;
        this.max=max;
        out=new PrintWriter(client.getOutputStream(),true);
        start();
    }

    public void run(){
        try{
            cu=new ServerObserver(client);

            Random random = new Random();
            int j = random.nextInt(max);

            out.println(messageArr.get(j));
        }catch (IOException ex){
            System.err.println("Ошибка чтения записи.");
        }
        finally {
            try{
                client.close();
                System.err.println("Соединение закрыто.");
            }catch (IOException ex){
                System.err.println("Сокет не закрыт!");
            }
        }
    }
}
