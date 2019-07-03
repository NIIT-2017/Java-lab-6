import java.io.*;
import java.net.Socket;

public class client_3 {

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main (String [] args) throws IOException {
        if (args.length!=1) {
            System.out.println("Use: java cient_id");
            System.exit(-1);
        }
        System.out.println("client id" +args[0]+ " is started");
        String ID = args[0];
        //String ID="1";
        System.out.println("client id" +ID+ " is started");
        Socket clientSocket =null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            clientSocket = new Socket("localhost", 5555);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())),true);

            out.println(ID);
            String input;
            while((input = in.readLine())!=null){
                System.out.println("server: "+input);
            }
        } finally {
            System.out.println("client is closed");
            in.close();
            out.close();
            clientSocket.close();
        }
    }
}
