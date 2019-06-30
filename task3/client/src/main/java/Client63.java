import java.io.*;
import java.net.*;

public class Client63 {
    public static void main(String[] args) throws IOException {
        System.out.println("Client started");

        Socket server = null;

//        if (args.length==0) {
//            System.out.println("Using: java Client hostname");
//            System.exit(-1);
//        }

            System.out.println("Connect to server");
        try {
            server = new Socket("localhost", 1234);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(server.getInputStream()));
                PrintWriter out =
                        new PrintWriter(server.getOutputStream(), true);
                BufferedReader inu =
                        new BufferedReader(new InputStreamReader(System.in));
            try {
                String fuser, fserver = null;
                System.out.print("Enter ID: ");
                fuser = inu.readLine();
                out.println(fuser);
                while (fuser != null) {

                    if ((fserver = in.readLine()) != null) {
                        System.out.println(fserver);
                        continue;
                    }

                }
            } catch(SocketException ex){
                out.close();
                in.close();
                inu.close();
            }
        }
        catch(SocketException e) {
            server.close();
        }
    }
}
