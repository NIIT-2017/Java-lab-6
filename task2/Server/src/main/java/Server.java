import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static ArrayList<String> aphorisms;

    public static void main(String[] args) {
        aphorisms = new ArrayList<>();
        readFile();

        if (aphorisms.size() == 0){
            System.out.println("Ошибка: файл пустой");
            return;
        }

        System.out.println("Старт сервера");

        PrintWriter out= null;
        ServerSocket server = null;
        Socket client = null;

        try {
            server = new ServerSocket(1235);
        } catch (IOException e) {
            System.out.println("Ошибка связывания с портом 1235");
            System.exit(-1);
        }

        try {
                System.out.println("Ждем соединения...");
                client= server.accept();
                if (client != null){
                    System.out.println("Клиент подключился.");
                    try {
                        out = new PrintWriter(client.getOutputStream(),true);

                        int index = (int)(Math.random() * aphorisms.size());
                        out.println(aphorisms.get(index));

                        out.close();
                        client.close();

                        System.out.println("Клиент отключился.\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        } catch (IOException e) {
            System.out.println("Не могу установить соединение.");
            System.exit(-1);
        }
    }

    private static void readFile() {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("aphorisms.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String st;
            while ((st = br.readLine()) != null) {
                aphorisms.add(st);
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения из файла.");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
