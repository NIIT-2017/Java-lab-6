package ClientServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {
    private int serversocket;
    private ServerSocket server = null;
    private Socket client = null;
    private PrintWriter out;

    public Server(int serversocket) {
        this.serversocket = serversocket;
    }

    public void createServer () throws IOException, InterruptedException {
        try {
            server = new ServerSocket(serversocket);
        } catch (IOException e) {
            System.out.println("Ошибка связывания с портом"+ serversocket);
            System.exit(-1);
        }
        try {
            System.out.print("Ждем соединения\n");
            client = server.accept();
            System.out.println("Клиент подключился\n");
        } catch (IOException e) {
            System.out.println("Не могу установить соединение\n");
            System.exit(-1);
        }
        out = new PrintWriter(client.getOutputStream(),true);
        while (true) {
            out.println(sendTime());
            Thread.sleep(1000);
        }
    }

    private String sendTime(){
        Date date = new Date();
        return date.toString();
    }

    public void disconnect() throws IOException {
        out.close();
        server.close();
        client.close();
    }
}
