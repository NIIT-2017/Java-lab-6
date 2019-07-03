
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ServerOne extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<Reminder> reminderListOne;
    private String ID;
    static long t_Server_start;

    public ServerOne(Socket s, ArrayList<Reminder> reminderList ) throws IOException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();
        this.reminderListOne=reminderList;

    }

    public void run() {
        try {
            this.ID = in.readLine();
                System.out.println("Recieved client ID: " + this.ID);
                reminderListOne=Reminder.ReminderSortedListByOneID(reminderListOne, this.ID );

            for (Reminder item : reminderListOne){
                try {
                    long t_now = System.currentTimeMillis() ;
                    long pause = (t_now-t_Server_start);
                    if(item.reminderTime*1000>pause) {
                        Thread.sleep(item.reminderTime * 1000 - pause);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(item.remainderId+" "+item.reminderTime+" "+item.reminderMessage);
                out.println(item.remainderId+" "+item.reminderTime+" "+item.reminderMessage);

            }

            System.out.println("there are no more reminders  for"+this.ID);
            out.println("there are no more reminders");
            System.out.println("connection with "+this.ID+" closed");
            this.in.close();
            this.out.close();
            this.socket.close();
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

public class MultiServer {

    static final int PORT = 5555;
    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Мультипоточный сервер стартовал");

        ServerOne.t_Server_start=System.currentTimeMillis() ;
        String FILENAME = "reminder.json";
        ArrayList<Reminder> reminderList = Reminder.ReminderSortedList(FILENAME);

        try {
            while (true) {
                Socket socket = s.accept();
                try {
                    System.out.println("Новое соединение установлено");
                    System.out.println("Данные клиента: "+socket.getInetAddress());
                    new ServerOne(socket, reminderList);
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
