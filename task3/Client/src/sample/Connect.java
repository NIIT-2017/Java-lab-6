package sample;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connect extends Thread {
    private TextArea taInfo;
    private ComboBox<String> cbLogin;
    private Button btnConnect;

    Connect(TextArea taInfo, ComboBox<String> cbLogin, Button btnConnect) {
        this.taInfo = taInfo;
        this.cbLogin = cbLogin;
        this.btnConnect = btnConnect;
        setDaemon(true);
        start();
    }

    public void run(){
        Socket server;
        try {
            btnConnect.setDisable(true);
            cbLogin.setDisable(true);
            server = new Socket("127.0.0.1",1236);
            BufferedReader in  = new BufferedReader(new InputStreamReader(server.getInputStream()));
            PrintWriter out = new PrintWriter(server.getOutputStream(),true);
            taInfo.setText("Соединение с сервером установлено.\n");
            out.println(cbLogin.getValue());
            String message = in.readLine();
            while (!message.equals("end")){
                    taInfo.appendText("\n");
                    taInfo.appendText(message);
                    message = in.readLine();
            }
            in.close();
            server.close();
            taInfo.appendText("\nСоединение с сервером завершено.");
            btnConnect.setDisable(false);
            cbLogin.setDisable(false);
        } catch (IOException e) {
            taInfo.setText("Ошибка соединения с сервером.");
        }
    }
}
