import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class task_6_4 {
    public static void main(String[] args) {

        if (args.length!=1) {
            System.out.println("Use with one from : USD, EUR, JPY, GBP");
            System.exit(-1);
        }

        HttpURLConnection connection =null;
        try{
            try {
                connection  = (HttpURLConnection) new URL("https://www.cbr-xml-daily.ru/daily_json.js").openConnection();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                connection.connect();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            StringBuilder data = new StringBuilder();
            BufferedReader in = null;
            try {
                if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine())!= null){
                        data.append(line);
                    }

                    JSONParser parser = new JSONParser();
                    JSONObject dataJO = (JSONObject) parser.parse(String.valueOf(data));
                    JSONObject valuteJO = (JSONObject)dataJO.get("Valute");
                    JSONObject countryValuteJO = (JSONObject)valuteJO.get(args[0]);

                    System.out.println( "курс "+"(" +"USD"+") "+countryValuteJO.get("Name")+" к рублю равен "+countryValuteJO.get("Value") );

                }else{
                    System.out.println("fail:"+connection.getResponseMessage()+connection.getResponseCode());
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }catch (Throwable e){
            e.printStackTrace();
        } finally {
             connection.disconnect();

        }
    }
}