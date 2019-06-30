import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Server {

    public static void main(String[] args) throws IOException {
        System.out.println("Server started");
        ServerSocket server = null;
        Socket client = null;
        BufferedReader in = null;
        PrintWriter out = null;
        boolean flag = false;

        try {
            server = new ServerSocket(1234);
        } catch (IOException e) {
            System.out.println("Binding error with port 1234");
            System.exit(-1);
        }

        try {
            System.out.println("Waiting for the connection");
            client= server.accept();
            System.out.println("Client connected");
        } catch (IOException e) {
            System.out.println("Can't connect!");
            System.exit(-1);
        }

        in  = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
        String input,output;
        Calendar calendar = new GregorianCalendar();
        Date date ;

        while(true) {
            if (flag) {
                try {
                    System.out.print("Waiting for the connection");
                    client= server.accept();
                    System.out.println("Client connected");
                } catch (IOException e) {
                    System.out.println("Can't connect!");
                    System.exit(-1);
                }
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                calendar = new GregorianCalendar();
            }
            input = in.readLine();
            System.out.println(input);
            if (input.equals("exit")) {
                System.out.println(input);
                break;
            }
            date = calendar.getTime();
            output = ""+date;
            out.println(output);
            System.out.println(output);
            flag = true;
            in.close();
            out.close();
            client.close();
        }
        System.out.println("Server is closed");
        in.close();
        out.close();
        client.close();
        server.close();
    }
}
