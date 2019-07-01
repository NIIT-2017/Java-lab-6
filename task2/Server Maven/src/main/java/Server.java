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
    private BufferedReader in;
    private PrintWriter out;


    public ServerOne(Socket s) throws IOException {
        socket = s;
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    public ArrayList parseJson()  {
        ArrayList<String> aphorisms = new ArrayList<String>();
        File f = new File("aphor");
        JSONParser parser = new JSONParser();
        FileReader fr = null;
        try {
            fr = new FileReader(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Object obj = null;
        try {
            obj = parser.parse(fr);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject js = (JSONObject) obj;
        JSONArray items = (JSONArray) js.get("afor");
        for (Object i : items) {
            aphorisms.add(((JSONObject) i).get("a").toString());
        }
        for (String a : aphorisms) {
            System.out.println(a);
        }
        return aphorisms;
    }







    public void run() {

        out.println(parseJson().get(3));
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public  class Server {
    static final int PORT = 5600;


    public static void main(String[] args) throws IOException, ParseException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server was started");

        try {
            while (true) {
                Socket socket = serverSocket.accept();
                new ServerOne(socket);
                // socket.close();
            }

        } finally {
            serverSocket.close();
        }

     //new ServerOne(new Socket()).parseJson();
    }
}