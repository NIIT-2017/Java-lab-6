import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class ServerOne extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    String login;

    public ServerOne(Socket s) throws IOException {
        socket = s;
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        start();
    }

    public void run() {
        try {
            login = in.readLine();
            Client user = new Client(login);
            user.readTasks(login);
            System.out.println("Your login is " + login);
            Iterator iterator = user.toDoList.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                Thread.sleep((long) pair.getKey() * 1000);
                out.println(pair.getValue());
                System.out.println(pair.getValue());
            }
            out.println("End");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Сокет не закрыт");
            }
        }
    }
}

class Client {
    String login;
    HashMap<Long, String> toDoList = new HashMap<>();

    Client(String login) {
        this.login = login;
    }

    protected void readTasks(String login) throws ParseException, IOException {

        InputStream is = getClass().getResourceAsStream(login + ".json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        JSONParser parser = new JSONParser();
        JSONObject js = (JSONObject) parser.parse(reader);
        JSONArray items = (JSONArray) js.get("tasks");
        for (Object i : items) {
            long seconds = (long) ((JSONObject) i).get("time");
            String taskText = (String) ((JSONObject) i).get("task");
            toDoList.put(seconds, taskText);
        }
    }
}

public class Server {

    static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Multithreaded server started");

        try {
            while (true) {
                Socket socket = s.accept();
                System.out.println("New connection established");
                new ServerOne(socket);
            }
        } finally {
            s.close();
        }
    }
}

