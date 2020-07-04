package sample;

public class Reminders {
    private String time;
    private String reminder;

    public Reminders(String time, String message) {
        this.time = time;
        this.reminder = message;
    }

    public String getTime() {
        return time;
    }

    public String getReminder() {
        return reminder;
    }

}