import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Server {
    public static void main(String[] args) {
        Server server = new Server();
        server.start(4321);
    }
    public void start(int port){
        String timeBefore=null;
        String timeAfter=null;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("New connect");
                OutputStream out = client.getOutputStream();
                PrintWriter writer = new PrintWriter(out, true);
                while (true) {
                    timeAfter = getTime();
                    if (!timeAfter.equals(timeBefore)){
                        timeBefore=timeAfter;
                        writer.println(timeBefore);
                        System.out.println(timeBefore);
                    }
                }
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    public String getTime(){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date();
        String result = dateFormat.format(date);
        return result;
    }
}