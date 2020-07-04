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
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MessengerServer extends Thread {
    static volatile ConcurrentHashMap<String, MessengerServer> clientsMap = new ConcurrentHashMap();
    static private ArrayList<Message> clientMessages = new ArrayList<>();
    private Socket socket;
    private PrintWriter outgoingMessage;
    private BufferedReader incomingMessage;
    private String clientId;

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
            if(mesJSON.has("isLast") && (boolean) mesJSON.get("isLast")){
                message.setLast(true);
            }else {
                message.setLast(false);
            }
            clientMessages.add(message);
        }
    }

    public static void main(String[] args) throws IOException {
        resourcesToFile();
        readMessages();
        Thread checkingMessage = new Thread (new Runnable() {
            public void run() {
                System.out.println("This is side thread!");
                while (true) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    String actualTime = dateFormat.format(new Date());
                    System.out.println("Checking messages at " + actualTime);
                    for (Message msg : clientMessages) {
                        if (msg.getTime().equals(actualTime) && clientsMap.containsKey(msg.getClientId())) {
                            MessengerServer messengerServer  = clientsMap.get(msg.getClientId());
                            if (!Objects.isNull(messengerServer)){
                                messengerServer.sendMessage(msg);
                            }
                        }
                    }
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        checkingMessage.start();

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

    public void run() {
        try {
            System.out.println("Waiting for incoming Message...");
            String id = incomingMessage.readLine();
            System.out.println("Client connected with ID: " + id);
            if (id == null) {
                System.out.println("Connection is closed!");
            } else {
                clientId = id;
                if (clientsMap.containsKey(id)){
                    MessengerServer oldClient = clientsMap.get(id);
                    if(!Objects.isNull(oldClient)){
                        oldClient.closeSocket();
                    }
                }
                clientsMap.put(id, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeSocket(){
        try {
            socket.close();
            outgoingMessage.close();
            incomingMessage.close();
        } catch (IOException e) {
            System.err.println("Socket isn`t closed!");
        }

    }

    synchronized public void sendMessage(Message message) {
        if (socket.isConnected()) {
            System.out.println("Send message: " + message.getMsgText() + "to client id: " + clientId);
            outgoingMessage.write(message.getMsgText());
            outgoingMessage.write("\n");
            outgoingMessage.flush();
            if(message.isLast()){
                closeSocket();
                if (clientsMap.containsKey(clientId))
                    clientsMap.remove(clientId);
            }
        } else {
            if (clientsMap.containsKey(clientId))
                clientsMap.remove(clientId);
        }
    }
}

