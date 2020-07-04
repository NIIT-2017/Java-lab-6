package sample;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Server {
    private ArrayList<String> aphorisms = new ArrayList<>();

    public Server() throws IOException {
        String line;
        File file = new File("wisdom-utf8.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
        while ((line = reader.readLine()) != null) {
                aphorisms.add(line);
        }
        reader.close();
    }

    public void startServer() throws IOException {
        ServerSocket server = new ServerSocket(8000);
        Socket client = server.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter  writer = new PrintWriter(client.getOutputStream(), true);

        while (true) {
            String input = reader.readLine();
            if (input.equals("exit")) {
                    break;
            }
            int i = new Random().nextInt(aphorisms.size());
            writer.println(aphorisms.get(i));
        }
        reader.close();
        writer.close();
        client.close();
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer();
    }
}