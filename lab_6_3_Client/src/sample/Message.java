package sample;

public class Message {
    private String timeOfMessage;
    private String message;

    public Message(String timeOfMessage, String message) {
        this.timeOfMessage = timeOfMessage;
        this.message = message;
    }

    public String getTimeOfMessage() {
        return timeOfMessage;
    }

    public void setTimeOfMessage(String timeOfMessage) {
        this.timeOfMessage = timeOfMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
