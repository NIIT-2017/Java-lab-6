package file;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.time.LocalTime;
import java.util.*;

public class JsonParser {
    private static String time;
    private static int hour;
    private static int min;
    private static int eventNumber;

    public String getEvent(String id) throws IOException, ParseException {
        String event = "";
        LocalTime lt = LocalTime.now();
        hour = lt.getHour();
        min = lt.getMinute();
        time = appendNull(hour)+":"+appendNull(min);
        JSONObject events = getEvents(id);
        if (events.containsKey(time))
            event = time+" - вам нужно "+events.get(time);
        else if (!hasNextEvent(events))
            event = "Событий нет";
        else
            event = "Событий нет. Следующее событие:\n"+nextEvent(events);
        return event;
    }

    private JSONObject getEvents(String id) throws IOException, ParseException {
        InputStream inputStream = getClass().getResourceAsStream("/events.json");
        JSONParser parser = new JSONParser();
        JSONObject jo = (JSONObject)parser.parse(new InputStreamReader(inputStream,"UTF-8"));
        JSONObject weekEvents = (JSONObject) jo.get(id);
        JSONObject events = (JSONObject) weekEvents.get(getDateOfWeek());
        return events;
    }

    private static String nextEvent(JSONObject events){
        String tm = "";
        Set<Map.Entry<String,String>> entryEvents = events.entrySet();
        for (Map.Entry entry : entryEvents) {
            tm = (String) entry.getKey();
            if (countMin(tm) > countMin(time))
                break;
            eventNumber++;
        }
        String nextEvent = (String) events.values().toArray()[eventNumber];
        eventNumber = 0;
        return tm+" - "+nextEvent;
    }

    private static boolean hasNextEvent(JSONObject events){
        int lastTime = 0;
        boolean flag = true;
        Set<String> times = events.keySet();
        for (String t: times){
            if(countMin(t)>lastTime)
                lastTime = countMin(t);
        }
        if (lastTime<countMin(time))
            flag = false;
        return flag;
    }

    private static int countMin(String time){
        int h = Integer.parseInt(time.split(":")[0]);
        int m = Integer.parseInt(time.split(":")[1]);
        int minutes = h*60+m;
        return minutes;
    }

    private static String appendNull(int i)
    {
        String str=Integer.toString(i);
        if(Integer.toString(i).length()==1)
            str = "0"+i;
        return str;
    }

    static private String getDateOfWeek(){
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String[] days = {"", "Воскресенье", "Понедельник","Вторник","Среда","Четверг","Пятница","Суббота"};
        String dateOfWeek = days[dayOfWeek];
        return dateOfWeek;
    }
}

