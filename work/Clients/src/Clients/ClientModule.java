package Clients;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class ClientModule {
    //stores dialog with server
    StringBuilder dialog = new StringBuilder();
    //indicates if the client is selected in the list of clients
    boolean selected = false;
    //terminate tool
    public volatile boolean go = true;
    public TextArea taResp;
    public TextArea taLogs;
    public TextField tfRequest;
    public BufferedReader in;
    public PrintWriter out;
    private String disconnectCmd = "exit";

    public ClientModule(TextArea ta_Resp, TextArea ta_Logs) {
        taResp = ta_Resp;
        taLogs = ta_Logs;
    }
    public ClientModule(TextArea ta_Resp, TextArea ta_Logs, TextField tf_request) {
        taResp = ta_Resp;
        taLogs = ta_Logs;
        tfRequest = tf_request;
    }

    public String getDialog() {
        return dialog.toString();
    }
    public void setSelected(boolean state) {
        selected = state;
    }

    public String getDisconnectCmd() {
        return disconnectCmd;
    }

    public void printLog(String log) {
        Platform.runLater(() -> taLogs.appendText(log + "\n"));

    }
    public void printResp(String response) {
        Platform.runLater(() -> taResp.setText(response));
    }
    public void workWith(Socket server) throws IOException {
        try {
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            out = new PrintWriter(server.getOutputStream(), true);
            printLog("in/out streams opened");
            listen();
        } catch (IOException ex) {
            printLog("Input / Output opening failed");
        } finally {
            in.close();
            out.close();
            printLog("in/out streams closed");
        }
    }

    public void write() {
        out.println(tfRequest.getText());
        tfRequest.clear();
    }
    public void write(String command) {
        out.println(command);
    }


    public void listen() {
        Thread listener = new Thread(() -> {
            String input = "";
            try {
                while (!input.equalsIgnoreCase("exit")) {
                    printResp(input);
                    try {
                        input = in.readLine();
                    } catch (IOException ex) {
                        printLog("input reading failed");
                        break;
                    }
                    printLog("Server response : " + input);
                }
            } catch (NullPointerException ex) {
                printLog("Server connection lost");
            }
        });
        listener.setDaemon(true);
        listener.start();
        while (go && listener.isAlive()) Thread.onSpinWait();
    }
}
