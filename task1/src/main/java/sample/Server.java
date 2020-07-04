package sample;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Server {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'at' HH:mm:ss");

    public void startServer() throws IOException {
            ServerSocket server = new ServerSocket(8000);
            System.out.println("server started");

            while (true) {
                Socket clientSocket = server.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                LocalDateTime now = LocalDateTime.now();
                String message = formatter.format(now);
                writer.println(message);
                writer.close();
            }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer();
    }

}