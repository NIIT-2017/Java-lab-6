package task3.server;

import java.io.*;
import java.net.Socket;

public class ClientHolder {
    private final ObjectOutputStream out;
    private final int id;
    private final Socket socket;

    public ClientHolder(Socket socket, int id) throws IOException {
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        this.id = id;
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public int getId() {
        return id;
    }
}
