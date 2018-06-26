package sample;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AphoServerOne extends Thread
{
    public enum Command {
        GETA, GETALL, NULL
    }
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String id;
    public AphoServerOne(Socket s, String id) throws IOException {
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
                if (strUser == null || checkComEnd(strUser)) {
                    break;
                }
                Command command = Command.NULL;
                if (checkComGet(strUser)) {
                    command = Command.valueOf(strUser.toUpperCase());
                }
                switch(command) {
                    case GETA:
                        strServer = AphoServer.getAphorism(randNum(AphoServer.getAphorismsCount()));
                        System.out.println("Server for client [id=" + id + "]: " + strServer);
                        out.println(strServer);
                        break;
                    case GETALL:
                        strServer = "";
                        for(int i=0; i < AphoServer.getAphorismsCount(); i++) {
                            strServer += AphoServer.getAphorism(i) + "\t";
                        }
                        System.out.println("Server for client [id=" + id + "]: " + strServer);
                        out.println(strServer);
                        break;
                    case NULL:
                        strServer = "I do not understand. Please try again.";
                        System.out.println("Server for client [id=" + id + "]: " + strServer);
                        out.println(strServer);
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
                System.out.println("Connection to client [id=" + id + "] is closed.");
            }
            catch (IOException ex) {
                System.err.println("Client's socket is not closed.");
            }
        }
    }
    private boolean checkComEnd(String com) {
        return equals(com, "END");
    }
    private boolean checkComGet(String com) {
        boolean check = equals(com, "GETA");
        return check | equals(com, "GETALL");
    }
    private boolean equals(String value, String sample) {
        return value.equalsIgnoreCase(sample);
    }
    private int randNum(int count) {
        return new Random().nextInt(count);
    }
}

public class AphoServer
{
    static final int PORT = 1232;
    private static int count = 0;
    public static void main(String[] args) throws IOException {
        Aphorisms.load();
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("MultiThread server start.");
        System.out.println("Waiting for connection of clients.");
        try {
            while(true) {
                Socket client = server.accept();
                String id = Integer.toString(++count);
                try {
                    System.out.print("New connection is OK. ");
                    System.out.println("Client [id=" + id + "] address: " + client.getInetAddress() + ".");
                    new AphoServerOne(client, id);
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
    public static String getAphorism(int i) {
        return Aphorisms.getAphorism(i);
    }
    public static int getAphorismsCount() {
        return Aphorisms.getCount();
    }
    private static class Aphorisms
    {
        private static String getAphorism(int i) {
            return aphorisms.remarks.get(i).getIdea();
        }
        private static int getCount() {
            return aphorisms.remarks.size();
        }
        private static void load() {
            aphorisms = new Remarks();
            aphorisms.loadRemarks();
        }
        private static Remarks aphorisms;
        private static class Remarks
        {
            private Remarks() {
                remarks = new ArrayList<Remark>();
            }
            private class Remark
            {
                private String idea;
                private Remark(String idea) {
                    this.idea = idea;
                }
                private String getIdea() {
                    return idea;
                }
            }
            private void loadRemarks() {
                try {
                    String pathName = "C:/Java/Lab6/src/sample/";
                    pathName += "Aphorisms.txt";
                    FileReader fr = new FileReader(pathName);
                    Scanner scan = new Scanner(fr);
                    while (scan.hasNextLine()) {
                        String strVal = scan.nextLine();
                        Pattern p = Pattern.compile("^([A-Za-z\\s,]+[.!]).*$");
                        Matcher m = p.matcher(strVal);
                        if (m.matches()) {
                            String idea = m.group(1);
                            if (checkRemarkData(idea)) {
                                newRemark(idea);
                            }
                        }
                    }
                    scan.close();
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            private boolean checkRemarkData(String idea) {
                boolean check;
                check = notNullAndNotEmpty(idea);
                return check;
            }
            private boolean notNullAndNotEmpty(String strVal) {
                return (strVal != null && !strVal.equals(""));
            }
            private void newRemark(String idea) {
                remarks.add(new Remark(idea));
            }
            private ArrayList<Remark> remarks;
        }
    }
}
