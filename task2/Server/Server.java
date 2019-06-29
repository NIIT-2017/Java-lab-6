import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

class ServerOne extends Thread {

    private Socket socket;
    private PrintWriter out;
    String str;

    static ArrayList<String> aphorisms = new ArrayList<String>();

    static void readAphorismsFromFile(String fileName) throws IOException, ParseException {

        File f = new File(fileName);
        JSONParser parser = new JSONParser();
        FileReader fr = new FileReader(f);

        Object obj = parser.parse(fr);
        JSONArray aph = (JSONArray) ((JSONObject) obj).get("aphorisms");
        for (Object j : aph) {
            aphorisms.add(j.toString());
        }
    }

    public ServerOne(Socket s) throws IOException, ParseException {

        readAphorismsFromFile("aphorisms.json");
        Random random = new Random();
        int r = random.nextInt(aphorisms.size()) + 1;
        str = aphorisms.get(r);
        socket = s;
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    public void run() {

        out.println(str);
        System.out.println(str);
        System.out.println("Connection is closed");

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class Server {
    static final int PORT = 1234;

    public static void main(String[] args) throws IOException, ParseException {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Multithreaded server started");

        try {
            while (true) {
                Socket socket = s.accept();
                try {
                    System.out.println("New connection established");
                    new ServerOne(socket);
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            s.close();
        }
    }
}

