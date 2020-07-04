package task3;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {
    private final Date date;
    private final String message;
    private final int id;

    public Event(int id, Date date, String message) {
        this.date = date;
        this.message = message;
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Event{" +
                "date=" + date +
                ", message='" + message + '\'' +
                ", id=" + id +
                '}';
    }
}
