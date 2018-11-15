import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class ServerOne extends Thread{
    private Socket socket;
    private Client client;
    private PrintWriter out;
    private Queue<Task> tasks;

    ServerOne(Socket s, String login, Client client) throws IOException, ParseException {
        socket = s;
        this.client = client;
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

        setDaemon(true);

        if (login != null){
            tasks = new LinkedList<>();
            readTasks(login);
            start();
        }else {
            out.println("Ошибка: ваш логин не зарегистрирован или уже используется.");
            out.println("end");
        }
    }

    private void readTasks(String login) throws IOException, ParseException {
        InputStream is = getClass().getResourceAsStream(login + ".json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        JSONParser parser=new JSONParser();
        JSONObject js=(JSONObject)parser.parse(reader);
        JSONArray items=(JSONArray)js.get("tasks");

        LocalDateTime temp = LocalDateTime.now();
        for (Object i : items) {
            long seconds = (Long) ((JSONObject)i).get("time");
            String taskText = (String) ((JSONObject)i).get("task");
            Task task = new Task(seconds, taskText, temp);
            temp = task.getTimeTask();
            tasks.add(task);
        }
    }
    public void run() {
            Task task;
            while ((task = tasks.poll()) != null){
                while (LocalDateTime.now().isBefore(task.getTimeTask())){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Date date = new Date();
                out.printf("%tT", date);
                out.println(" Напоминание:");
                out.println(task.getTaskText());
                out.println();
            }
            out.println("end");
        try {
            socket.close();
        }
        catch (IOException e) {
            System.err.println("Сокет не закрыт");
        }
        client.setOnline(false);
    }
}

