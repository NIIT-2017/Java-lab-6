import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

public class Server62 {

    public static void main(String[] args) throws IOException {
        System.out.println("Server started");
        int i = 0;
        PrintWriter    out= null;
        ServerSocket server = null;
        Socket       client = null;
        ArrayList <String> str = new ArrayList<String>();
        Random rand = null;


        try {
            File file = new File("aphorisms.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bf = new BufferedReader(fileReader);

            while (bf.ready()){
                str.add(bf.readLine());
                i++;
            }
        } catch (FileNotFoundException ex){
            System.out.println("File not found");
        }


        try {
            server = new ServerSocket(1234);
        } catch (IOException e) {
            System.out.println("Binding error with port 1234");
            System.exit(-1);
            server.close();
        }

        while(true) {
                try {
                    System.out.println("Waiting for the connection");
                    client= server.accept();
                    System.out.println("Client connected");
                } catch (IOException e) {
                    System.out.println("Can't connect!");
                    System.exit(-1);
                }
            out = new PrintWriter(client.getOutputStream(),true);


            rand = new Random();
            out.println(str.get(rand.nextInt(str.size())));
            out.close();
            client.close();
        }

    }
}
