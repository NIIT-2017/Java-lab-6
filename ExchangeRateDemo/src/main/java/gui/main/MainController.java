package gui.main;

import gui.Collector;
import gui.result.ResultController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import money.GetCursOnDateResultParser;
import ru.cbr.web.DailyInfo;
import ru.cbr.web.DailyInfoSoap;
import ru.cbr.web.GetCursOnDateXMLResponse;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MainController implements Initializable{

    @FXML
    public ToggleButton USDBase;
    @FXML
    public ToggleButton EURBase;
    @FXML
    public ToggleButton JPYBase;
    @FXML
    public ToggleButton GBPBase;
    @FXML
    public ToggleButton CNYBase;
    @FXML
    public ToggleButton RUBBase;
    @FXML
    public ToggleButton USDQuote;
    @FXML
    public ToggleButton EURQuote;
    @FXML
    public ToggleButton JPYQuote;
    @FXML
    public ToggleButton GBPQuote;
    @FXML
    public ToggleButton CNYQuote;
    @FXML
    public ToggleButton RUBQuote;
    @FXML
    public Button calculate;

    private boolean flag1;
    private boolean flag2;

    private BigDecimal baseCur;
    private BigDecimal quoteCur;
    private String baseCurTitle;
    private String quoteCurTitle;
    private static GetCursOnDateXMLResponse.GetCursOnDateXMLResult XMLres;

    private ResultController resultController;

    private void setBaseCurrency(String title){
        baseCurTitle = title;
        if (title.equals("RUB"))
            baseCur = BigDecimal.valueOf(1.0001);
        else {
            try {
                baseCur = GetCursOnDateResultParser.getCurrencyByCurrencyCh(title, XMLres).rate;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        flag1 = true;
    }

    private void setQuoteCurrency(String title){
        quoteCurTitle = title;
        if (title.equals("RUB"))
            quoteCur = BigDecimal.valueOf(1.0001);
        else{
            try {
                quoteCur = GetCursOnDateResultParser.getCurrencyByCurrencyCh(title, XMLres).rate;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        flag2 = true;
    }

    private void setCurrencyRate(javafx.event.ActionEvent e){
        ToggleButton btn = (ToggleButton) e.getSource();
        String id = btn.getId();
        String title = id.substring(0,3);
        if (id.substring(3).equals("Base")){
            setBaseCurrency(title);
        }else
        if (id.substring(3).equals("Quote")) {
            setQuoteCurrency(title);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleButton[] buttons = {USDBase,EURBase,JPYBase,GBPBase,CNYBase,RUBBase,USDQuote,EURQuote,JPYQuote,GBPQuote,CNYQuote,RUBQuote};
        for (ToggleButton btn: buttons){
            btn.setOnAction(e ->{
            setCurrencyRate(e);
            btn.getStyleClass().add("selected-toggle-button");
            });
        }
        calculate.setOnAction(e->{
            if (flag1==true&&flag2==true){
                try {
                    for (ToggleButton btn: buttons){
                        btn.getStyleClass().remove("selected-toggle-button");
                    }
                    showResult();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        setXMLres();
    }

    private void setXMLres(){
        DailyInfo service = new DailyInfo();
        DailyInfoSoap port = service.getDailyInfoSoap();
        XMLGregorianCalendar onDate = null;
        try {
            onDate = GetCursOnDateResultParser.getXMLGregorianCalendarNow();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        XMLres = port.getCursOnDateXML(onDate);

    }

     public void showResult() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Parent dialogFrame = loader.load(getClass().getResourceAsStream("/fxml/result.fxml"));
        Scene scene = new Scene(dialogFrame,300,170);
        scene.getStylesheets().addAll(getClass().getResource("/css/result.css").toString());
        resultController = loader.getController();
        Stage stage = new Stage();
        stage.setTitle("Текущий курс");
        stage.setResizable(false);
        stage.setScene(scene);
         stage.show();
        BigDecimal res = baseCur.divide(quoteCur,BigDecimal.ROUND_HALF_EVEN).setScale(4);
        Platform.runLater(() -> Collector.resultController.setResult(baseCurTitle+"/"+quoteCurTitle+": "+
                res.toString())
        );
    }

    public void close(){
        Stage stage = (Stage)calculate.getScene().getWindow();
        stage.close();
    }

    public void showAbout(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("О программе");
        stage.setResizable(false);
        FXMLLoader loader = new FXMLLoader();
        Parent  root = loader.load(getClass().getResourceAsStream("/fxml/about.fxml"));
        Scene scene = new Scene(root,500,300);
        InputStream inputStream = getClass().getResourceAsStream("/about.txt");
        Scanner sc = new Scanner(inputStream,"UTF-8");
        String txt = "";
        while (sc.hasNext())
            txt += sc.nextLine()+"\n";
        Collector.aboutController.text.setText(txt);
        stage.setScene(scene);
        stage.show();
    }
}
