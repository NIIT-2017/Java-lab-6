package task3.server;

import org.apache.commons.lang3.RandomStringUtils;
import task3.Event;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class EventsSender extends Thread {
    // Почитать про HashMap
    private Map<Integer, ClientHolder> clients = new ConcurrentHashMap<>();

    public EventsSender() {
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            if (clients.size() != 0) {
                Event event = generateRandomEvent();
                ClientHolder client = clients.get(event.getId());
                try {
                    client.getOut().writeObject(event);
                    sleep(3000);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    try {
                        client.getSocket().close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public void addClient(ClientHolder client) {
        this.clients.put(client.getId(), client);
    }

    private Event generateRandomEvent() {
        Random random = new Random();
        int id = random.nextInt(clients.size());
        System.out.println("Generated event for: " + id);
        return new Event(id, new Date(), "Event: " + RandomStringUtils.randomAlphanumeric(10));
    }
}
