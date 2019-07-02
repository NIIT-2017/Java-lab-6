import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public
class Server {
    static final int PORT = 1234;
    static Integer id=0;
    public static
    void main ( String[] args ) throws IOException {
        ServerSocket s=new ServerSocket(PORT);
        System.out.println("Multiserver is starting");
        try{
            while (true){
                Socket socket=s.accept();
                System.out.println("New connection");
                new ServerNew(socket,id);
                id++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            s.close();
        }

    }
}

