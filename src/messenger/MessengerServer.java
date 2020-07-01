package messenger;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessengerServer extends Thread {
    static private ArrayList<Message> clientMessages = new ArrayList<>();
    private Socket socket;
    private PrintWriter outgoingMessage;
    private BufferedReader incomingMessage;

    public MessengerServer(Socket socket) throws IOException {
        this.socket = socket;
        incomingMessage = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outgoingMessage = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    private static File resourcesToFile() {
        try {
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("Messeges.json");
            File temporaryFile = File.createTempFile("Temporary", ".tmp");
            temporaryFile.deleteOnExit();
            try (FileOutputStream outStream = new FileOutputStream(temporaryFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
            }
            return temporaryFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void run() {
        try {
            System.out.println("Waiting for incoming Message...");
            String id = incomingMessage.readLine();
            System.out.println("I get " + id);
            if (id == null) {
                System.out.println("Connection is closed!");
            } else {
                while (true) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    String actualTime = dateFormat.format(new Date());

                    for (Message msg : clientMessages) {
                        if (msg.getClientId().equals(id)) {
                            if (msg.getTime().equals(actualTime)) {
                                outgoingMessage.write(msg.getMsgText());
                                outgoingMessage.write("\n");
                                outgoingMessage.flush();
                            }
                        }
                    }
                    Thread.sleep(40000);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Socket isn`t closed!");
            }
        }
    }

    static public void readMessages() {
        String stringOfId;
        JSONArray messages;
        try {
            File file = resourcesToFile();
            stringOfId = FileUtils.readFileToString(file, "utf-8");
            messages = new JSONArray(stringOfId);
        } catch (IOException ex) {
            System.out.println("Error during reading " + ex.getMessage());
            return;
        }
        for (int i = 0; i < messages.length(); i++) {
            JSONObject mesJSON = (JSONObject) messages.get(i);
            Message message = new Message();
            message.setClientId((String) mesJSON.get("id"));
            message.setMsgText((String) mesJSON.get("mes"));
            message.setTime((String) mesJSON.get("time"));
            clientMessages.add(message);
        }
    }

    public static void main(String[] args) throws IOException {
        resourcesToFile();
        readMessages();
        try (ServerSocket serverSocket = new ServerSocket(1003)) {
            while (true) {
                System.out.println("The multithread server is started!");
                Socket socket = serverSocket.accept();
                try {
                    System.out.println("New connection is accepted from: " + socket.getRemoteSocketAddress());
                    new MessengerServer(socket);
                } catch (IOException e) {
                    System.err.println(e);
                    socket.close();
                }
            }
        }
    }
}

