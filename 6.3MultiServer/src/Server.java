import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

public class Server {
    static final int PORT=1234;
    static private BufferedReader in;
    static Map<String,InetAddress> mapMyClients= new HashMap<String,InetAddress>();
    static Map<String,String> mapTimeMesToClient=new HashMap<String, String>();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Мультипоточный сервер стартовал!");

        try{
            while (true){

                Socket client=server.accept();
                in=new BufferedReader(new InputStreamReader(client.getInputStream())); //id of client
                mapTimeMesToClient.clear();

                try{
                    System.out.println("Новое соединение установлено");

                    InetAddress clientAddress=client.getInetAddress();
                    System.out.println("Данные клиента "+clientAddress);

                    String clientID =in.readLine();
                    System.out.println("Получено: id = "+clientID);

                    for(String id : mapMyClients.keySet())
                        if(id.equals(null))
                            mapMyClients.put(clientID,clientAddress);

                    JSONParser parser=new JSONParser();  //read file for this client has this ID
                    try{
                       // URL resource=Server.class.getResource("clients.json");
                       // File file= Paths.get(resource.toURI()).toFile();
                       // FileReader fr= new FileReader(file);
                        //FOR JAR
                        InputStream inputStream = Server.class.getResourceAsStream("clients.json");
                        String fr = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

                        JSONArray allID=(JSONArray)parser.parse(fr);

                        for(Object oneClient : allID) {
                            JSONObject thisClient = (JSONObject) oneClient;
                            String fileID = (String) thisClient.get("id");

                            if(fileID.equals(clientID)){
                                JSONArray forMapTimeMes = (JSONArray) thisClient.get("mapTimeMesToClient");
                                for (Object oneMessage : forMapTimeMes) {
                                    JSONObject thisMessage = (JSONObject) oneMessage;
                                    String timeMessage = (String) thisMessage.get("time");
                                    String theMessage = (String) thisMessage.get("message");
                                    mapTimeMesToClient.put(timeMessage, theMessage); //for this client
                                }
                                break;
                            }
                            else
                                client.close(); //I don't have messages for this client (ID)
                        }

                    }  catch (ParseException e) {
                        e.printStackTrace();
                    }
                    new ServerOne(client, mapTimeMesToClient);
                }catch (IOException e){
                    client.close();
                }
            }
        }
        finally {
            server.close();
        }
    }
}

class ServerOne extends Thread{
    private Socket client;
    private PrintWriter out;
    private Map<String,String> mapTimeMesToClient;

    public ServerOne(Socket s, Map<String,String> mapTimeMesToClient) throws IOException{
        client=s;
        this.mapTimeMesToClient=mapTimeMesToClient;
        out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
        start();
    }

    public void run() {
        System.out.println("ServerOne");
        while (true) {
            String time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
            for (String timeId : mapTimeMesToClient.keySet()) {
                if (time.equals(timeId)) {
                        String line = mapTimeMesToClient.get(timeId);
                        out.println(line);
                        System.out.println("For client message: " + line);
                }
            }
            try {
                Thread.sleep(60000); //1 min
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
