package Task3.conection;

import java.io.*;
import java.net.Socket;

//common class for connection
public class MyConnection {
    //fields
    private final Thread rxThread;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final Socket socket;

    private final MyConnectionListener listener;

    private int ID;
    //word for disconnect

    public static final String dis = "disconnect";
    //methods

    public MyConnection(String host,int port,MyConnectionListener listener) throws IOException {
        this(new Socket(host, port), listener);
    }
    public MyConnection(Socket socket, MyConnectionListener listener) throws IOException{
        ID = 0;
        this.socket = socket;
        this.listener = listener;

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //say listener connection is up
                    listener.onConnect(MyConnection.this);
                    while (!rxThread.isInterrupted()) {
                        String msg = in.readLine();
                        // say listener message
                        if (!msg.equals(dis)) listener.onReceive(MyConnection.this,msg);
                        else {
                            //if neighbor want to break connection
                            sendString(dis);
                            disconnect();
                        }
                    }
                } catch (IOException e) {
                    listener.onException(MyConnection.this, e);
                }finally {
                    listener.onDisconnect(MyConnection.this);
                }
            }
        });
        rxThread.start();
    }
    public synchronized void disconnect() {
        rxThread.interrupt();
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        } catch (IOException e) {
            listener.onException(MyConnection.this,e);
        }
    }

    public synchronized void sendString(String str) {
        try {
            out.write(str+"\r\n");
            out.flush();
        } catch (IOException e) {
            listener.onException(MyConnection.this,e);
            disconnect();
        }
    }

    //for log
    @Override
    public String toString() {
        return "address: "+socket.getInetAddress().toString()+" port: "+socket.getPort()+" ID: "+ID;
    }

    //set ID for input  server connections
    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
