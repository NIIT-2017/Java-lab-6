import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.FileReader;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Старт сервера");
        //BufferedReader in=null;
        PrintWriter out=null;

        ServerSocket server=null;
        Socket client=null;

        try {
            server=new ServerSocket(1234);

        } catch (IOException e) {
            System.out.println("Ошибка связывания с портом 1234");
            System.exit(-1);
        }

        try {
            System.out.println("Ждем соединения");
            client=server.accept();
            System.out.println("Клиент подключился");
        } catch (IOException e) {
            System.out.println("Не могу установить соединение");
            System.exit(-1);
        }

        out = new PrintWriter(client.getOutputStream(),true);
        Aphorism one = new Aphorism();
        out.println(one.aphorism());
        out.close();
        client.close();
    }
}

class Aphorism{
    public String aphorism(){
        String str = "";
        String str1;
        try {
            FileReader reader = new FileReader("aphorism.txt");
            int c;
            while((c=reader.read())!=-1){
                str = str + (char)c;
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        String[] buf = str.split("/");
        Random random = new Random();
        return buf[random.nextInt(buf.length)];
    }
}
