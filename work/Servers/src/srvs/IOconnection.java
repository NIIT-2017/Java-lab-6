package srvs;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class IOconnection {
    BufferedReader in;
    PrintWriter out;
    TextArea taLog;

    public IOconnection(TextArea taLog_par){
        taLog = taLog_par;
    }

    public void doLog(String log) {
        Platform.runLater(() -> taLog.appendText(log + "\n"));
    }
    public void serveClient(Socket client) throws IOException{
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            requestProcessing();
        } catch (IOException ex) {
            doLog("Input / Output opening failed");
        } finally {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        }
    }

    abstract void requestProcessing();
}
