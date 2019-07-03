package sample;

public class ClientMessage{
    private String time;
    private String mess;

    public ClientMessage (String time, String mess){
        this.mess = mess;
        this.time = time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getMess() {
        return mess;
    }

    public String getTime() {
        return time;
    }
}
