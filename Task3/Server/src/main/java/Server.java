import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("�������������� ������ ���������");
        try{
            while(true){
        Socket socket = serverSocket.accept();
                System.out.println("����� ���������� �����������");
                new ServerOne(socket);
            }
        }
        finally {
            serverSocket.close();
        }
    }
}
