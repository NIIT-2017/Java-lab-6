package sample;

import com.sun.jdi.event.ExceptionEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.SerializationUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

public class Controller implements Initializable {
    @FXML
    Pane pane;
    @FXML
    Button buttonLoad;
    @FXML
    Label labelInfo;
    @FXML
    TextField textFieldServerIp;
    @FXML
    ComboBox comboBoxMode;
    String[] modes;
    //Label labelClientId;
    //TextField textFieldClientId;
    HBox hBoxBottom;
    JSONObject jsonObjectRates;
    public void getDateFromServer() {
        try {
            Socket socket = new Socket(getServerIpString(), 5000);
            Calendar calendar = ((Calendar) getObjectFromServer(socket));
            labelInfo.setText(" Seconds : " + calendar.get(Calendar.SECOND) + " Minutes : "
                    + calendar.get(Calendar.MINUTE) + " Hours : " + calendar.get(Calendar.HOUR)
                    + " Day : " + calendar.get(Calendar.DAY_OF_MONTH) + " Month : " + calendar.get(Calendar.MONTH)
                    + " Year : " + calendar.get(Calendar.YEAR));
            socket.close();
        } catch (Exception ex) {
            labelInfo.setText("Error. Couldn't load data from server." + ex.getMessage());
        }
    }

    public void getAphorismFromServer() {
        try {
            Socket socket = new Socket(getServerIpString(), 6000);
            String aphorism = (String) getObjectFromServer(socket);
            labelInfo.setText(aphorism);
            socket.close();
        } catch (Exception ex) {
            labelInfo.setText("Error. Couldn't load data from server." + ex.getMessage());
        }
    }

    private Object getObjectFromServer(Socket socket) throws Exception {
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        int packetSize = dataInputStream.readInt();
        byte[] bytes = new byte[packetSize];
        dataInputStream.read(bytes);
        return SerializationUtils.deserialize(bytes);
    }

    private String getServerIpString() {
        String serverIp;
        if (textFieldServerIp.getText() == "")
            serverIp = "localhost";
        else
            serverIp = textFieldServerIp.getText();
        return serverIp;
    }

    private void listenToServerEvents() {

        try {
            labelInfo.setText("");
            Socket socket = new Socket(getServerIpString(), 7000);
            Thread readingThread = new Thread() {
                public void run() {
                    try {
                        while (true) {
                            TextField textFieldId = (TextField) hBoxBottom.getChildren().get(1);
                            sendData(socket, Integer.parseInt(textFieldId.getText()));
                            String serverMessage = (String) getObjectFromServer(socket);
                            if (!serverMessage.equals(""))
                                Platform.runLater(() -> labelInfo.setText(labelInfo.getText() + "\n" + serverMessage));
                        }
                    } catch (Exception ex) {

                    }
                }
            };
            readingThread.start();
        } catch (Exception ex) {
            labelInfo.setText(labelInfo.getText() + "\n" + ex.getMessage());
        }
    }

    private void sendData(Socket socket, Object object) throws Exception {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        byte[] bytes = SerializationUtils.serialize((Serializable) object);
        dataOutputStream.writeInt(bytes.length);
        dataOutputStream.write(bytes);
        dataOutputStream.flush();
    }

    public void setButtonEvent() {

        switch ((String) comboBoxMode.getSelectionModel().getSelectedItem()) {
            case "Date":
                deleteBottomLine();
                buttonLoad.setOnAction((e) -> getDateFromServer());
                break;
            case "Aphorism":
                deleteBottomLine();
                buttonLoad.setOnAction((e) -> getAphorismFromServer());
                break;
            case "Events log": {
                deleteBottomLine();
                createClientIdLine();
                buttonLoad.setOnAction((e) -> listenToServerEvents());
                break;
            }
            case "Currency": {
                deleteBottomLine();
                createCurrencyLine();
                buttonLoad.setOnAction((e) -> showCurrencyRelation());
                break;
            }
        }
    }
    private void showCurrencyRelation()
    {
        try {
            double leftPrice = 1.0 / getCurrencyValue(getCurrencyCombobox(0).getSelectionModel().getSelectedItem());
            double rightPrice = 1.0 / getCurrencyValue(getCurrencyCombobox(1).getSelectionModel().getSelectedItem());
            labelInfo.setText("1 = " + rightPrice / leftPrice);
        }
        catch (Exception ex)
        {
            labelInfo.setText("Please set values properly. "+ex.getMessage());
        }
    }
    private void createClientIdLine() {
        if (hBoxBottom == null) {
            hBoxBottom = new HBox();
            Label labelClientId = new Label();
            labelClientId.setText("Client id");
            TextField textFieldClientId = new TextField();
            hBoxBottom.getChildren().addAll(labelClientId, textFieldClientId);
            VBox vBox = (VBox) pane.getChildren().get(0);
            vBox.getChildren().add(hBoxBottom);
        }
    }
    private  double getCurrencyValue(String currency)
    {
        return jsonObjectRates.getDouble(currency);
    }
    private void createCurrencyLine() {
        if (hBoxBottom == null) {
            hBoxBottom = new HBox();
            ComboBox comboBoxLeftValue = new ComboBox();
            comboBoxLeftValue.setMinSize(pane.getWidth() / 2, comboBoxLeftValue.getHeight());
            ComboBox comboBoxRightValue = new ComboBox();
            comboBoxRightValue.setMinSize(pane.getWidth() / 2, comboBoxLeftValue.getHeight());
            hBoxBottom.getChildren().addAll(comboBoxLeftValue, comboBoxRightValue);
            VBox vBox = (VBox) pane.getChildren().get(0);
            vBox.getChildren().add(hBoxBottom);
            ArrayList<String> values=getCurrecniesListFromSite();
            ComboBox<String> comboBoxLeft=getCurrencyCombobox(0);
            ComboBox<String> comboBoxRight=getCurrencyCombobox(1);
            comboBoxLeft.getItems().addAll(values);
            comboBoxRight.getItems().addAll(values);
        }
    }

    private void deleteBottomLine() {
        if (hBoxBottom != null) {
            VBox vBox = (VBox) pane.getChildren().get(0);
            vBox.getChildren().remove(hBoxBottom);
            hBoxBottom = null;
        }
    }
    private ComboBox<String> getCurrencyCombobox(int index)
    {
        return (ComboBox<String>) hBoxBottom.getChildren().get(index);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelInfo.setWrapText(true);
        modes = new String[]{"Date", "Aphorism", "Events log", "Currency"};
        comboBoxMode.getItems().addAll(modes);
    }

    private ArrayList<String> getCurrecniesListFromSite() {
        try {
            jsonObjectRates = getJsonObjectRates();
            Iterator iterator = jsonObjectRates.keys();
            ArrayList<String> names = new ArrayList<String>();
            while (iterator.hasNext()) {
                names.add( (String) iterator.next());
            }
            return names;
        } catch (Exception ex) {
        }
        return null;
    }
    private  JSONObject getJsonObjectRates() throws Exception
    {
        JSONObject jsonObject = readJsonFromUrl("https://openexchangerates.org/api/latest.json?app_id=5a11aa4d56564bbdb101289a6fa338d7");
        JSONObject jsonRates = jsonObject.getJSONObject("rates");
        return jsonRates;
    }
    //Copied code
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
}
