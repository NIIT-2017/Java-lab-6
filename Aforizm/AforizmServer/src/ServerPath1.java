import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerPath1 {
    private static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("New connection");
        try{
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("New string");
                new ServerPath2(socket);
            }
        }
        finally {
            serverSocket.close();
        }
    }
}