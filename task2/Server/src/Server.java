import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ServerOne extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<String> aphorisms = new ArrayList<>();

    public ServerOne(Socket s) throws IOException {
        socket = s;
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    public void run() {

        out.println("hello");
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class Server {
    static final int PORT = 5600;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server was started");
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                new ServerOne(socket);
               // socket.close();
            }

        } finally {
            serverSocket.close();
        }


    }
}