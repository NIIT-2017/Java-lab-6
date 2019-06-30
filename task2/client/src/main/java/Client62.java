import java.io.*;
import java.net.*;

public class Client62 {
    public static void main(String[] args) throws IOException {
        System.out.println("Client started");
        try {
            Socket server = null;


            server = new Socket("localhost", 1234);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(server.getInputStream()));;

            String fserver;

            fserver = in.readLine();
            System.out.println(fserver);

            in.close();
            server.close();
            server = null;
        }catch(IOException ex){
                System.out.println("IOExeption catched!");
        }
    }
}
