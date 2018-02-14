import java.io.*;
import java.net.*;
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
        try {
            while (true) {
                String str = in.readLine();
                if (str.equals("END"))
                    break;
                Date date = new Date();
                System.out.println(date.toString());
                out.println(date.toString());
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

public class ClockServer {
    static final int PORT = 3456;
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