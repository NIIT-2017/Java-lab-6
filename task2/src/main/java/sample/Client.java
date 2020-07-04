package sample;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private String aphorism;
    private Socket server;
    private BufferedReader reader;
    private PrintWriter writer;

    public Client() throws IOException {
        server = new Socket("127.0.0.1", 8000);
        reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
        writer = new PrintWriter(server.getOutputStream(), true);
    }

    public String getAphorism() {
        return aphorism;
    }

    public void startClient(String message) throws IOException {
            writer.println(message);
            if (message.equals("exit")){
                reader.close();
                writer.close();
                server.close();
            }
            else{
                aphorism = reader.readLine();
            }
    }
}