import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private static int threadNumberDate = 0;
    private static ArrayList<String> aphorisms;
    private static ArrayList<Socket> clients;

    private static void loadAphorismsFromFile() throws Exception {
        aphorisms = new ArrayList<>();
        File jarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        File file = new File(jarFile.getParentFile(), "Aphorisms.txt");
        BufferedReader br =new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            aphorisms.add(line);
        }
    }
    

    public static void main(String[] args) throws Exception {
        clients = new ArrayList<>();
        loadAphorismsFromFile();
        startDateThread();
        startAphorismThread();
        startEventsThread();
    }

    private static void startEventsThread() throws Exception {
        ServerSocket serverSocket = new ServerSocket(7000);
        String threadName = "Events thread ";
        Scanner scanner = new Scanner(System.in);
        Thread threadAddingClients = new Thread(() -> {
            while (true) {
                try {
                    System.out.println(threadName + "Waiting for signal");
                    Socket socket = serverSocket.accept();
                    clients.add(socket);
                    threadNumberDate++;
                } catch (Exception ex) {
                    break;
                }
            }
        });
        threadAddingClients.start();
        Thread threadSendingToClients = new Thread() {
            public void run() {
                System.out.println(threadName + "> New thread " + threadNumberDate);
                while (true) {
                    int enteredId=scanner.nextInt();
                    String message = scanner.nextLine();
                    if (message.equals("") || message.equals("\n"))
                        break;
                    else {
                        for (int i = 0; i < clients.size(); i++) {
                            int id;
                            try {
                                id = (Integer)getObjectFromServer(clients.get(i));
                            }
                            catch (Exception ex)
                            {
                                id=-1;
                            }
                            if(id==-1 || id==enteredId||enteredId==-1)
                                sendData(clients.get(i), message);
                            else
                                sendData(clients.get(i),"");
                        }
                    }
                }
            }
        };
        threadSendingToClients.start();
    }

    private static void startDateThread() throws Exception {
        ServerSocket serverSocket = new ServerSocket(5000);
        String threadName = "Date thread ";
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    System.out.println(threadName + "Waiting for signal");
                    Socket socket = serverSocket.accept();
                    Thread threadProcessing = new Thread() {
                        public void run() {
                            System.out.println(threadName + "> New thread " + threadNumberDate);
                            Calendar calendar = Calendar.getInstance();
                            sendData(socket, calendar);
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println(threadName + " " + threadNumberDate + " > Data sent.Closing connection");
                        }
                    };
                    threadProcessing.start();
                    threadNumberDate++;

                } catch (Exception ex) {
                    break;
                }
            }
        });
        thread.start();
    }

    private static void startAphorismThread() throws Exception {
        ServerSocket serverSocket = new ServerSocket(6000);
        String threadName = "Aphorism thread ";
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    System.out.println(threadName + "Waiting for signal");
                    Socket socket = serverSocket.accept();
                    Thread threadProcessing = new Thread() {
                        public void run() {
                            System.out.println(threadName + "> New thread " + threadNumberDate);
                            String aphorism = getRandomAphorism();
                            sendData(socket, aphorism);
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println(threadName + " " + threadNumberDate + " > Data sent.Closing connection");
                        }
                    };
                    threadProcessing.start();
                    threadNumberDate++;

                } catch (Exception ex) {
                    break;
                }
            }
        });
        thread.start();
    }

    private static void sendData(Socket socket, Object object) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            dataOutputStream.writeInt(bytes.length);
            dataOutputStream.write(bytes);
            dataOutputStream.flush();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    private static Object getObjectFromServer(Socket socket) throws Exception {
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        int packetSize = dataInputStream.readInt();
        byte[] bytes = new byte[packetSize];
        dataInputStream.read(bytes);
        return SerializationUtils.deserialize(bytes);
    }

    private static String getRandomAphorism() {
        return aphorisms.get(ThreadLocalRandom.current().nextInt(0, aphorisms.size()));
    }
}
