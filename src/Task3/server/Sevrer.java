package Task3.server;

import Task3.conection.MyConnection;
import Task3.conection.MyConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Sevrer implements MyConnectionListener {
    //fields
    private final ArrayList<MyConnection> connections = new ArrayList<>();
    private static int LAST_ID;
    public static void main(String[] args) {
        new Sevrer();
    }

    //methods
    private Sevrer() {
        //task for sending to clients messages every 5 seconds
        //if server have got active connections
        Timer sendTimer = new Timer(true);
        sendTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(connections.size()>0) {
                    for (MyConnection next : connections) {
                        next.sendString("You are "+next+", message every 5 second");
                        //for log
                        System.out.println("sent message");
                    }
                }
            }
        },1000,5000);

        System.out.println("Server is running");
        try (ServerSocket serverSocket = new ServerSocket(2021)) {
            while (true) {
                try {
                    new MyConnection(serverSocket.accept(), this);
                } catch (IOException e) {
                    System.out.println("can't connect client");
                }
            }
        } catch (IOException e) {
            System.out.println("can't running server, port is unavailable");
        }
    }

    //events
    @Override
    public synchronized void onConnect(MyConnection connection) {
        connections.add(connection);
        connection.setID(++LAST_ID);
        System.out.println("client is connect");
    }
    //echo
    @Override
    public synchronized void onReceive(MyConnection connection, String message) {
        System.out.println(message);
        connection.sendString("from server: "+message);
    }

    @Override
    public synchronized void onDisconnect(MyConnection connection) {
        connections.remove(connection);
        System.out.println("client "+connection.getID()+" was disconnect");
    }

    @Override
    public synchronized void onException(MyConnection connection, Exception e) {
        connections.remove(connection);
        System.out.println("connection exception: " +e);
    }
}
