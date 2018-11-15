import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {
    public static void main(String[] args) {
        System.out.println("Старт сервера");

        PrintWriter out= null;
        ServerSocket server = null;
        Socket client = null;

        try {
            server = new ServerSocket(1234);
        } catch (IOException e) {
            System.out.println("Ошибка связывания с портом 1234");
            System.exit(-1);
        }

        try {
                System.out.println("Ждем соединения...");
                client= server.accept();
                if (client != null){
                    System.out.println("Клиент подключился.");
                    try {
                        out = new PrintWriter(client.getOutputStream(),true);

                        Date date = new Date();
                        out.printf("%tR", date);

                        out.close();
                        client.close();

                        System.out.println("Клиент отключился.\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        } catch (IOException e) {
            System.out.println("Не могу установить соединение.");
            System.exit(-1);
        }
    }
}
