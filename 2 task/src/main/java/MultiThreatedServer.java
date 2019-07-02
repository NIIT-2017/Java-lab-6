import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public
class MultiThreatedServer extends Thread {
    private
    Socket socket;
    private
    BufferedReader in;
    private
    PrintWriter out;

    public
    MultiThreatedServer ( Socket socket ) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new FileReader("af.txt"));
        this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    public void run() {
        String aff;
        ArrayList<String> affs = new ArrayList<String>();
        while(true){
            try {
                aff = in.readLine();
                if( aff == null) break;
                affs.add(aff);
             //   System.out.println(aff);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("File was red!");
        System.out.println("Strings in array");
        for (int i = 0; i < affs.size() ; i ++){
            out.println(affs.get(i));
            System.out.println(affs.get(i));
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

