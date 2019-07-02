import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;


class ServerOne extends Thread {
    private Socket socket;
    private Integer id;
    private PrintWriter out;
    static ArrayList<String> messages = new ArrayList<String>(Arrays.asList(
            "don't worry", "be happy", "relax", "take it easy"
    ));

    public ServerOne(Socket s, Integer id) throws IOException {
        this.id = id;
        this.socket = s;
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        out.println("New"+String.valueOf(id));
        System.out.println("New"+String.valueOf(id));
        start();
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep((id+1) * 10 * 1000);
                out.println(String.valueOf(id)+messages.get(id % 4));
                System.out.println(String.valueOf(id)+messages.get(id % 4));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Server {
    static final int PORT = 1234;
    static Integer id=0;
    public static void main(String[] args) throws IOException {
        ServerSocket s=new ServerSocket(PORT);
        System.out.println("Multiserver is starting");
        try{
            while (true){
                Socket socket=s.accept();
                System.out.println("New connection");
                new ServerOne(socket,id);
                id++;
            }
        } finally {
            s.close();
        }

    }
}