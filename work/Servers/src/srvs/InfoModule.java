package srvs;

import javafx.scene.control.TextArea;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;

public class InfoModule extends IOconnection {
    private String url = "https://www.cbr-xml-daily.ru/daily_json.js";
    private float usdRate = 0f;
    private float eurRate = 0f;

    public InfoModule(TextArea taLog) {
        super(taLog);
    }

    @Override
    void requestProcessing() {
        String input;
        NumberFormat formatter = new DecimalFormat("0.00");
        String result;
        float value = 0f;
        try {
            while ((input = in.readLine()) != null) {
                if (input.equalsIgnoreCase("exit")){
                    doLog("[Client] : " + input);
                    out.println(input);
                    break;
                }
                try {
                    value = Float.parseFloat(input);
                    rateQuery();
                } catch (NumberFormatException ex) {
                    doLog("[Server] : Wrong format, only numbers  and '.' are valid");
                    out.println("Wrong format, only numbers and '.' are valid");
                }
                result = formatter.format(value / usdRate) + " USD|" + formatter.format(value / eurRate) + " EUR";
                out.println(result);
                doLog("[Client] : " + input);
                doLog("[Server] : " + result);
            }
        } catch (IOException ex) {
            doLog("[Server] : Input / Output failed");
        }
    }
    //get rate for USD and EUR
    private void rateQuery() {
        try {
            StringBuilder sb = new StringBuilder();
            Scanner scanner = new Scanner(new URL(url).openStream());
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
            JSONObject obj = (JSONObject) new JSONParser().parse(sb.toString());
            obj = (JSONObject) obj.get("Valute");
            JSONObject usd = (JSONObject) obj.get("USD");
            JSONObject eur = (JSONObject) obj.get("EUR");
            usdRate = Float.parseFloat(usd.get("Value").toString());
            eurRate = Float.parseFloat(eur.get("Value").toString());
        } catch (IOException | ParseException ex) {
            doLog("[Server] : reading data from " + url + "failed");
            ex.printStackTrace();
        }
    }
}
