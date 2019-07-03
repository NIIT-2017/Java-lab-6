import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class task_6_2_s {

        public static void main (String args[]) throws IOException {
            //файл где хранятся афоризмы
            final String FILENAME = "dictum.json";

            System.out.println("server is started");
            ServerSocket server = null;
            Socket clientSocket=null;
            BufferedReader in = null;
            PrintWriter out = null;
            try {
                try {
                    server = new ServerSocket(5555);
                } catch (IOException e) {
                    System.out.println("port connection error");
                    System.exit(-1);
                }
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

                String input;
                while(true){
                    input=in.readLine();
                    if( input.equals("request")) {
                        System.out.println("server recive: " + input);
                        out.println(dictum.getRandomFromFile(FILENAME));
                    }
                    else{
                        in.close();
                        out.close();
                        clientSocket.close();
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
                    }
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
