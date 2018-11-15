import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static Map<String, Client> clients;

    public static void main(String[] args) {

        try {
            Main main = new Main();
            clients = new HashMap<>();
            main.createClients();
        } catch (IOException | ParseException e) {
            System.out.println("Не удалось прочитать файл с загеристированными клиентами");
            e.printStackTrace();
            System.exit(-1);
        }

        ServerSocket s;
        try {
            s = new ServerSocket(1236);
            System.out.println("Мультипоточный сервер стартовал");
            try {
                while (true) {
                    Socket socket = s.accept();
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String login = in.readLine();
                        Client client = clients.get(login);
                        if (client == null || client.isOnline()){
                            new ServerOne(socket, null, null);
                        }else {
                            client.setOnline(true);
                            new ServerOne(socket, login, client);
                        }
                    }
                    catch (IOException | ParseException e) {
                        socket.close();
                    }
                }
            }
            finally {
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createClients() throws IOException, ParseException {
        InputStream is = getClass().getResourceAsStream("clients.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        JSONParser parser=new JSONParser();
        JSONObject js=(JSONObject)parser.parse(reader);
        JSONArray items=(JSONArray)js.get("clients");

        for (Object i : items) {
            String login = (String) ((JSONObject)i).get("login");
            clients.put(login, new Client());
        }
    }

}
