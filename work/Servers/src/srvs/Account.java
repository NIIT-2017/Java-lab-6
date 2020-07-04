package srvs;

import java.time.LocalTime;
import java.util.*;

public class Account {
    private int id;
    private HashMap<LocalTime, String> notes;

    public Account(int id) {
        this.id = id;
        notes = new HashMap<>();
    }

    public int getId() {
        return id;
    }
    public Collection<LocalTime> getMarks() {
        return notes.keySet();
    }

    public void addNote(LocalTime time, String note) {
        notes.put(time, note);
    }
    public String getMessage(LocalTime mark) {
        return notes.get(mark);
    }

    @Override
    public String toString() {
        StringBuilder data = new StringBuilder(id + ":\n");
        for (Map.Entry<LocalTime, String> note : notes.entrySet()) {
            data.append(note.getKey()).append(" => ").append(note.getValue()).append("\n");
        }
        return data.toString();
    }
}
