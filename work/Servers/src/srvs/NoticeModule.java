package srvs;

import javafx.scene.control.TextArea;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class NoticeModule extends IOconnection implements Runnable {
    private static int numOfListener = 0;
    private static String fileName = "notes.JSON";
    private static HashMap<Integer, Account> accounts;
    //constructor for creating main instance of NoticeModule
    public NoticeModule(TextArea taLog) {
        super(taLog);
        accounts = new HashMap<>();
        restoreNote();
        numOfListener = 0;
    }
    //constructor for creating instances of NoticeModule to serve a person
    public NoticeModule(Socket client, TextArea taLog) {
        super(taLog);
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            numOfListener++;
        } catch (IOException ex) {
            doLog("[Server] : opening input/output failed for " + client.getInetAddress());
            ex.printStackTrace();
        }
    }

    @Override
    public void serveClient(Socket client) {
        Thread service = new Thread(new NoticeModule(client, taLog));
        service.setDaemon(true);
        service.start();
    }

    private static String getListenerName(int number) {
        return "[Listener#" + number + "] : ";
    }

    @Override
    void requestProcessing(){
        int listenerNum = numOfListener;
        Account account = null;
        String input;
        //getting an id
        out.println("[Server] : Enter your ID");
        try {
            while (((input = in.readLine()) != null)) {
                if (input.equalsIgnoreCase("exit")){
                    doLog(getListenerName(listenerNum) + input);
                    out.println(input);
                    return;
                }
                try {
                    doLog(getListenerName(listenerNum) + input);
                    int id = Integer.parseInt(input);
                    if (accounts.containsKey(id)) {
                        account = accounts.get(id);
                        doLog("[Server] : listener joins to account with id : " + id);
                        out.println("[Server] : You are joined");
                        break;
                    } else {
                        doLog("[Server] :" + id + " wrong id");
                        out.println("[Server] : wrong id");
                    }
                } catch (NumberFormatException ex) {
                    out.println("[Server] : Wrong format, only numbers are valid");
                }
            }
            //Creating messenger for the person with the id
            ArrayList<LocalTime> marks = new ArrayList<>(account.getMarks());
            Collections.sort(marks);
            Account finalAccount = account;
            Thread messenger = new Thread(() -> {
                long delay;
                while (!marks.isEmpty()) {
                    delay = marks.get(0).toSecondOfDay() - LocalTime.now().toSecondOfDay();
                    if (delay < 0) {
                        delay = 0;
                    }
                    try {
                        Thread.sleep(delay * 1000);
                    } catch (InterruptedException ex) {
                        doLog("[Server] : messenger" + getListenerName(listenerNum) + " with account id :" + finalAccount.getId() + " failed with sleep()");
                    }
                    doLog("Messenger" + getListenerName(listenerNum) + finalAccount.getMessage(marks.get(0)));
                    out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).split("\\.")[0] + "]" + finalAccount.getMessage(marks.get(0)));
                    marks.remove(0);
                }
            });
            messenger.setDaemon(true);
            messenger.start();
            //wait exit command
            while ((input = in.readLine()) != null) {
                if (input.equalsIgnoreCase("exit")){
                    doLog(getListenerName(listenerNum) + input);
                    out.println(input);
                    break;
                }
                doLog("[Server] : You are already joined. Enter \"exit\" to exit");
                out.println("[Server] : You are already joined. Enter \"exit\" to exit");
            }
        } catch (IOException ex) {
            doLog("[Server] : Input / Output failed");
        }
    }

    private static void restoreNote() {
        try{
            JSONArray units = (JSONArray) new JSONParser().parse(new InputStreamReader(NoticeModule.class.getClassLoader().getResourceAsStream(fileName)));
            Iterator bookIterator = units.iterator();
            while (bookIterator.hasNext()) {
                JSONObject person_JSON = (JSONObject) bookIterator.next();
                Account account = new Account(Integer.parseInt(person_JSON.get("id").toString()));
                JSONArray notes = (JSONArray) person_JSON.get("notes");
                Iterator notesIterator = notes.iterator();
                while (notesIterator.hasNext()) {
                    JSONObject note_JSON = (JSONObject) notesIterator.next();
                    account.addNote(LocalTime.parse(note_JSON.get("time").toString()), note_JSON.get("note").toString());
                }
                accounts.put(account.getId(), account);
            }
        } catch (ParseException | IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
            requestProcessing();
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        } catch (IOException ex) {
            doLog("Input / Output closing failed");
        }
    }
}
