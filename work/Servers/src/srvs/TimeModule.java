package srvs;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeModule extends IOconnection {

    public TimeModule(TextArea taLog){
        super(taLog);
    }

    @Override
    public void requestProcessing() {
        String input;
        String time;
        try {
            while ((input = in.readLine()) != null) {
                if (input.equalsIgnoreCase("exit")){
                    doLog("[Client] : " + input);
                    out.println(input);
                    break;
                }
                if (input.equalsIgnoreCase("get")) {
                    time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).split("\\.")[0];
                    out.println(time);
                    doLog("[Client] : " + input);
                    doLog("[Server] : " + time);
                    continue;
                }
                out.println("[Server] : " + input);
                doLog("[Server] : " + input);
            }
        } catch (IOException ex) {
            doLog("[Server] : Input / Output failed");
        }
    }
}
