package sample;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;

public class Client {
    Socket clientSocket = null;
    BufferedReader in = null;
    PrintWriter out = null;
    String input, output;

    public void clientStart() throws IOException {
        System.out.println("client is started");
        clientSocket = new Socket("localhost", 5555);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
    }
    public JSONObject clientRequest() throws IOException {
        output = "request";
        out.println(output);
        JSONObject dictumJO = null;
        String input = in.readLine();
        JSONParser parser = new JSONParser();
        try {
            dictumJO = (JSONObject) parser.parse(input);
        }
        catch ( ParseException ex){
            System.err.println(ex.toString());
        }
        return dictumJO;
    }
    public void clientStop() throws IOException {
        output = "exit";
        out.println(output);
        System.out.println("send \"exit\" to server, client is stopped");
        in.close();
        out.close();
        clientSocket.close();
        clientSocket = null;
        in = null;
        out = null;
    }
}
