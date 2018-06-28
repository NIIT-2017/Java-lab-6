package Task4;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Document;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;


public class Convertor extends Application {

    private ArrayList<Currency> currencies;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        currencies = new ArrayList<>();
        receiveCurrency();

        HBox root = initFrame();

        primaryStage.setScene(new Scene(root, 500, 100));
        primaryStage.show();
    }

    private void receiveCurrency() {
        try {
            URL url = new URL("http://www.cbr.ru/scripts/XML_daily.asp");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
            NodeList charCodeNodeList = doc.getElementsByTagName("CharCode");
            NodeList valueNodeList = doc.getElementsByTagName("Value");
            NodeList nameNodeList = doc.getElementsByTagName("Name");
            for (int i = 0; i < charCodeNodeList.getLength(); i++) {
                currencies.add(new Currency(nameNodeList.item(i).getTextContent(),
                                charCodeNodeList.item(i).getTextContent(),
                                valueNodeList.item(i).getTextContent()));
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private HBox initFrame() {
        TextField countFirstCurrencyTextField = new TextField("100");
        HBox firstHBox = new HBox(countFirstCurrencyTextField);
        ComboBox<String> firstCurrency = new ComboBox<>();
        Label firstNameLabel = new Label();
        firstNameLabel.setWrapText(true);
        firstNameLabel.setMaxWidth(200);
        VBox firstVBox = new VBox(5.0, countFirstCurrencyTextField, firstCurrency,firstNameLabel);


        TextField countSecondCurrencyTextField = new TextField("100");
        countSecondCurrencyTextField.setEditable(false);
        ComboBox<String> secondCurrency = new ComboBox<>();
        Label secondNameLabel = new Label();
        secondNameLabel.setWrapText(true);
        secondNameLabel.setMaxWidth(200);
        VBox secondVBox = new VBox(5.0, countSecondCurrencyTextField, secondCurrency, secondNameLabel);

        Button exchangeButton = new Button("exchange");
        Label textRateLabel = new Label("rate:");
        Label rateLabel = new Label("1.0");
        VBox center = new VBox(5.0,exchangeButton,textRateLabel,rateLabel);
        center.setAlignment(Pos.CENTER);

        for (Currency next : currencies) {
            firstCurrency.getItems().add(next.getCode());
            secondCurrency.getItems().add(next.getCode());
        }

        firstCurrency.getSelectionModel().select(0);
        firstNameLabel.setText(currencies.get(0).getName());
        secondCurrency.getSelectionModel().select(0);
        secondNameLabel.setText(currencies.get(0).getName());

        firstCurrency.setOnAction(event -> {
            Currency currency1 = currencies.get(firstCurrency.getSelectionModel().getSelectedIndex());
            Currency currency2 = currencies.get(secondCurrency.getSelectionModel().getSelectedIndex());
            firstNameLabel.setText(currency1.getName());
            rateLabel.setText(String.valueOf(currency1.getRate()/currency2.getRate()));
            });

        secondCurrency.setOnAction(event -> {
            Currency currency1 = currencies.get(firstCurrency.getSelectionModel().getSelectedIndex());
            Currency currency2 = currencies.get(secondCurrency.getSelectionModel().getSelectedIndex());
            secondNameLabel.setText(currency2.getName());
            rateLabel.setText(String.valueOf(currency1.getRate()/currency2.getRate()));
        });

        exchangeButton.setOnAction(event -> {
            double count = Double.parseDouble(rateLabel.getText())*Double.parseDouble(countFirstCurrencyTextField.getText());
            countSecondCurrencyTextField.setText(String.valueOf(count));
        });
        HBox root = new HBox(15.0,firstVBox, center, secondVBox);
        root.setAlignment(Pos.CENTER);
        return root;
    }

    private class Currency {
        private final String code;
        private final double rate;
        private final String name;

        Currency(String name, String code, String rate) {
            this.name = name;
            this.code = code;
            this.rate = Double.parseDouble(rate.replaceAll(",","."));
        }

        String getName() {
            return name;
        }

        String getCode() {
            return code;
        }

        double getRate() {
            return rate;
        }
    }
}
