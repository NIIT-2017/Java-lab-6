import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket server = null;
        BufferedReader in = null;

        System.out.println("Client started");
        server = new Socket("127.0.0.1", 1234);
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        String fserver = in.readLine();
        System.out.println(fserver);
        in.close();
        server.close();
    }
}
