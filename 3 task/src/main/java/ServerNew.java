import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public
class ServerNew extends Thread {
    private Socket socket;
    private Integer id;
    private PrintWriter out;
    static ArrayList<String> messages = new ArrayList<String>(Arrays.asList(
            "Why are you so serious?",
            "Because I'm Batman!",
            "The start of ending!",
            "The end of starting!",
            "Fly like butterfly!",
            "Be tasty like potato!"
    ));
    public void run() {
        while (true) {
            try {
                Thread.sleep((id+1) * 3000);
                out.println(String.valueOf(id)+messages.get(id % 6));
                System.out.println(String.valueOf(id)+messages.get(id % 6));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public ServerNew (Socket s, Integer id) throws IOException {
        this.id = id;
        this.socket = s;
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        out.println("New"+String.valueOf(id));
        System.out.println("New"+String.valueOf(id));
        start();
    }
}


