import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class MonoThreadClientHandler implements Runnable {

    private static Socket clientDialog;

    public MonoThreadClientHandler(Socket client) {
        MonoThreadClientHandler.clientDialog = client;
    }

    //@Override
    public void run() {

        try {
            System.out.println(clientDialog.getLocalAddress().toString());
            DataOutputStream out = new DataOutputStream(clientDialog.getOutputStream());
            DataInputStream in = new DataInputStream(clientDialog.getInputStream());
            String entry = in.readUTF();
            System.out.println("READ from clientDialog message - " + entry);
            if (entry.equals("aphorism")) {
                new Aphorism(clientDialog);
                out.flush();
            }
            if (entry.equals("time")) {
                new Time(clientDialog);
                out.flush();
            }
            if (Pattern.matches("id.*", entry)) {
                new Registration(clientDialog);
                try{
                    FileWriter writer = new FileWriter("registration.txt", true);
                    writer.write(entry+clientDialog.getLocalAddress().toString());
                    writer.write("\n");
                    writer.flush();
                    writer.close();
                }
                catch(IOException ex){
                    System.out.println(ex.getMessage());
                }
                out.flush();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            in.close();
            out.close();
            clientDialog.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Time extends Thread{
    private Socket socket;
    private PrintWriter out;

    public Time(Socket socket) throws IOException {
        this.socket = socket;
        out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    public void run(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-H-m-s-S");
        String stringDate = sdf.format(date);
        out.println(stringDate);
    }
}

class Registration extends Thread{
    private Socket socket;
    private PrintWriter out;

    public Registration(Socket socket) throws IOException {
        this.socket = socket;
        out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    public void run(){
        String message = "The connection is established and the ID has been registered";
        out.println(message);
    }
}

class Aphorism extends Thread{
    private Socket socket;
    private PrintWriter out;

    public Aphorism(Socket socket) throws IOException {
        this.socket = socket;
        out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    public void run(){
        String aphorism = null;
        int random = 1+(int)(Math.random()*99);
        InputStream in = getClass().getResourceAsStream("aphorism.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        for(int i = 0; i < random; ++i) {
            try {
                br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            aphorism = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(aphorism);
        out.println(aphorism);
    }
}