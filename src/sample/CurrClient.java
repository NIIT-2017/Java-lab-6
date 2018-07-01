package sample;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CurrClient
{
    public static void main(String[] args) {
        args = new String[]{"localhost"};
        if (args.length == 0) {
            System.out.println("Use: java Client hostname.");
            System.exit(-1);
        }
        System.out.println("Client start.");
        System.out.print("Connection with server " + args[0] + ". ");
        System.out.println("Please wait...");
        try {
            Socket server = new Socket(args[0], 1234);
            System.out.println("Connection is OK.");
            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            PrintWriter out = new PrintWriter(server.getOutputStream(), true);
            BufferedReader inu = new BufferedReader(new InputStreamReader(System.in));
            String strUser, strServer;
            while(true) {
                System.out.println("{Samples of commands: getFromUSDToRUB, getFromEurToRub, end}");
                System.out.print("Client: ");
                strUser = inu.readLine();
                if (strUser == null || checkComEnd(strUser)) {
                    out.println("END");
                    break;
                }
                out.println(strUser);
                strServer = in.readLine();
                System.out.println("Server: " + strServer);
            }
            in.close();
            out.close();
            inu.close();
            server.close();
        }
        catch (IOException ex) {
            System.out.println("Server is not open or closed. Please try again later.");
        }
        finally {
            System.out.println("Client closed.");
        }
    }
    private static boolean checkComEnd(String checkCom) {
        if (checkCom != null) {
            for(String commandEnd: commandsEnd) {
                if (checkCom.equalsIgnoreCase(commandEnd)) {
                    return true;
                }
            }
        }
        return false;
    }
    private static final ArrayList<String> commandsEnd = new ArrayList<String>
            (Arrays.asList("STOP", "CLOSE", "EXIT", "END"));
}
