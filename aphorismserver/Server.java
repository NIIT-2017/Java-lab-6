package aphorismserver;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

class Compound extends Thread {
    private Socket client = null;
    private PrintWriter out;
    static ArrayList<String> aphorisms = new ArrayList<String>();

    static void readFromTxt(String fileName) {
        try {
            FileInputStream fstream = new FileInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                aphorisms.add(strLine);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Compound(Socket socket) throws IOException {
        client = socket;
        readFromTxt("aphorisms.txt");
        out = new PrintWriter(client.getOutputStream(), true);
        Random random = new Random();
        out.println(aphorisms.get(random.nextInt(aphorisms.size())));
        out.close();
        client.close();
    }
}
public class Server {
    public static void main(String[] args) throws IOException {
        System.out.println("Start server");

        ServerSocket server = null;

        try {
            server = new ServerSocket(1515);
        } catch (IOException e) {
            System.out.println("Error connection to port: 1515");
            System.exit(-1);
        }

        try {
            while (true) {
                System.out.print("Waiting for connection");
                Socket soc = server.accept();
                try {
                    System.out.println("\n" + "Client connected");
                    new Compound(soc);
                } catch (IOException e) {
                    System.out.println("Connection error");
                    System.exit(-1);
                }
            }
        } finally {
            server.close();
        }
    }
}
