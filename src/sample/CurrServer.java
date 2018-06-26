package sample;

import java.io.*;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CurrServerOne extends Thread
{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String id;
    public CurrServerOne(Socket s, String id) throws IOException {
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
                strServer = CurrServer.getRate(strUser);
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

public class CurrServer
{
    static final int PORT = 1234;
    private static int count = 0;
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("MultiThread server start.");
        System.out.println("Waiting for connection of clients...");
        try {
            while(true) {
                Socket client = server.accept();
                String id = Integer.toString(++count);
                try {
                    System.out.print("New connection is OK. ");
                    System.out.println("Client [id=" + id + "] address: " + client.getInetAddress() + ".");
                    new CurrServerOne(client, id);
                }
                catch (IOException ex) {
                    client.close();
                    System.out.println("Client [id=" + id + "] is closed.");
                }
            }
        }
        finally {
            server.close();
            System.out.println("Server closed.");
        }
    }

    private static final ArrayList<String> values = new ArrayList<String>
            (Arrays.asList("RUB","USD","EUR","GBP","CNY","INR","CHF"));

    private static boolean checkVal(String val) {
        if (val != null) {
            for(String value: values) {
                if (val.equalsIgnoreCase(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getRate(String strComGet) {
        String response = "Do not understand. Please try again.";
        Pattern p = Pattern.compile("^.*GETFROM([A-Z]{3})TO([A-Z]{3}).*$");
        Matcher m = p.matcher(strComGet.toUpperCase());
        if (m.matches()) {
            String from = m.group(1);
            String to = m.group(2);
            if (checkVal(from) && checkVal(to)) {
                String rate = getRateFromTo(from, to);
                if (rate.equals("0")) {
                    response = "Don`t get rate from https://www.xe.com... Please try again later.";
                }
                else {
                    response = "1 " + from + " = " + rate + " " + to + ".";
                }
            }
        }
        return response;
    }

    private static String getRateFromTo(String from, String to) {
        try {
            java.net.URL url = new java.net.URL(
                    "https://www.xe.com/currencyconverter/"
                            + "convert/?Amount=1&From=" + from
                            + "&To=" + to);
            java.util.Scanner sc = new java.util.Scanner(url.openStream());
            while(sc.hasNextLine()) {
                String strVal = sc.nextLine();
                Pattern p = Pattern.compile("^.*"+from+"\\s=\\s([0-9]+\\.[0-9]+)\\s"+to+"</.*$");
                Matcher m = p.matcher(strVal);
                if (m.matches()) {
                    String strRate = m.group(1);
                    sc.close();
                    return strRate;
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
        return "0";
    }
}
