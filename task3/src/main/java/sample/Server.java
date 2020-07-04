package sample;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

class ServerOne extends Thread{
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Map<Integer, List<Reminders>> map = new HashMap<>();

    public ServerOne(Socket s) throws IOException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    public void run(){
        try {
            int id = Integer.parseInt(in.readLine());
            System.out.println("id: " + id);
            parseJSON();
            if (map.containsKey(id)) {
                List<Reminders> list = map.get(id);
                while(true){
                    for (Reminders reminder : list) {
                        Date date = new Date();
                        DateFormat formatter = new SimpleDateFormat("HH:mm");
                        String now = formatter.format(date);
                        if (reminder.getTime().equals(now)) {
                            //System.out.println(reminder.getReminder());
                            out.println(reminder.getReminder());
                        }
                    }
                }
            } else {
                out.println("По id = " + id + " не найдено напоминаний.");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void parseJSON() throws IOException, ParseException {
        File file = getResourceAsFile("reminders.json");
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(file));
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray arrayReminders = (JSONArray) jsonObject.get("reminders");
        for(int i = 0 ; i < arrayReminders.size(); i++){
            JSONObject object = (JSONObject) arrayReminders.get(i);
            Long temp = (Long)object.get("id");
            int id = temp.intValue();
            String time = (String) object.get("time");
            String reminder = (String) object.get("reminder");
            List<Reminders> value;
            if (map.containsKey((Object)id)) {
                value = map.get(id);
            } else {
                value = new ArrayList<>();
            }
            value.add(new Reminders(time, reminder));
            map.put(id, value);
        }
    }

    public File getResourceAsFile(String name) throws IOException {
        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(name);
        if (inputStream == null) { return null; }
        File fileTmp = File.createTempFile(String.valueOf(inputStream.hashCode()), ".tmp");
        fileTmp.deleteOnExit();
        FileOutputStream outStream = new FileOutputStream(fileTmp);
        byte[] buffer = new byte[512];
        int data;
        while ((data = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, data);
        }
        outStream.close();
        return fileTmp;
    }
}

public class Server {
    private static final int PORT = 8000;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Мультипоточный сервер стартовал");
        try{
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Новое соединение установлено");
                System.out.println("Данные клиента: "+ socket.getInetAddress());
                new ServerOne(socket);
            }
        }
        finally {
            serverSocket.close();
        }
    }
}