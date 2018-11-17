import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class SocketServer extends Thread {
    public static final int PORT_NUMBER = 8081;

    protected Socket socket;

    private SocketServer(Socket socket) {
        this.socket = socket;
        System.out.println("New client connected from " + socket.getInetAddress().getHostAddress());
        start();
    }

    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String request;
            String line;
           while ((request = br.readLine()) != null) {
                System.out.println("Message received:" + request);
                line = readtext()+'\n';
                out.write(line.getBytes());
            }

        } catch (IOException ex) {
            System.out.println("Unable to get streams from client");
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("SocketServer Started");
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT_NUMBER);
            while (true) {

                new SocketServer(server.accept());
            }
        } catch (IOException ex) {
            System.out.println("Unable to start server.");
        } finally {
            try {
                if (server != null)
                    server.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String readtext() throws IOException {
        Random r = new Random();
        ArrayList<String> aphorism = new ArrayList<>();
        String FILE_NAME = "aphorism.txt";
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(FILE_NAME);
        BufferedReader bf = new BufferedReader(new InputStreamReader(is,"UTF-8"));

          bf.lines().forEach((i)->aphorism.add(i));
        String line =   aphorism.get(r.nextInt(aphorism.size()));
        return line;
    }
}
