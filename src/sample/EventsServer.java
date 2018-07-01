package sample;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

class EventsServerOne extends Thread
{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String num;
    public EventsServerOne(Socket s, String num) throws IOException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        this.num = num;
        start();
    }
    public void run() {
        try {
            String strUser, strServer;
            while(true) {
                strUser = in.readLine();
                System.out.println("Client [num=" + num + "]: " + strUser);
                if (strUser == null || strUser.equalsIgnoreCase("END")) {
                    break;
                }
                strServer = EventsServer.checkRequestCommand(strUser);
                System.out.println("ServerServer for client [num=" + num + "]: " + strServer);
                out.println(strServer);
            }
        }
        catch (IOException ex) {
            System.err.println("Error of read/write.");
        }
        finally {
            try {
                in.close();
                out.close();
                socket.close();
                System.out.println("Connection to client [num=" + num + "] is closed.");
            }
            catch (IOException ex) {
                System.err.println("Client's socket is not closed.");
            }
        }
    }
}

public class EventsServer extends Thread
{
    public static void main(String[] args) {
        Clients.init();
        Events.load();
        new EventsServer().start();
        while(server == null) {
        }
        try {
            while(server != null) {
                server.setSoTimeout(timeout);
                while(server != null && client == null) {
                    long diffMin = ChronoUnit.MINUTES.between(Events.getLoadTime(), LocalTime.now());
                    if (diffMin >= 10) {
                        Events.load();
                    }
                    try {
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException ex) {
                    }
                    if (Clients.clientsNotNull()) {
                        for(int i=0; i < Clients.getCount(); i++) {
                            Clients.Client client = Clients.getClient(i);
                            diffMin = ChronoUnit.MINUTES.between(client.getDateTime(), LocalDateTime.now());
                            if (diffMin >= 0) {
                                int id = client.getId();
                                int PORT = client.getPORT();
                                String time = client.getTime().format(formTime);
                                if (EventsServerCl.sendEventsToClient(id, PORT, time)) {
                                    client.setNextDate();
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (SocketException ex) {
            System.err.println("Error of ServerServer timeout.");
        }
        System.out.println("ServerServer timeout.");
    }
    private static DateTimeFormatter formDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static DateTimeFormatter formTime = DateTimeFormatter.ofPattern("HH:mm");
    private static DateTimeFormatter formDateTime = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final int PORT = 1233;
    private static int timeout = 10 * 60 * 1000;
    private static int count = 0;
    private static volatile ServerSocket server;
    private static volatile Socket client;
    public void run() {
        try {
            server = new ServerSocket(PORT);
            System.out.println("MultiThread ServerServer start.");
            System.out.println("Waiting for connection of clients...");
        }
        catch (IOException ex) {
            System.err.println("Error of new ServerServer socket.");
        }
        try {
            while(true) {
                client = null;
                client = server.accept();
                String num = Integer.toString(++count);
                try {
                    System.out.println("New connection is OK.");
                    System.out.println("Client [num=" + num + "] address: " + client.getInetAddress() + ".");
                    new EventsServerOne(client, num);
                }
                catch (IOException ex) {
                    client.close();
                    System.err.println("ServerServer can't connect with client [num=" + num + "].");
                }
            }
        }
        catch (IOException ex) {
            if (server == null) {
                System.out.println("ServerServer stop.");
            }
            else {
                server = null;
                System.err.println("ServerServer's error of server accept or client close.");
            }
        }
        finally {
            System.out.println("ServerServer closed.");
        }
    }
    public static void close() {
        try {
            server.close();
            server = null;
        }
        catch (IOException ex) {
            System.err.println("Error of ServerServer close.");
        }
    }
    public static String checkRequestCommand(String strComGet) {
        String response = "Command to request is not correct.";
        Pattern p = Pattern.compile("^.*SUBE([0-9]{2}[:][0-9]{2})ID([0-9]+)PORT([0-9]+).*$");
        Matcher m = p.matcher(strComGet.toUpperCase());
        if (m.matches()) {
            LocalTime time = LocalTime.parse(m.group(1), formTime);
            int id = Integer.parseInt(m.group(2));
            int PORT = Integer.parseInt(m.group(3));
            if (subscribe(id, PORT, time)) {
                return "<OK>. Subscription to events is true.";
            }
        }
        else {
            p = Pattern.compile("^.*UNSUBEID([0-9]+)PORT[0-9]+.*$");
            m = p.matcher(strComGet.toUpperCase());
            if (m.matches()) {
                int id = Integer.parseInt(m.group(1));
                if (unsubscribe(id)) {
                    return "<OK>. Subscription to events is false.";
                }
            }
        }
        return response;
    }
    private static boolean subscribe(int id, int PORT, LocalTime time) {
        if (Clients.addClient(id, PORT, time)) {
            return true;
        }
        return false;
    }
    private static boolean unsubscribe(int id) {
        if (Clients.remClient(id)) {
            return true;
        }
        return false;
    }
    private static class EventsServerCl
    {
        private static boolean sendEventsToClient(int id, int PORT, String time) {
            boolean result = false;
            String[] args = new String[]{"localhost"};
            if (args.length == 0) {
                System.err.println("Use: java Client hostname.");
                System.exit(-1);
            }
            System.out.println("ServerCl start.");
            System.out.println("Connection with client " + args[0] + ". ");
            System.out.println("Please wait...");
            try {
                Socket server = new Socket(args[0], PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
                PrintWriter out = new PrintWriter(server.getOutputStream(), true);
                System.out.println("Connection is OK. Client [id=" + id + ", PORT=" + PORT + "].");
                while(Events.onLoadStarted()) {
                }
                String strServerCl = "";
                for(int i=0; i < Events.getCount(); i++) {
                    strServerCl += Events.getEvent(i) + "\t";
                }
                while(!result) {
                    out.println("<EVENTS>: " + strServerCl + "<END>.");
                    System.out.println("ServerCl for client [id=" + id + ", time=" + time + "]: " + strServerCl);
                    if (in.readLine().equals("<OK>")) {
                        result = true;
                    }
                }
                in.close();
                out.close();
                server.close();
                System.out.println("ServerCl closed.");
            }
            catch (IOException ex) {
                System.err.println("Client is not open or closed.");
            }
            return result;
        }
    }
    private static class Clients
    {
        private static int getCount() {
            return clients.size();
        }
        private static Client getClient(int i) {
            return clients.get(i);
        }
        private static boolean addClient(int id, int PORT, LocalTime time) {
            if (!checkId(id)) {
                bAddStarted = true;
                synchronized (clients) {
                    clients.add(new Client(id, PORT, time));
                }
                bAddStarted = false;
                return true;
            }
            return false;
        }
        private static boolean remClient(int id) {
            if (checkId(id)) {
                bRemStarted = true;
                synchronized (clients) {
                    clients.remove(getNumById(id));
                }
                bRemStarted = false;
                return true;
            }
            return false;
        }
        private static boolean checkId(int id) {
            if (clientsNotNull()) {
                for(Client client : clients) {
                    if (client.getId() == id) {
                        return true;
                    }
                }
            }
            return false;
        }
        private static int getNumById(int id) {
            if (clientsNotNull()) {
                for(int i=0; i < clients.size(); i++) {
                    if (clients.get(i).getId() == id) {
                        return i;
                    }
                }
            }
            return -1;
        }
        private static boolean clientsNotNull() {
            return clients != null;
        }
        private static void init() {
            clients = new ArrayList<Client>();
        }
        private static ArrayList<Client> clients;
        private static boolean bAddStarted;
        private static boolean bRemStarted;
        private static class Client
        {
            private int id;
            private int PORT;
            private LocalTime time;
            private LocalDateTime dateTime;
            private Client (int id, int PORT, LocalTime time) {
                this.id = id;
                this.PORT = PORT;
                this.time = time;
                this.dateTime = LocalDateTime.of(LocalDate.now(), time);
            }
            private LocalDateTime getDateTime() {
                return dateTime;
            }
            private void setNextDate() {
                setDateTime(dateTime.plusDays(1));
            }
            private void setDateTime(LocalDateTime dateTime) {
                this.dateTime = dateTime;
            }
            private int getId() {
                return id;
            }
            private int getPORT() {
                return PORT;
            }
            private LocalTime getTime() {
                return time;
            }
        }
    }
    private static class Events
    {
        private static String getEvent(int i) {
            return events.remarks.get(i).getString();
        }
        private static int getCount() {
            return events.remarks.size();
        }
        private static void load() {
            events = new Remarks();
            events.loadFromResource();
            eventsLoadTime = LocalTime.now();
        }
        private static LocalTime getLoadTime() {
            return eventsLoadTime;
        }
        private static LocalTime eventsLoadTime;
        private static Remarks events;
        private static boolean onLoadStarted() {
            return Remarks.bLoadStarted;
        }
        private static class Remarks
        {
            private Remarks() {
                remarks = new ArrayList<Remark>();
            }
            private class Remark
            {
                private String string;
                private Remark(String string) {
                    this.string = string;
                }
                private String getString() {
                    return string;
                }
            }
            private void loadFromResource() {
                try {
                    bLoadStarted = true;
                    java.net.URL url = new java.net.URL("http://www.mk.ru/news/");
                    java.util.Scanner sc = new java.util.Scanner(url.openStream());
                    while(sc.hasNextLine()) {
                        Pattern p = Pattern.compile("^.*>([0-9]{2}[:][0-9]{2})<.*>([A-Za-zА-Яа-я0-9 ,.;:?!`'\"«»$]+)</.*$");
                        Matcher m = p.matcher(sc.nextLine());
                        if (m.matches()) {
                            String time = m.group(1);
                            String string = m.group(2);
                            newRemark(time + " " + string + ".");
                        }
                    }
                    sc.close();
                    bLoadStarted = false;
                }
                catch(MalformedURLException ex) {
                    System.out.println("MalformedURLException.");
                }
                catch(IOException ex) {
                    System.out.println("IOException.");
                }
            }
            private void newRemark(String string) {
                remarks.add(new Remark(string));
            }
            private ArrayList<Remark> remarks;
            private static boolean bLoadStarted;
        }
    }
}
