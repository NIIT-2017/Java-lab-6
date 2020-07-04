package server;

import javafx.concurrent.Task;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class ServerProcess extends Task<Void> {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ArrayList<Aphorism> aphorisms;

    public ServerProcess(ServerSocket serverSocket, Socket clientSocket) {
        this.serverSocket = serverSocket;
        this.clientSocket = clientSocket;
        this.aphorisms = new ArrayList<>();
    }

    @Override
    public Void call(){
        giveAphorism();
        return null;
    }

    public File getResourceAsFile() {
        try {
            InputStream inStream = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream("server/aphorisms.json");
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

    private void getAphorismFromJSON() {
        File jsfile = this.getResourceAsFile();
        try {
            String content = FileUtils.readFileToString(jsfile, "utf-8");
            JSONObject jsList = new JSONObject(content);
            JSONArray jsAphorisms = (JSONArray) jsList.get("aphorisms");
            for (Object obj : jsAphorisms) {
                String strAphorism = ((JSONObject) obj).get("aphorism").toString();
                String strAuthor = ((JSONObject) obj).get("author").toString();
                Aphorism aphorism = new Aphorism(strAphorism, strAuthor);
                this.aphorisms.add(aphorism);
            }
        }
        catch (IOException e) {}
    }

    public void giveAphorism() {
        BufferedReader brIn = null;
        PrintWriter pwOut = null;
        String sInput;
        updateMessage("Waiting connection");
        try {
            clientSocket = serverSocket.accept();
            updateMessage("Client has connected");
            brIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            pwOut = new PrintWriter(clientSocket.getOutputStream(), true);
            while ((sInput = brIn.readLine()) != null) {
                if (sInput.equals("exit")) {
                    updateMessage("Client disconnected. Server stopped");
                    brIn.close();
                    pwOut.close();
                    serverSocket.close();
                    clientSocket.close();
                    break;
                } else if (sInput.equals("aphorism")) {
                    updateMessage("Client connected");
                    this.getAphorismFromJSON();
                    Random r = new Random();
                    int i = r.nextInt(aphorisms.size());
                    String line = aphorisms.get(i).getAphorism().concat(" ")
                            .concat(aphorisms.get(i).getAuthor());
                    System.out.println(line);
                    pwOut.println(line);
                }
            }
        }
        catch (IOException e){
            updateMessage("Can't connect");
        }
    }
}
