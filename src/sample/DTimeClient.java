package sample;

import java.io.*;
import java.net.*;

public class DTimeClient
{
    public static void main(String[] args) {
        requestToServer("getDateTimeMoscow");
    }
    public static String requestToServer(String request) {
        String[] args = new String[]{"localhost"};
        if (args.length == 0) {
            System.err.println("Use: java Client hostname.");
            System.exit(-1);
        }
        System.out.println("Client start.");
        System.out.println("Connection with server " + args[0] + ". ");
        System.out.println("Please wait...");
        String response = "Server is not open or closed.";
        try {
            Socket server = new Socket(args[0], 1231);
            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            PrintWriter out = new PrintWriter(server.getOutputStream(), true);
            System.out.println("Connection is OK.");
            System.out.println("Client: " + request);
            out.println(request);
            response = in.readLine();
            System.out.println();
            System.out.println("Server: " + response);
            out.println("End");
            in.close();
            out.close();
            server.close();
            System.out.println("Client closed.");
        }
        catch (IOException ex) {
            System.err.println("Server is not open or closed. Please try again later.");
        }
        return response;
    }
}
