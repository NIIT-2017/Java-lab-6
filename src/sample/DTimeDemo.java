package sample;

public class DTimeDemo
{
    public static void main(String[] args) {
        new DTimeServer().start();
        Main.main(args);
        DTimeServer.close();
    }
}
