import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ServerOne extends Thread{
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private List<Message> messages = new ArrayList<>();

    public ServerOne(Socket s) throws IOException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    public void run(){
        try {
            long id = Long.parseLong(in.readLine());
            System.out.println("id: " + id);
            int count = 0;
            makeMessages();
            while (true) {
                for (Message message : messages) {
                    if (id == message.getId()) {
                        count++;
                        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                        Date now = new Date();
                        String dateNow = dateFormat.format(now);
                        if (message.getTime().equals(dateNow)) {
                            out.println(message.getMessage());
                        }
                    }
                }
                if(count==0) {
                    out.println("Нет запланированных задач");
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeMessages(){
        File file = getResourceAsFile("messages.json");
        long id;
        Date date;
        String msg;
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(file));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray JSONmessages = (JSONArray) jsonObject.get("messages");
            for(int i = 0 ; i< JSONmessages.size() ;i++){
                JSONObject object = (JSONObject) JSONmessages.get(i);
                id = (long) object.get("id");
                String time = (String) object.get("time");
                msg = (String) object.get("message");
                Message message = new Message(id, time, msg);
                messages.add(message);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     public File getResourceAsFile(String name) {
        try {
            InputStream inStream = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream(name);
            if (inStream == null) {
                return null;
            }
            File tempFile = File.createTempFile(String.valueOf(inStream.hashCode()), ".tmp");
            tempFile.deleteOnExit();

            try (FileOutputStream outStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
            }
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
