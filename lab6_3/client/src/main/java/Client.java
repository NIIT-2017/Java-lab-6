import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

    public Socket echoSocket = null;
    private int port;
    private int id;
    private String host;

    public Client(String host, int port, int id) {
        this.port = port;
        this.id = id;
        this.host = host;
        String serverHostname = new String(host);
        try {
            echoSocket = new Socket(serverHostname, port);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Unable to get streams from server");
            System.exit(1);
        }
    }

    @Override
    public void run() {

        System.out.println("Connecting to host " + host + " on port " + port  + ".");

        PrintWriter out = null;
        BufferedReader in = null;

            try {

                out = new PrintWriter(echoSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));


                out.println("client " + id + " conected to server");
                System.out.println("client " + id + " conected to server");
                String line;
                while (true) {

                    if ((line=in.readLine())!=null) {

                        System.out.println("server: " + line);
                        if ("q".equals(line)) {
                            break;
                        }
                        line=null;
                    }
                }

                Thread.sleep(10000);
                out.close();
                in.close();
                echoSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }