import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

class Diary {

    public int IDClient;
    public String Message;
    public String AlertTime;

    Diary(int IDClient, String Message, String AlertTime) {
        this.IDClient = IDClient;
        this.Message = Message;
        this.AlertTime = AlertTime;
    }
}

class Time {

    public String AlertTime;
    public ArrayList<Diary> diary;

    Time(String AlertTime) {
        this.AlertTime = AlertTime;
        diary = new ArrayList<>();
    }

    public static void addClients(Diary d) {

        Time time = SocketServer.times.stream().filter(groups -> groups.AlertTime.equals(d.AlertTime)).findFirst().orElse(null);
        time.diary.add(d);
    }

}

class ScheduledTask extends TimerTask {
    public String reportDate;
    public SocketServer socketServer;
    public static String reportDate_prev;

    private void sendMessage(Socket socket, String msg, int id, String reportDate){
        if (socket == null) return;
        try {
            OutputStream outputStream = socket.getOutputStream();
            String message = reportDate + ": Client " + id + " не забудьте " + msg + "\n";
            outputStream.write(message.getBytes());
            outputStream.flush();
            } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    @Override
    public void run() {
        DateFormat now = new SimpleDateFormat("HH:mm");
        Date today = Calendar.getInstance().getTime();
        reportDate = now.format(today);
        for(Time i:SocketServer.times){

            String t = i.AlertTime;

        if (reportDate.replace("\"","").equals(t.replace("\"",""))) {
          if  (!(reportDate.equals(reportDate_prev))) {
                reportDate_prev = reportDate;
                i.diary.forEach(diary -> sendMessage(SocketServer.hashmap.get(diary.IDClient),diary.Message,diary.IDClient, reportDate));
                    }
                }

        }

    }
}

public class SocketServer extends Thread  {
    public static final int PORT_NUMBER = 8081;
    protected Socket socket;
    public static ArrayList<Diary> diares = new ArrayList<Diary>();
    public static ArrayList<Time> times = new ArrayList<>();
    public static Map<Integer,Socket> hashmap = new HashMap<>();

    private SocketServer(Socket socket) {
         this.socket = socket;
        System.out.println("New client connected from " + socket.getInetAddress().getHostAddress());
        start();
    }

    public void run() {

        InputStream in = null;
        OutputStream out = null;
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String request;
            String search;
            Timer time = new Timer();
            ScheduledTask st = new ScheduledTask();
            st.socketServer=this;

            time.schedule(st, 0, 5000);

            while ((request = br.readLine()) != null) {
                search=request.replaceAll("\\D+","");
                int key= Integer.parseInt(search);
                hashmap.put(key,socket);
                System.out.println("Message received:" + request);
                request += '\n';
                out.write(request.getBytes());

            }

        } catch (IOException ex) {
            System.out.println("Unable to get streams from client");
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException{
        String File = "diary.json";
        readfile(File);

        System.out.println("SocketServer Started");
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT_NUMBER);
            while (true) {
                new SocketServer(server.accept());
            }
        } catch (IOException ex) {
            System.out.println("Unable to start server.");
        } finally {
            try {
                if (server != null)
                    server.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void readfile(String File) {
        String AlertTime_prev = "";

        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            InputStream is = cl.getResourceAsStream(File);
            BufferedReader bf = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            JsonElement jsonElement = new JsonParser().parse(bf);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            if (jsonArray != null) {
                for (JsonElement i : jsonArray) {
                    JsonObject jsonObject = i.getAsJsonObject();
                    int IDClient = Integer.parseInt(String.valueOf(jsonObject.get("IDClient")));
                    String AlertTime = String.valueOf(jsonObject.get("AlertTime"));
                    String Message = String.valueOf(jsonObject.get("Message"));
                    Diary d = new Diary(IDClient, Message, AlertTime);
                    diares.add(d);
                    if (!(AlertTime.equals(AlertTime_prev))) {
                        AlertTime_prev = AlertTime;
                        Time t = new Time(AlertTime);
                        times.add(t);
                    }
                    Time.addClients(d);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}