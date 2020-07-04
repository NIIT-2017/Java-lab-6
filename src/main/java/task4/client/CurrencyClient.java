package task4.client;

import task4.CurrencyExchange;

import java.io.*;
import java.net.Socket;
import java.util.Currency;

public class CurrencyClient {
    public static void main(String[] args) {
        String currencyRate = getCurrencyRate("RUB", "EUR");
        System.out.println(currencyRate);
        String currencyRate2 = getCurrencyRate("RUB", "CAD");
        System.out.println(currencyRate2);
    }

    public static String getCurrencyRate(String from, String to) {
        String rate = "";
        try (Socket socket = new Socket("localhost", 8080);
             OutputStream output = socket.getOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(output);
             InputStream input = socket.getInputStream();
             ObjectInputStream reader = new ObjectInputStream(input)) {

            out.writeObject(new CurrencyExchange(Currency.getInstance(from), Currency.getInstance(to)));

            Object obj = reader.readObject();
            rate = String.valueOf(obj);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rate;
    }
}
