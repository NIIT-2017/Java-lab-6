import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    static final int PORT=1234;
    static final int MESSAGES=3; //waiting 3 messages from server

    public static void main(String[] args) throws IOException {
        System.out.println("Клиент стартовал");
        //String myID=args[1]; //for server  FOR JAR
        String myID=new String("1");

        Socket server = null;
        //FOR JAR
        //if (args.length == 0) {
        //    System.out.println("Использование Java Server_IP");
        //    System.exit(-1);
        //}
       // System.out.println("Cоединяемся с сервером " + args[0]);

        System.out.println("Cоединяемся с сервером 127.0.0.1");

        try {
            //server = new Socket(args[0], PORT);
            server = new Socket("127.0.0.1", PORT);
        } catch (UnknownHostException e) {
            System.out.println("Неизвестный хост");
            System.exit(-1);
        }
        catch (NoRouteToHostException e) {
            System.out.println("Нет связи");
            System.exit(-1);
        }
        catch (ConnectException e) {
            System.out.println("Ошибка соединения");
            System.exit(-1);
        }
        catch (IOException e) {
            System.out.println("Ошибка ввода-вывода");
            System.exit(-1);
        }

        BufferedReader in=null;
        PrintWriter out=null;

        try {
            in=new BufferedReader(new InputStreamReader(server.getInputStream()));
            out=new PrintWriter(server.getOutputStream(),true);
            }catch (IOException e){
            System.out.println("Ошибка создания потоков");
            System.exit(-1); }


        if(!(myID.equals(""))) //if args[1]!=""
            out.println(myID); //to server my ID
        else
            System.exit(-1);

        String fromServer;
        int countMessage=0;
        while (true){ //listening to server.

            fromServer=in.readLine();
            boolean b=(fromServer==null);
            if(!b){
                countMessage++;
                System.out.println(fromServer);
            }
            if(countMessage==MESSAGES) //After the third message  "exit"
                break;
        }
        out.close();
        in.close();
        server.close();

    }
}
