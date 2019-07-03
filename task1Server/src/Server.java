import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {

    public void goServer(){
        try {
            ServerSocket server = new ServerSocket(1559);
            System.out.println("Сервер создан");
            //go to the endless loop to wait connections with clients
            while (true) {
                Socket client = server.accept();
                PrintWriter writer = new PrintWriter(client.getOutputStream());
                SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
                Date currDate = new Date();
                SimpleDateFormat sdfDate = new SimpleDateFormat("d MMMM yyyy");
                String time = sdfTime.format(currDate);
                String date = sdfDate.format(currDate);
                writer.println(time);
                writer.println(date);
                writer.close();
            }

        }
        catch (IOException e) {
            System.out.println("Ошибка связывания с портом 1559");
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        Server myServer = new Server();
        myServer.goServer();
    }

}
