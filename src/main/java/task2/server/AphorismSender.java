package task2.server;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class AphorismSender extends Thread {

    private List<ClientHolder> clients = Collections.synchronizedList(new LinkedList<>());
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<String> aphorisms = new ArrayList<>();

    public AphorismSender() {
        readAphorisms();
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            String randomAphorism = getRandomAphorism();
            for (int i = 0; i < clients.size(); i++) {
                ClientHolder client = clients.get(i);
                client.getOut().println(randomAphorism);
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
        this.clients.add(client);
    }

    private void readAphorisms() {
        InputStream file = this.getClass().getClassLoader()
                .getResourceAsStream("task2/aphorisms.json");
        try {
            aphorisms = objectMapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRandomAphorism() {
        Random random = new Random();
        return aphorisms.get(random.nextInt(aphorisms.size()));
    }

}
