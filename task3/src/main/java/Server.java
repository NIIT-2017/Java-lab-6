import com.sun.tools.javac.Main;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server {
    static final int PORT = 1235;
    static Map<Integer,Socket> Client = new HashMap<>();


    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Мультипоточный сервер стартовал");


        new Thread(()->{
            while (true){
                try {
                    InputStream resource = Main.class.getClassLoader().getResourceAsStream("massage.json");
                    String content = IOUtils.toString(resource, StandardCharsets.UTF_8);
                    JSONArray clients = new JSONArray(content);
                    for (int j = 0; j < clients.length(); j++) {
                        JSONObject client = clients.getJSONObject(j);
                        if (!Client.containsKey(client.getInt("id")))
                            break;
                        Socket sok = Client.get(client.getInt("id"));
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sok.getOutputStream())), true);
                        JSONArray massages = client.getJSONArray("notes");
                        for (int k = 0; k < massages.length(); k++) {
                            JSONObject massage = massages.getJSONObject(k);
                            String time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
                            if (massage.getString("time").equals(time)){
                                out.println(massage.getString("note"));
                            }
                            try {
                                Thread.sleep(60000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        int i = 1;
        try {
            while (true) {
                Socket socket = s.accept();
                Client.put(i++,socket);
                System.out.println("Новое соединение установлено");
                System.out.println("Данные клиента: "+ socket.getInetAddress());
            }
        }
        finally {
            s.close();
        }
    }
}