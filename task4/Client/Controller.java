package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Controller {
    HashMap<String, Double> currency = new HashMap<>();
    HashMap<String, String> nameOfCurrency = new HashMap<>();

    @FXML
    TextArea curs;

    @FXML
    ComboBox<String> currencyFrom;

    @FXML
    ComboBox<String> currencyTo;

    @FXML
    public void count() {
        String from = currencyFrom.getValue();
        String to = currencyTo.getValue();
        if (to == null || from == null)
            return;
        double valueFrom = currency.get(from);
        double valueTo = currency.get(to);
        double answer = valueFrom / valueTo;
        String ansStr = String.format("%.2f", answer);
        curs.setText(nameOfCurrency.get(from) + " = " + ansStr + " " + nameOfCurrency.get(to));
    }

    @FXML
    public void initialize() throws IOException, ParseException {
        URL url = new URL("https://www.cbr-xml-daily.ru/daily_json.js");
        InputStream is = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(br);
        JSONObject valute = (JSONObject) object.get("Valute");

        Set<String> keys = valute.keySet();

        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            String cur = (String) iterator.next();
            JSONObject obj1 = (JSONObject) valute.get(cur);
            Double value = (Double) obj1.get("Value");
            String name = (String) obj1.get("Name");
            currency.put(cur, value);
            nameOfCurrency.put(cur, name);
        }

        currency.put("RUB", 1.0);
        nameOfCurrency.put("RUB", "Российский рубль");
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(currency.keySet());
        currencyFrom.setItems(list);
        currencyTo.setItems(list);
    }
}
