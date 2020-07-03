package srvs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server extends Thread {
    ServerSocket server = null;
    Socket client = null;
    public IOconnection module = null;
    public boolean terminateFlag = false;
    private boolean isMultiThread = false;

    //open ServerSocket into constructor
    private Server(IOconnection module, int port){
        setDaemon(true);
        this.module = module;
        this.module.doLog("Server started");
        try{
            server = new ServerSocket(port);
        } catch(IOException ex){
            this.module.doLog("connecting failed");
        }
    }

    public Server multiThread(boolean flag) {
        this.isMultiThread = flag;
        return this;
    }
    public static Server getInstance(IOconnection module, int port) {
        return new Server(module, port);
    }

    //close ServerSocket, Socket and it's input, output
    public void close() {
        try {
            if (server != null)
                server.close();
            if (client != null)
                client.close();
            module.doLog("Server stopped successfully");
        } catch (IOException ex) {
            module.doLog("terminating failed");
        }
    }

    @Override
    public void run() {
        //get Socket
        do {
            try {
                module.doLog("Waiting for a client");
                client = server.accept();
                module.doLog("A client has connected");
            } catch (SocketException ex) {
                if (terminateFlag) {
                    return;
                }
                module.doLog("Can not establish connection");
                continue;
            } catch (IOException ex) {
                module.doLog("Can not establish connection");
                continue;
            }
            //module starts to handle client requests
            module.doLog("Waiting for a client's message");
            try {
                module.serveClient(client);
            } catch (IOException ex) {
                module.doLog("Input / Output closing failed");
            }
        //Server continue to establish connections with another clients if isMultiThread=true
        } while(isMultiThread);

        close();
    }
}
