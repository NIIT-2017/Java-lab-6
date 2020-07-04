package sample;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

public class Controller {
    @FXML
    Pane pane;
    @FXML
    Label label11;
    @FXML
    Label label12;
    @FXML
    Label label21;
    @FXML
    Label label22;
    @FXML
    Label label31;
    @FXML
    Label label32;
    @FXML
    Label label41;
    @FXML
    Label label42;
    @FXML
    Label label51;
    @FXML
    Label label52;
    @FXML
    Label label61;
    @FXML
    Label label62;
    @FXML
    Label label71;
    @FXML
    Label label72;
    @FXML
    Label label81;
    @FXML
    Label label82;
    @FXML
    Label labelHeading;

    @FXML
    public void initialize() throws IOException, ParseException {

        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
        String ip = in.readLine();
        in.close();

        URL url = new URL("https://wttr.in/"+ip+"?format=j1");
        InputStream is = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        JSONParser parser = new JSONParser();
        JSONObject objectJSON = (JSONObject) parser.parse(br);

        JSONArray arrayArea = (JSONArray) objectJSON.get("nearest_area");
        JSONObject objectArea = (JSONObject) arrayArea.get(0);
        JSONArray arrayCountry = (JSONArray) objectArea.get("country");
        JSONArray arrayAreaName = (JSONArray) objectArea.get("areaName");
        JSONObject objectCountry = (JSONObject) arrayCountry.get(0);
        JSONObject objectAreaName = (JSONObject) arrayAreaName.get(0);
        String country = (String) objectCountry.get("value");
        String area = (String) objectAreaName.get("value");


        JSONArray arrayWeather = (JSONArray) objectJSON.get("weather");
        JSONObject objectWeather = (JSONObject) arrayWeather.get(0);
        String date = (String) objectWeather.get("date");
        String mintempC = (String) objectWeather.get("mintempC");
        String maxtempC = (String) objectWeather.get("maxtempC");
        String sunHour = (String) objectWeather.get("sunHour");
        JSONArray arrayHourly = (JSONArray) objectWeather.get("hourly");

        List<String> time = new ArrayList<>();
        List<String> tempC = new ArrayList<>();
        List<String> weatherDesc = new ArrayList<>();

        for(int i = 0; i < arrayHourly.size(); i++){
            JSONObject object = (JSONObject) arrayHourly.get(i);
            time.add((String)object.get("time"));
            tempC.add((String)object.get("tempC"));
            JSONArray arrayWeatherDesc = (JSONArray) object.get("weatherDesc");
            JSONObject objectWeatherDesc = (JSONObject) arrayWeatherDesc.get(0);
            weatherDesc.add((String)objectWeatherDesc.get("value"));
        }

        JSONArray arrayAstronomy = (JSONArray) objectWeather.get("astronomy");
        JSONObject objectAstronomy = (JSONObject) arrayAstronomy.get(0);
        String sunrise = (String) objectAstronomy.get("sunrise");
        String sunset = (String) objectAstronomy.get("sunset");

        label11.setText(" 12:00 AM\n     t: "+tempC.get(0));
        label12.setText(weatherDesc.get(0));
        label21.setText(" 03:00 AM\n   t: "+tempC.get(1));
        label22.setText(weatherDesc.get(1));
        label31.setText(" 06:00 AM\n   t: "+tempC.get(2));
        label32.setText(weatherDesc.get(2));
        label41.setText(" 09:00 AM\n     t: "+tempC.get(3));
        label42.setText(weatherDesc.get(3));
        label51.setText(" 12:00 PM\n    t: "+tempC.get(4));
        label52.setText(weatherDesc.get(4));
        label61.setText(" 03:00 PM\n   t: "+tempC.get(5));
        label62.setText(weatherDesc.get(5));
        label71.setText(" 06:00 PM\n    t: "+tempC.get(6));
        label72.setText(weatherDesc.get(6));
        label81.setText(" 09:00 PM\n   t: "+tempC.get(7));
        label82.setText(weatherDesc.get(7));
        String heading = "Locate "+country+", "+area+"\n"+date+"\n"+"sunrise: "+sunrise+", sunset: "+sunset+", sun hour: "+sunHour+"\n"+"t min: "+mintempC+", t max: "+maxtempC+"\n";

        //pane.setStyle("-fx-background-color: WHITE");
        labelHeading.setStyle("-fx-background-color: WHITE");
        //label11.setStyle("-fx-background-color: #69d4f226");
        label11.setStyle("-fx-background-color: WHITE");
        label12.setStyle("-fx-background-color: WHITE");
        label21.setStyle("-fx-background-color: WHITE");
        label22.setStyle("-fx-background-color: WHITE");
        label31.setStyle("-fx-background-color: WHITE");
        label32.setStyle("-fx-background-color: WHITE");
        label41.setStyle("-fx-background-color: WHITE");
        label42.setStyle("-fx-background-color: WHITE");
        label51.setStyle("-fx-background-color: WHITE");
        label52.setStyle("-fx-background-color: WHITE");
        label61.setStyle("-fx-background-color: WHITE");
        label62.setStyle("-fx-background-color: WHITE");
        label71.setStyle("-fx-background-color: WHITE");
        label72.setStyle("-fx-background-color: WHITE");
        label81.setStyle("-fx-background-color: WHITE");
        label82.setStyle("-fx-background-color: WHITE");

        labelHeading.setText(heading);

    }


}