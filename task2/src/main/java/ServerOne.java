import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Random;

class ServerOne extends Thread {
    private Socket socket;
    private PrintWriter out;
    private List<String> lines=null;


    public ServerOne(Socket s) throws IOException {
        socket = s;
        try {
            URL resource = getClass().getResource("/wisdom.txt");
            this.lines = Files.readAllLines(Paths.get(resource.toURI()));
        } catch (URISyntaxException e) {
        e.printStackTrace();
        }
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    public void run() {
        try {
            while (true) {
                Date date = new Date();
                Random ran = new Random(date.getTime());
                int min = 0;
                int max = lines.size()-1;
                int diff = max - min;
                int i = ran.nextInt(diff + 1) + min;
                out.println(lines.get(i));
                out.println("\n");
                Thread.sleep(2000);
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            }
            catch (IOException e) {
                System.err.println("Сокет не закрыт");
            }
        }
    }
}