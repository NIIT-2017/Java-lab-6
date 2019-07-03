package ServerToDo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;

class ServerToDo extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private int id;
    private ArrayList<Message> clientListOfMessages;
    private ArrayList<Message> timeOver;

    public ServerToDo(Socket socket) throws IOException {
        this.socket = socket;

        //creating streams
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }


    public void run() {
        try {
            String str = in.readLine();
            System.out.println("Id " + str + " was got from the client");
            id = Integer.parseInt(str);/*
            //to not duplicate open threads for the same client
            if (Server.clientsConnected.containsKey(id)){
                Server.clientsConnected.get(id).in.close();
                Server.clientsConnected.get(id).out.close();
                Server.clientsConnected.get(id).socket.close();
                Server.clientsConnected.remove(id);
            }
            Server.clientsConnected.put(id, this);
*/

            //creating actual list of messages for the client by id
            clientListOfMessages = new ArrayList<Message>();
            timeOver = new ArrayList<Message>();
            for (Message m : Server.serverListOfMessages) {
                if (m.getId() == this.id) {
                    Calendar testCal = new GregorianCalendar();
                    if (testCal.before(m.getTime())) {
                        clientListOfMessages.add(m);
                    } else {
                        timeOver.add(m);
                    }
                }
            }
            //testing socket
            out.println("test time/test message");
            if (clientListOfMessages.isEmpty()){
                out.println(" /У Вас не запланировано никаких дел на сегодня!!!");
            }
            else {
                while (!clientListOfMessages.isEmpty()) {
                    //creating variable with current time
                    Calendar calendar = new GregorianCalendar();
                    Iterator<Message> iterator = clientListOfMessages.iterator();
                    while (iterator.hasNext()) {
                        Message nextMess = iterator.next();
                        if ((calendar.get(Calendar.HOUR_OF_DAY) == nextMess.getTime().get(Calendar.HOUR_OF_DAY)) &&
                                (calendar.get(Calendar.MINUTE) == nextMess.getTime().get(Calendar.MINUTE))) {
                            out.println(nextMess.toString());
                            //System.out.println("The message: \"" + nextMess.toString() + "\" was sent to the client");
                            timeOver.add(nextMess);
                            iterator.remove();
                        }
                    }
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                out.println(" /Дела на сегодня закончились, отдыхайте!!!");
            }
        } catch (IOException e) {
            System.err.println("Error of reading/writing");
        } finally {
            try {
                out.close();
                in.close();
                socket.close();
                //Server.clientsConnected.remove(id, this);
            } catch (IOException e) {
                System.err.println("Socket cannot be closed");
            }
        }
    }
}


//checking the connection with client
//int size = socket.getInputStream().read();
//if(size <= 0)
//{
//  socket.close();
//  break;
//}



public class Server {
    static final int PORT = 1558;
    static final String FILE_NAME = "toDoList.json";
    static ArrayList<Message> serverListOfMessages = new ArrayList<Message>();
    //static HashMap <Integer, ServerToDo>clientsConnected = new HashMap();

    public static void parseJson(String fileName){
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(fileName)){
            JSONObject object = (JSONObject)parser.parse(reader);
            JSONArray messArr = (JSONArray) object.get("activities");
            Iterator messIterator = messArr.iterator();
            Long id = 0l;
            String t = null;
            String text = null;
            while(messIterator.hasNext()) {
                JSONObject st = (JSONObject) messIterator.next();
                t = (String) st.get("time");
                id = (Long) st.get("id");
                text = (String) st.get("message");
                Message mess = new Message (id.intValue(), t, text);
                serverListOfMessages.add(mess);
            }
        }
        catch (ParseException | IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Server started");
        parseJson(FILE_NAME);
        System.out.println("ToDo list was created");
//create new thread for new client
        try {
            while (true) {
                Socket socket = s.accept();
                try {
                    System.out.println("New connection was created");
                    new ServerToDo(socket);
                }
                catch (IOException e) {
                    socket.close();
                }
            }
        }
        finally {
            s.close();
        }
    }
}