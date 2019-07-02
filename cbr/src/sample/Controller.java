package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;


public class Controller {
    static HashMap<String, Valute> myHashMap;

    private class Valute {
        private String ID;
        private String NumCode;
        private String CharCode;
        private Integer Nominal;
        private String Name;
        private Double Value;
        private Double Previous;

        public
        Valute ( String ID, String numCode, String charCode, Integer nominal, String name, Double value, Double previous ) {
            this.ID = ID;
            NumCode = numCode;
            CharCode = charCode;
            Nominal = nominal;
            Name = name;
            Value = value;
            Previous = previous;
        }

        public
        String getID () {
            return ID;
        }

        public
        String getNumCode () {
            return NumCode;
        }

        public
        String getCharCode () {
            return CharCode;
        }

        public
        Integer getNominal () {
            return Nominal;
        }

        public
        String getName () {
            return Name;
        }

        public
        Double getValue () {
            return Value;
        }

        public
        Double getPrevious () {
            return Previous;
        }
    }

    @FXML
    ComboBox valute1;

    public
    void setValute1 ( HashMap<String, Valute> myHashMap) {
        Set<HashMap.Entry<String, Valute>> set = myHashMap.entrySet();
        List<String> list1 = new ArrayList<>();
        for (HashMap.Entry <String, Valute> item:
             set) {
            list1.add(item.getKey());
        }
        ObservableList<String> list = FXCollections.observableList(list1);
        valute1.setItems(list);
    }

    @FXML
    ComboBox valute2;

    public
    void setValute2 ( HashMap<String, Valute> myHashMap) {
        Set<HashMap.Entry<String, Valute>> set = myHashMap.entrySet();
        List<String> list1 = new ArrayList<>();
        for (HashMap.Entry <String, Valute> item:
                set) {
            list1.add(item.getKey());
        }
        ObservableList<String> list = FXCollections.observableList(list1);
        valute2.setItems(list);
    }

    @FXML
    TextField myField;

    @FXML
    TextArea result;

    @FXML
    private void initialize() throws IOException, ParseException {
        URL url = new URL("https://www.cbr-xml-daily.ru/daily_json.js");
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String inputLine = "";
            // построчно считываем результат в объект StringBuilder
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
               // System.out.println(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(stringBuilder);
        JSONObject currancy = (JSONObject) JSONValue.parseWithException(stringBuilder.toString());
        JSONObject valute = (JSONObject) currancy.get("Valute");

        Set<String> keys = valute.keySet();
        myHashMap = new HashMap<>();

        Iterator iterator = keys.iterator();
        while(iterator.hasNext()) {
            String cur = (String) iterator.next();
            JSONObject obj1 = (JSONObject) valute.get(cur);
            Valute val = new Valute(obj1.get("ID").toString(), obj1.get("NumCode").toString(),
                    obj1.get("CharCode").toString(), Integer.parseInt(obj1.get("Nominal").toString()),
                    obj1.get("Name").toString(), Double.parseDouble(obj1.get("Value").toString()),
                    Double.parseDouble(obj1.get("Previous").toString()));
            myHashMap.put(cur, val);
        }
        Valute rub = new Valute("rub", "rub001", "RUB1", 1, "Рубль",
                1.00, 1.00);
        myHashMap.put("RUB", rub);
        setValute1(myHashMap);
        setValute2(myHashMap);

    }

    public
    void convert () {
        String value = "";
        String key1 = valute1.getValue().toString();
        String key2 = valute2.getValue().toString();
        Valute val1 = myHashMap.get(key1);
        Valute val2 = myHashMap.get(key2);
        value = myField.getText() + " " + val1.getName() + " стоят "
                + "\n" + ( Double.parseDouble(myField.getText().toString()) *
                val1.getValue() / val2.getValue() ) + " " + val2.getName();
        result.setWrapText(true);
        result.setText(value);
    }
}
