import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Мультипоточный сервер стартовал");
        try {
            while (true) {
                Socket socket = s.accept();
                try {
                    System.out.println("Новое соединение установлено");
                    System.out.println("Данные клиента: "+
                            socket.getInetAddress());
                    new ServerOne(socket);
                }
                catch (IOException e) {
                    socket.close();
                }
            }
        }
        finally {
            s.close();
        }
    }
}