import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {

    public static void main(String args[]){
        String host = "127.0.0.1";
        int port = 8081;
        new Client(host, port);


    }

    public Client(String host, int port) {
        try {
            String serverHostname = new String("127.0.0.1");

            System.out.println("Connecting to host " + serverHostname + " on port " + port + ".");

            Socket echoSocket = null;
            PrintWriter out = null;
            BufferedReader in = null;

            try {
                echoSocket = new Socket(serverHostname, 8081);
                out = new PrintWriter(echoSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: " + serverHostname);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Unable to get streams from server");
                System.exit(1);
            }

           if (true) {


                out.println("Hello,server!");
                System.out.println("server: " + in.readLine());
            out.println("Bay,server!");
            }

            out.close();
            in.close();
            echoSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}