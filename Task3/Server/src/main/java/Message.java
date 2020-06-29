import java.util.Date;

public class Message {
    private long id;
    private String time;
    private String message;
    public Message(long id, String time, String message) {
        this.id = id;
        this.time = time;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
