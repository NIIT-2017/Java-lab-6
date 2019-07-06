import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ArrayList<String> phrasesList;
    private int port = 1234;
    private ServerSocket server;
    private InputStreamReader inputStreamReaderServ;
    private BufferedReader readerServ;
    private PrintWriter writerServ;

    public Server(String nameOfFile){
        //reading file with phrases and creating arraylist with phrases
        this.phrasesList = new ArrayList<>();
        BufferedReader breader = null; //buffered stream for reading phrases from the file
        String tmpString;
        try {
            //creating server socket
            server = new ServerSocket(port);
            System.out.println("Сервер создан");
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
            Socket client = server.accept();
            System.out.println("Клиент подключился");
            String input, output;
            //creating streams for client
            inputStreamReaderServ = new InputStreamReader(client.getInputStream());
            readerServ = new BufferedReader(inputStreamReaderServ);
            writerServ = new PrintWriter(client.getOutputStream(), true);
            //go to the endless loop to wait connections with client
            while (true) {
                input = readerServ.readLine();
                if (input.equals("exit")) {
                    break;
                }
                //getting random phrase from the list
                int rand = (int) (Math.random() * phrasesList.size());
                String phrase = phrasesList.get(rand);
                //sending phrase to the client
                writerServ.println(phrase);
                }
            readerServ.close();
            writerServ.close();
            client.close();
            }
        catch (IOException e) {
            e.printStackTrace();
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
