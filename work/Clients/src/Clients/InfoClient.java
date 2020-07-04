package Clients;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class InfoClient extends ClientModule {

    public InfoClient(TextArea ta_Resp, TextArea ta_Logs) {
        super(ta_Resp, ta_Logs);
    }

    public InfoClient(TextArea ta_Resp, TextArea ta_Logs, TextField tf_request) {
        super(ta_Resp, ta_Logs, tf_request);
    }

    @Override
    public void printResp(String response) {
        taResp.clear();
        for (String line : response.split("\\|"))
            taResp.appendText(line + "\n");
    }
}
