package Clients;

import java.io.IOException;
import java.net.Socket;

public class Client extends Thread {

    private Socket server;
    private ClientModule module;

    private Client(String host, int port, ClientModule module) {
        setDaemon(true);
        this.module = module;
        try {
            server = new Socket(host, port);
            module.printLog("Connection established");
        } catch (IOException ex) {
            module.printLog("Connecting failed");
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

    @Override
    public String toString() {
        return module.toString();
    }

    public void close() {
        try {
            if (server != null)
                server.close();
            module.printLog("Client disconnected successfully");
        } catch (IOException ex) {
            module.printLog("Client terminating failed");
        }
    }

    @Override
    public void run() {
        try{
            module.workWith(server);
        } catch (IOException ex) {
            module.printLog("Input / Output closing failed");
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
