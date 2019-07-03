package sample;

import java.io.*;
import java.net.Socket;

public class client {
    public static void clientStart () throws IOException {
        System.out.println("client is started");
        Socket clientSocket =null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            clientSocket = new Socket("localhost", 5555);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())),true);

            String input,output;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                System.out.print("print something: ");
                output = reader.readLine();
                out.println(output);
                if(output.equals("exit")){
                    System.out.println("send exit to server, and client exit too");
                    break;
                }
                input = in.readLine();
                System.out.println("server answer is: "+input);
            }
            /*---------------*/
        } finally {
            System.out.println("client is closed");
            in.close();
            out.close();
            clientSocket.close();
        }
    }
}
