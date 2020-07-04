package aphorisms;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class AphorismsServer extends Thread {
    private Socket socket;
    private PrintWriter outgoingMessage;
    private BufferedReader incommingMessage;
    static private List<String> aphorismsN = new ArrayList<>();

    public AphorismsServer(Socket socket) throws IOException {
        this.socket = socket;
        incommingMessage = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outgoingMessage = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    static private List<String> readAphorismFile() {
        try {
            BufferedReader bf = new BufferedReader(
                    new InputStreamReader(
                            ClassLoader.getSystemClassLoader().getResourceAsStream("Nietzsche.txt")));
            String line;
            while ((line = bf.readLine()) != null) {
                aphorismsN.add(line);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return aphorismsN;
    }

    public void run() {
        try {
            while (true) {
                System.out.println("Waiting for incomming Meassage...");
                String string = incommingMessage.readLine();
                System.out.println(string);
                if (string != null && !string.equals("OFF")) {
                    int randomI = (int) (Math.random() * 19);
                    System.out.println("Send to client: " + aphorismsN.get(randomI));
                    outgoingMessage.write(aphorismsN.get(randomI));
                    outgoingMessage.write("\n");
                    outgoingMessage.flush();
                } else {
                    System.out.println("Connection is closed!");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Socket isn`t closed!");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        readAphorismFile();
        try (ServerSocket serverSocket = new ServerSocket(3333)) {
            while (true) {
                System.out.println("The multithreaded server is started!");
                Socket socket = serverSocket.accept();
                try {
                    System.out.println("New connection is accepted from: " + socket.getRemoteSocketAddress());
                    new AphorismsServer(socket);
                } catch (IOException e) {
                    System.err.println(e);
                    socket.close();
                }
            }
        }
    }

}
