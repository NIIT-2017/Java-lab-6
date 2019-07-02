import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Server {

    public static void main(String[] args) throws IOException {
        System.out.println("Старт сервера");
        PrintWriter    out= null;

        ServerSocket server = null;
        Socket       client = null;

        // создаем серверный сокет
        try {
            server = new ServerSocket(1234);
        } catch (IOException e) {
            System.out.println("Ошибка связывания с портом 1234");
            System.exit(-1);
        }

        try {
            System.out.print("Ждем соединения");
            client= server.accept();
            System.out.println("Клиент подключился");
        } catch (IOException e) {
            System.out.println("Не могу установить соединение");
            System.exit(-1);
        }


        out = new PrintWriter(client.getOutputStream(),true);

        Calendar calendar = new GregorianCalendar();
        Date time = calendar.getTime();

        out.println(time);

        out.close();
        client.close();
        server.close();
    }
}