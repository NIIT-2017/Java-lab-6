package sample;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DTimeServerOne extends Thread
{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String id;
    public DTimeServerOne(Socket s, String id) throws IOException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        this.id = id;
        start();
    }
    public void run() {
        try {
            String strUser, strServer;
            while(true) {
                strUser = in.readLine();
                System.out.println("Client [id=" + id + "]: " + strUser);
                if (strUser == null || strUser.equalsIgnoreCase("END")) {
                    break;
                }
                strServer = DTimeServer.getDateTime(strUser);
                System.out.println("Server for client [id=" + id + "]: " + strServer);
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
                System.out.println("Connection to client [id=" + id + "] is closed.");
            }
            catch (IOException ex) {
                System.err.println("Client's socket is not closed.");
            }
        }
    }
}

public class DTimeServer extends Thread
{
    public static void main(String[] args) {
        new DTimeServer().start();
        while(server == null) {
        }
        try {
            while(server != null) {
                server.setSoTimeout(timeout);
                while(server != null && client == null) {
                }
            }
        }
        catch (SocketException ex) {
            System.err.println("Error of server timeout.");
        }
        System.out.println("Server timeout.");
    }
    private static final int PORT = 1231;
    private static int count = 0;
    private static int timeout = 10 * 1000;
    private static volatile ServerSocket server;
    private static volatile Socket client;
    public void run() {
        try {
            Thread.sleep(3 * 1000);
        }
        catch (InterruptedException ex) {
        }
        try {
            server = new ServerSocket(PORT);
            System.out.println("MultiThread server start.");
            System.out.println("Waiting for connection of clients...");
        }
        catch (IOException ex) {
            System.err.println("Error of new server socket.");
        }
        try {
            while(true) {
                client = server.accept();
                String id = Integer.toString(++count);
                try {
                    System.out.println("New connection is OK.");
                    System.out.println("Client [id=" + id + "] address: " + client.getInetAddress() + ".");
                    new DTimeServerOne(client, id);
                }
                catch (IOException ex) {
                    client.close();
                    System.err.println("Server can't connect with client [id=" + id + "].");
                }
            }
        }
        catch (IOException ex) {
            if (server == null) {
                System.out.println("Server stop.");
            }
            else {
                server = null;
                System.err.println("Server's error of server accept or client close.");
            }
        }
        finally {
            System.out.println("Server closed.");
        }
    }
    public static void close() {
        try {
            server.close();
            server = null;
        }
        catch (IOException ex) {
            System.err.println("Error of server close.");
        }
    }
    public static String getDateTime(String strComGet) {
        String response = "Command to request is not correct.";
        Pattern p = Pattern.compile("^.*GETDATETIME([A-Za-z]+).*$");
        Matcher m = p.matcher(strComGet.toUpperCase());
        if (m.matches()) {
            String region = m.group(1);
            response = "Region in request is not correct.";
            if (checkValue(region)) {
                return getDTFromResource(region);
            }
        }
        return response;
    }
    private static boolean checkValue(String value) {
        if (value != null) {
            for(String region: regions) {
                if (value.equalsIgnoreCase(region)) {
                    return true;
                }
            }
        }
        return false;
    }
    private static final ArrayList<String> regions = new ArrayList<String>
        (Arrays.asList("Moscow","London","Paris")
            );
    private static String getDTFromResource(String region) {
        try {
            java.net.URL url = new java.net.URL("https://time.is/" + region);
            java.util.Scanner sc = new java.util.Scanner(url.openStream());
            while(sc.hasNextLine()) {
                Pattern p = Pattern.compile("^.*([0-9]{2}[:][0-9]{2}[:][0-9]{2}).*([0-9]{2})[ ](.*)[ ]([0-9]{4}).*$");
                Matcher m = p.matcher(sc.nextLine());
                if (m.matches()) {
                    String val = getNumOfMonth(m.group(3));
                    if (!val.equals("0")) {
                        String time = m.group(1);
                        String day = m.group(2);
                        String month = val;
                        String year = m.group(4);
                        String date = day+"."+month+"."+year;
                        String dateTime = date+" "+time;
                        sc.close();
                        return dateTime;
                    }
                }
            }
            sc.close();
        }
        catch(MalformedURLException ex) {
            System.out.println("MalformedURLException.");
        }
        catch(IOException ex) {
            System.out.println("IOException.");
        }
        return "Can't get time from server.";
    }
    private static String getNumOfMonth(String month) {
        if (month != null) {
            for(int i=0; i < months.size(); i++) {
                if (month.equalsIgnoreCase(months.get(i))) {
                    String num = Integer.toString(i+1);
                    if (i < 10) {
                        num = "0" + num;
                    }
                    return num;
                }
            }
        }
        return "0";
    }
    private static final ArrayList<String> months = new ArrayList<String>
        (Arrays.asList("ЯНВАРЬ","ФЕВРАЛЬ","МАРТ","АПРЕЛЬ","МАЙ","ИЮНЬ","ИЮЛЬ","АВГУСТ","СЕНТЯБРЬ","ОКТЯБРЬ","НОЯБРЬ","ДЕКАБРЬ")
            );
}
