import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.PrintWriter;
        import java.net.ServerSocket;
        import java.net.Socket;
        import java.util.Calendar;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Старт сервера");
        //BufferedReader in=null;
        PrintWriter out=null;

        //Calendar cal = Calendar.getInstance();
       // System.out.println(cal.getTime());
        ServerSocket server=null;
        Socket client=null;

        try {
            server=new ServerSocket(1234);

        } catch (IOException e) {
            System.out.println("Ошибка связывания с портом 1234");
            System.exit(-1);
        }

        try {
            System.out.println("Ждем соединения");
            client=server.accept();
            System.out.println("Клиент подключился");
        } catch (IOException e) {
            System.out.println("Не могу установить соединение");
            System.exit(-1);
        }

        out = new PrintWriter(client.getOutputStream(),true);
        Calendar cal = Calendar.getInstance();
        out.println(cal.getTime());
        out.close();
        client.close();
    }
}