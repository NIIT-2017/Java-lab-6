package Clients;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NoticeModule extends ClientModule {
    private static int numOfClients = 0;
    private int index;

    public NoticeModule(TextArea ta_Resp, TextArea ta_Logs) {
        super(ta_Resp, ta_Logs);
        numOfClients++;
        index = numOfClients;
    }

    public NoticeModule(TextArea ta_Resp, TextArea ta_Logs, TextField tf_request) {
        super(ta_Resp, ta_Logs, tf_request);
        numOfClients++;
        index = numOfClients;
    }

    @Override
    public void printResp(String response) {
        dialog.append(response).append("\n");
        if (selected)
            taResp.appendText(response + "\n");
    }

    @Override
    public String toString() {
        return "Listener#" + index;
    }
}
