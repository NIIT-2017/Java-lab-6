
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Message {
    private int id;
    private Calendar time;
    private String mess;
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM YYYY HH:mm");

    public Message(int id, String time, String mess){
        this.id = id;
        this.mess = mess;
        //SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        String [] strHourMinArr = time.split(":");
        int hour = Integer.parseInt(strHourMinArr[0]);
        int min = Integer.parseInt(strHourMinArr[1]);
        //for this learning task I always set today date although in real app date should be got from database with messages
        this.time = new GregorianCalendar();
        this.time.set(Calendar.HOUR_OF_DAY, hour);
        this.time.set(Calendar.MINUTE, min);
        sdf.setTimeZone(this.time.getTimeZone());
    }

    public Calendar getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public String getMess() {
        return mess;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    @Override
    public String toString() {
        return (sdf.format(time.getTime()) + "/" + mess);
    }
    //for testing
    public static void main(String[] args) {
        Message n = new Message(101, "08:19", "бла-бла-бла");
        System.out.println(n.toString());
    }

}
