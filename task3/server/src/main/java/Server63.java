import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;

class ServerOne extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ServerOne(Socket s) throws IOException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    public void run() {
        ArrayList<String> tm = new ArrayList<String>();
        ArrayList<String> tx = new ArrayList<String>();
        try {
            String str = in.readLine();
            for (int i = 0; i< Server63.id.size();i++) {
                if (Server63.id.get(i).equals(str)) {
                    tm.add(Server63.time.get(i));
                    tx.add(Server63.tasks.get(i));
                }
            }

            while (true) {
                String txt = null;
                Date date = new Date();
                String dt =date.getHours() + ":" + date.getMinutes()+":"+date.getSeconds();

                for (String j : tm) {
                    if (j.equals(dt)) {
                        txt = tx.get(tm.indexOf(j));
                        System.out.println(txt);
                        out.println(txt);
                        Thread.sleep(1000);
                        break;
                    }
                }

            }

        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        catch(InterruptedException e){
             System.err.println(e.getMessage());
            }

        finally {
            try {
                socket.close();
            }
            catch (IOException e) {
                System.err.println("Socket is not closed");
            }
        }
    }
}

public class Server63 {
    static final int PORT = 1234;
    static ArrayList<String> id = new ArrayList<String>();
    static ArrayList<String> time = new ArrayList<String>();
    static ArrayList<String> tasks = new ArrayList<String>();
    static int flag = 0;

    public static void main(String[] args) throws IOException {
        int i = 0;

        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Multithreaded server started");


        try {
            File file = new File("tasks.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bf = new BufferedReader(fileReader);

            while (bf.ready()){
                String[] str = bf.readLine().split("-");
                id.add(str[0]);
                time.add(str[1]);
                tasks.add(str[2]);
                i++;
            }
        } catch (FileNotFoundException ex){
            System.out.println("File not found");
        }

        try {
            while (true) {
                Socket socket = s.accept();
                try {
                    System.out.println("New connection established");
                    System.out.println("Client data: "+
                            socket.getInetAddress());
                    new ServerOne(socket);
                }
                catch (IOException e) {
                    socket.close();
                }
            }
        }
        finally {
            s.close();
        }
    }
}
