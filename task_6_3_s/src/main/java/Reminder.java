import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Reminder {
    int reminderTime;
    String remainderId;
    String reminderMessage;

    Reminder(JSONObject remainderJO){
    this.reminderTime=getTime(remainderJO);
    this.remainderId=(String) remainderJO.get("id");
    this.reminderMessage= (String) remainderJO.get("reminder");
    }

    //чтение массива напоминалок из json файла reminder.json
    private static JSONArray readFromFile(String fileName) {
        JSONParser parser = new JSONParser();
        JSONArray reminderJA = new JSONArray();
        try {
            FileReader reader = new FileReader(fileName);
            try {
                reminderJA = (JSONArray) parser.parse(reader);
            } catch (ParseException ex) {
                System.err.println(ex.toString());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
        return reminderJA;
    }

    public static int getTime(JSONObject reminderJO) {
        String time = (String) reminderJO.get("time");
        String[] timeArrey = time.split(":");
        int timeInt = 0;
        int i = 0;
        for (String item : timeArrey) {
            timeInt += (int) (Integer.parseInt(item) * Math.pow(60, 2 - i));
            i++;
        }
        return timeInt;
    }

    public static ArrayList<Reminder> ReminderSortedListByOneID(ArrayList<Reminder> ReminderSortedList, String id){
        ArrayList<Reminder> listByOneID= new ArrayList<Reminder>();
        for (Reminder item : ReminderSortedList){
            if (item.remainderId.equals(id)){
                listByOneID.add(item);
            }
        }
    return listByOneID;
    }

    private static void JArrayToReminderSortedList(JSONArray reminderJA, ArrayList<Reminder> reminderSortedList){
        for (Object item : reminderJA){
            Reminder temp = new Reminder((JSONObject) item);
            reminderSortedList.add(temp);
        }
        Collections.sort(reminderSortedList, new Comparator<Reminder>() {
            public int compare(Reminder o1, Reminder o2) {
                return (o1.reminderTime-o2.reminderTime);
            }
        });
    }

    public static ArrayList<Reminder> ReminderSortedList(String FILENAME) {
        JSONArray reminderJA = readFromFile(FILENAME);
        ArrayList<Reminder> reminderL = new ArrayList<Reminder>();
        JArrayToReminderSortedList(reminderJA, reminderL);
        return reminderL;
    }
}
