package sample;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class EventsClient1 {
    public static void main(String[] args) {
        new EventsClient(1001, 1301).start();
    }
}

class EventsClient2 {
    public static void main(String[] args) {
        new EventsClient(1002, 1302).start();
    }
}

class EventsClient3 {
    public static void main(String[] args) {
        new EventsClient(1003, 1303).start();
    }
}

public class EventsClient
{
    private int id;
    private int PORT;
    private String sign;
    public EventsClient(int id, int PORT) {
        this.id = id;
        this.PORT = PORT;
        this.sign = "ID" + Integer.toString(id) + "PORT" + Integer.toString(PORT);
    }
    private EventsClientServ eventsClientServ;
    private EventsClientCl eventsClientCl;
    public void start() {
        eventsClientServ = new EventsClientServ(id, PORT);
        eventsClientCl = new EventsClientCl(sign);
        eventsClientServ.start();
        try {
            Thread.sleep(1 * 1000);
        } catch (InterruptedException ex) {
        }
        eventsClientCl.start();
    }
    public void close() {
        eventsClientServ.close();
        eventsClientCl.close();
    }
}

class News
{
    private static String news;
    public static void setNews(String snew) {
        news = snew;
    }
    public static String getNews() {
        return news;
    }
}

class EventsClientServOne extends Thread
{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    public EventsClientServOne(Socket s) throws IOException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }
    public void run() {
        try {
            String strServerCl;
            while(true) {
                strServerCl = in.readLine();
                System.out.println("ServerCl: " + strServerCl);
                if (strServerCl.contains("<EVENTS>: ") && strServerCl.contains("<END>.")) {
                    News.setNews(strServerCl);
                    out.println("<OK>");
                }
                if (strServerCl == null || strServerCl.equalsIgnoreCase("END") || strServerCl.contains("<END>.")) {
                    break;
                }
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
                System.out.println("Connection to ServerCl is closed.");
            }
            catch (IOException ex) {
                System.err.println("ServerCl socket is not closed.");
            }
        }
    }
}

class EventsClientServ extends Thread
{
    private int id;
    private int PORT;
    public EventsClientServ(int id, int PORT) {
        this.id = id;
        this.PORT = PORT;
    }
    private volatile ServerSocket server;
    private volatile Socket client;
    public void run() {
        try {
            server = new ServerSocket(PORT);
            System.out.println("MultiThread ClientServ [id=" + id + ", PORT=" + PORT + "] start.");
            System.out.println("Waiting for connection of ServerCl...");
        }
        catch (IOException ex) {
            System.err.println("Error of new ServerCL socket.");
        }
        try {
            while(true) {
                client = server.accept();
                try {
                    System.out.println("New connection is OK.");
                    System.out.println("ServerCL address: " + client.getInetAddress() + ".");
                    new EventsClientServOne(client);
                }
                catch (IOException ex) {
                    client.close();
                    System.err.println("ClientServ can't connect with ServerCl.");
                }
            }
        }
        catch (IOException ex) {
            if (server == null) {
                System.out.println("ClientServ stop.");
            }
            else {
                server = null;
                System.err.println("ClientServ error of accept or close.");
            }
        }
        finally {
            System.out.println("ClientServ closed.");
        }
    }
    public void close() {
        try {
            server.close();
            server = null;
        }
        catch (IOException ex) {
            System.err.println("Error of ClientServ close.");
        }
    }
}

class EventsClientCl extends Thread
{
    private String sign;
    public EventsClientCl(String sign) {
        this.sign = sign;
    }
    private volatile Socket server;
    private boolean clClWorkStat;
    public void run() {
        boolean clClWorkStat = true;
        String[] args = new String[]{"localhost"};
        if (args.length == 0) {
            System.err.println("Use: java Client hostname.");
            System.exit(-1);
        }
        System.out.println("ClientCl " + sign + " start.");
        System.out.println("Connection with ServerServer " + args[0] + ". ");
        System.out.println("Please wait...");
        try {
            BufferedReader inu = new BufferedReader(new InputStreamReader(System.in));
            String strUser, strServer;
            while(clClWorkStat) {
                System.out.print("ClientCL: ");
                strUser = inu.readLine();
                if (strUser == null || checkComEnd(strUser)) {
                    System.exit(-1);
                    clClWorkStat = false;
                }
                if (checkComGet(strUser)) {
                    System.out.println(News.getNews());
                }
                if (checkComSub(strUser)) {
                    server = new Socket(args[0], 1233);
                    BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
                    PrintWriter out = new PrintWriter(server.getOutputStream(), true);
                    System.out.println("Connection is OK.");
                    out.println(strUser + sign);
                    strServer = in.readLine();
                    System.out.println("ServerServer: " + strServer);
                    out.print("END");
                    in.close();
                    out.close();
                    server.close();
                }
            }
            inu.close();
        }
        catch (IOException ex) {
            System.out.println("ServerServer is not open or closed. Please try again later.");
        }
        finally {
            System.out.println("ClientCl closed.");
        }
    }
    public void close() {
        try {
            clClWorkStat = false;
            server.close();
            server = null;
        }
        catch (IOException ex) {
            System.err.println("Error of ClientCl close.");
        }
    }
    private boolean checkComGet(String strComGet) {
        if (strComGet.equalsIgnoreCase("GETNEWS")) {
            return true;
        }
        return false;
    }
    private boolean checkComSub(String strComSub) {
        Pattern p = Pattern.compile("^.*SUBE[0-9]{2}[:][0-9]{2}.*$");
        Matcher m = p.matcher(strComSub.toUpperCase());
        if (m.matches()) {
            return true;
        }
        else {
            p = Pattern.compile("^.*UNSUBE.*$");
            m = p.matcher(strComSub.toUpperCase());
            if (m.matches()) {
                return true;
            }
        }
        return false;
    }
    private boolean checkComEnd(String checkCom) {
        if (checkCom != null) {
            for(String commandEnd: commandsEnd) {
                if (checkCom.equalsIgnoreCase(commandEnd)) {
                    return true;
                }
            }
        }
        return false;
    }
    private final ArrayList<String> commandsEnd = new ArrayList<String>
            (Arrays.asList("STOP", "CLOSE", "EXIT", "END"));
}
