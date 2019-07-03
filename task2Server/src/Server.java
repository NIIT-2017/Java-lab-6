import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ArrayList<String> phrasesList;
    private int port = 1234;
    ServerSocket server;

    public Server(String nameOfFile){
        //reading file with phrases and creating arraylist with phrases
        this.phrasesList = new ArrayList<>();
        BufferedReader breader = null; //buffered stream for reading phrases from the file
        String tmpString;
        try {
            File file = new File(nameOfFile);
            breader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
            while ((tmpString = breader.readLine()) != null) {
                phrasesList.add(tmpString);
            }
            breader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goServer(){
        try {
            server = new ServerSocket(port);
            System.out.println("Сервер создан");
            //go to the endless loop to wait connections with clients
            while (true) {
                Socket client = server.accept();
                PrintWriter writer = new PrintWriter(client.getOutputStream());
                //getting random phrase from the list
                int rand = (int) (Math.random() * phrasesList.size());
                String phrase = phrasesList.get(rand);
                //sending phrase to the client
                writer.println(phrase);
                writer.close();
                client.close();
                //System.out.println(phrase + " это фраза у сервера");
            }
        }
        catch (IOException e) {
            System.out.println("Ошибка связывания с портом " + port);
            System.exit(-2);
        }
        finally {
            try {
                server.close();
            }
            catch(IOException e){
                System.out.println("Ошибка закрытия");
                System.exit(-3);
            }
        }
    }

    public static void main(String[] args) {
        Server myServer = new Server("smart.txt");
        myServer.goServer();
    }
}
