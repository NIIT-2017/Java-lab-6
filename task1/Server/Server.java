import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Server {
    public static void main(String[] args) throws IOException {
        System.out.println("Server started");
        PrintWriter out = null;
        BufferedReader in = null;
        ServerSocket server = null;
        Socket client = null;
        String input;


        try {
            server = new ServerSocket(1234);
        } catch (IOException e) {
            System.out.println("Error with connection to port 1234");
            System.exit(-1);
        }

        try {
            System.out.println("Waiting for connection");
            client = server.accept();
            System.out.println("Client connects");
        } catch (IOException e) {
            System.out.println("Can't connect");
            System.exit(-1);
        }

        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        System.out.println("Waiting for time request");

        while ((input = in.readLine()) != null) {
            if (input.equalsIgnoreCase("exit"))
                break;
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
            out.println(simpleDateFormat.format(calendar.getTime()));
        }

        out.close();
        in.close();
        client.close();
        server.close();
    }
}
