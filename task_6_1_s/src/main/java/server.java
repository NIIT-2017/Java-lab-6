import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class server {
    public static void main (String args[]) throws IOException {
        System.out.println("server is started");
        ServerSocket server = null;
        Socket clientSocket=null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            try {
                server = new ServerSocket(5555, 2);
            } catch (IOException e) {
                System.out.println("port connection error");
                System.exit(-1);
            }
            while(true){
                try {
                    System.out.println("wait for connection...");
                    clientSocket = server.accept();
                    System.out.println("connection is set up");
                } catch (IOException e) {
                    System.out.println("server accept error");
                    System.exit(-2);
                }
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())),true);
                String input=in.readLine();
                Date date = new Date();
                out.println(""+date);
                in.close();
                out.close();
                clientSocket.close();
            }
        }finally {
            System.out.println("server is closed");
            in.close();
            out.close();
            clientSocket.close();
            server.close();
        }
    }
}
