package server;

import file.JsonParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import shared.Message;

//import static file.JsonParser.getEvent;

public class ThreadedHandler implements Runnable
{
    private Socket incoming;
    private Timer pingTimer;
    private Timer eventTimer;
    private TimerTask pingTask;
    private TimerTask eventTask;
    private long outPing;
    private long inPing;
    private String login;
    private Message msg;

    public ThreadedHandler(Socket incoming)
    {
        this.incoming = incoming;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(incoming.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(incoming.getOutputStream());
            msg = (Message) in.readObject();
            login = msg.getLogin();
            ping(out);
            sendEvent(out);

            while (true){
                if (msg.getMessage().equals("ping")){
                    inPing++;
                }
                if (!(outPing==inPing)){
                    System.out.println("User "+msg.getLogin()+" is disconnected");
                    out.close();
                    in.close();
                    incoming.close();
                    break;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void ping(ObjectOutputStream out){
        pingTimer = new Timer();
        pingTask = new TimerTask() {
            @Override
            public void run() {

                try {
                    if (outPing == inPing) {
                        out.writeObject(new Message("server", "ping"));
                        out.flush();
                        outPing++;
                    } else throw new SocketException();
                }catch (SocketException se){
                    pingTimer.cancel();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        pingTimer.scheduleAtFixedRate(pingTask, 0, 25000);
    }
    public void sendEvent(ObjectOutputStream out){
        JsonParser jsonParser = new JsonParser();
        eventTimer = new Timer();
        eventTask = new TimerTask() {
            @Override
            public void run() {
                while (incoming.isConnected()){
                    try {
                        out.writeObject(new Message("server",jsonParser.getEvent(login)));
                    }catch (SocketException se){
                        System.out.println("Клиент "+msg.getLogin()+" отключился");
                        eventTimer.cancel();
                        break;
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        eventTimer.schedule(eventTask,1000,60000);
    }
}
