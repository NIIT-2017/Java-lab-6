package task2.server;

import java.io.*;
import java.net.Socket;

public class ClientHolder {
    private final PrintWriter out;
    private final Socket socket;

    public ClientHolder(Socket socket) throws IOException {
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getOut() {
        return out;
    }
}
