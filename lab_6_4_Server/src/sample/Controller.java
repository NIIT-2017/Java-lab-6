package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;


public class Controller {
    HashMap<String, Double> currency = new HashMap<>();
    HashMap<String, String> currencyName = new HashMap<>();

    @FXML
    TextArea curs;

    @FXML
    ComboBox<String> currencyFrom;

    @FXML
    ComboBox<String> currencyTo;

    @FXML
    public void count(){
        String from=currencyFrom.getValue();
        String to=currencyTo.getValue();
        if (to==null||from==null){
            return;
        }
        double fromValue=currency.get(from);
        double toValue=currency.get(to);
        double ans=fromValue/toValue;
        String strDouble=String.format("%.2f",ans);
        curs.setText("1 "+currencyName.get(from)+" = " + strDouble+ " "+currencyName.get(to));
    }

    @FXML
    public void initialize() throws IOException, JSONException {
        URL url = new URL("https://www.cbr-xml-daily.ru/daily_json.js");
        JSONObject json = new JSONObject(IOUtils.toString(url, Charset.forName("UTF-8")));
        JSONObject obj = (JSONObject) json.get("Valute");
        Iterator iterator = obj.keys();
        while (iterator.hasNext()) {
            String cur = (String) iterator.next();
            JSONObject obj1 = (JSONObject) obj.get(cur);
            Double value = (Double) obj1.get("Value");
            String name = (String) obj1.get("Name");
            currency.put(cur, value);
            currencyName.put(cur,name);
        }
        currency.put("RUB",1.0);
        currencyName.put("RUB","Российский рубль");
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(currency.keySet());
        currencyFrom.setItems(list);
        currencyTo.setItems(list);

    }
}