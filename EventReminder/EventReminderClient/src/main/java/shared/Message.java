package shared;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    private String login;
    private String message;
    private Date time;


    public Message(String login, String message){
        this.login = login;
        this.message = message;
        this.time = java.util.Calendar.getInstance().getTime();
    }

    public String getLogin() {
        return login;
    }

    public String getMessage() {
        return message;
    }

}
