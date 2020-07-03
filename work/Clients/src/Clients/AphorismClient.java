package Clients;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AphorismClient extends ClientModule {

    public AphorismClient(TextArea ta_Resp, TextArea ta_Logs) {
        super(ta_Resp, ta_Logs);
    }
    public AphorismClient(TextArea ta_Resp, TextArea ta_Logs, TextField tf_request) {
        super(ta_Resp, ta_Logs, tf_request);
    }

    @Override
    public void write() {
        out.println("get");
    }
}
