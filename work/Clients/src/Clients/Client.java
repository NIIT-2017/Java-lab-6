package Clients;

import java.io.IOException;
import java.net.Socket;

public class Client extends Thread {

    private Socket server;
    private ClientModule module;
    private boolean connected = false;

    private Client(String host, int port, ClientModule module) {
        setDaemon(true);
        this.module = module;
        try {
            server = new Socket(host, port);
            connected = true;
            module.printLog("Connection established");
        } catch (IOException ex) {
            module.printLog("Connecting failed");
            connected = false;
        }
    }

    public static Client getInstance(String host, int port, ClientModule module) {
        return new Client(host, port, module);
    }
    public String getDialog() {
        return module.getDialog();
    }
    public void setSelected(boolean state) {
        module.setSelected(state);
    }
    public boolean isConnected() {
        return connected;
    }

    @Override
    public String toString() {
        return module.toString();
    }

    public void close() {
        try {
            if (server != null) {
                server.close();
                module.printLog("Client disconnected successfully");
                connected = false;
            }
        } catch (IOException ex) {
            module.printLog("Client terminating failed");
            connected = false;
        }
    }

    @Override
    public void run() {
        try{
            module.workWith(server);
        } catch (IOException ex) {
            module.printLog("Input / Output closing failed");
        } catch (NullPointerException ex) {
            module.printLog("");
        } finally {
            close();
        }
    }

    public void sendRequest() {
        module.printLog("send request to server");
        module.write();
    }
    public void disconnect() {
        module.printLog("send disconnecting command");
        module.write(module.getDisconnectCmd());
        module.go = false;
    }
}
