//Задание 2
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

class ServerOne extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Random random = new Random();
    private int num;
    ArrayList source = new ArrayList<String>();

    public ServerOne(Socket s) throws IOException {
        try {
            File file = new File("source.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                source.add(line);
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    public void run() {
        try {
            while (true) {
                String str = in.readLine();
                if (str.equals("END"))
                    break;
                num = random.nextInt(source.size());
                out.println("Сервер: " + source.get(num));
            }
            System.out.println("Соединение закрыто");
        }
        catch (IOException e) {
            System.err.println("Ошибка чтения/записи");
        }
        finally {
            try {
                socket.close();
            }
            catch (IOException e) {
                System.err.println("Сокет не закрыт");
            }
        }
    }
}

public class Server {
    static final int PORT = 3345;

    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Мультипоточный сервер стартовал");
        try {
            while (true) {
                Socket socket = s.accept();
                try {
                    System.out.println("Новое соединение установлено");
                    System.out.println("Данные клиента: "+
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