package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class Controller {

    @FXML
    Label label;

    @FXML
    ComboBox<String> valuteFrom;

    @FXML
    ComboBox<String> valuteTo;

    HashMap<String,String> codeName = new HashMap<>();
    HashMap<String,Float> codeValue = new HashMap<>();

    public void initialize() throws IOException, ParseException {

        String sUrl = "https://www.cbr-xml-daily.ru/daily_json.js";
        URL url = new URL(sUrl);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();
        JSONParser parser = new JSONParser();
        Object object = parser.parse((Reader) request.getContent());
        JSONObject js = (JSONObject) object;
        JSONArray items = (JSONArray) js.get("Valute");
        for(Object i : items){
            codeName.put(((JSONObject) i).get("ID").toString(),((JSONObject) i).get("Name").toString());
            codeValue.put(((JSONObject) i).get("ID").toString(),Float.parseFloat((String) ((JSONObject) i).get("Value")));
        }
        ObservableList <String> observableList = FXCollections.observableArrayList();
        observableList.addAll(codeName.keySet());
        valuteFrom.setItems(observableList);
    }

    public void exchange(){
        String from = valuteFrom.getValue();
        String to = valuteTo.getValue();
        float value = (codeValue.get(from) * codeValue.get(to));
        label.setText(String.valueOf(value));
    }
}
