package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Map<String, Double> exchangeRates;
    private ObservableList<String> currencies;

    @FXML
    private ComboBox<String> cbCurrency;

    @FXML
    private TextField tfLeftUp;

    @FXML
    private TextField tfLeftDown;

    @FXML
    private TextField tfRightUp;

    @FXML
    private TextField tfRightDown;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exchangeRates = new HashMap<>();
        currencies = FXCollections.observableArrayList("USD", "EUR", "GBP", "JPY", "CHF", "CNY");
        cbCurrency.setItems(currencies);
        readExchangeRates();

        tfLeftUp.setEditable(false);
        tfLeftDown.setEditable(false);
        tfRightUp.setEditable(false);
        tfRightDown.setEditable(false);

        cbCurrency.setOnAction(event -> {
            String cur = cbCurrency.getValue();
            double value = exchangeRates.get(cur);
            tfLeftUp.setText("1 " + cur);
            double roundValue = round(value);
            tfRightUp.setText(String.valueOf(roundValue) + " RUB");
            tfLeftDown.setText("1 RUB");
            double upendValue = round(1/value);
            tfRightDown.setText(String.valueOf(upendValue)+ " " + cur);
        });
    }

    private double round(double value) {
        value *= 100;
        int i = (int) Math.round(value);
        return (double)i / 100;
    }

    private void readExchangeRates() {
        try (InputStream is = new URL("https://www.cbr-xml-daily.ru/daily_json.js").openStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")))){

            JSONParser parser=new JSONParser();
            JSONObject jo=(JSONObject)parser.parse(br);
            JSONObject valuteJO = (JSONObject)jo.get("Valute");

            for (String cur : currencies) {
                JSONObject usdJO = (JSONObject)valuteJO.get(cur);
                double value = (Double) (usdJO.get("Value"));
                long nominal = (Long) (usdJO.get("Nominal"));
                double result = value / nominal;
                exchangeRates.put(cur, result);
            }
        } catch (Exception e){
            System.out.println("Получение курсов валют вызвало ошибку.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
