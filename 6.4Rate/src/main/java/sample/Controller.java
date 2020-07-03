package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;

public class Controller {

    @FXML
    private Button btnGetRate;

    @FXML
    private TextField tfDate;
    @FXML
    private TextField tfUSD;
    @FXML
    private TextField tfJPY;
    @FXML
    private TextField tfRUB;

    @FXML
    private void getRate() throws IOException {

        java.net.URL url = new java.net.URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
        java.util.Scanner sc=new java.util.Scanner(url.openStream()); //European Central Bank

        for(int i=0;i<7;i++)
            sc.nextLine(); //go to currency
        String date=sc.nextLine().replaceAll("^.*'(.*)'.*$","$1");
        tfDate.setText(date);
        //<Cube time="2020-07-02">

        String currencyUSD=sc.nextLine().replaceAll("^.*'.*'.*'(.*)'.*$","$1");
        tfUSD.setText(currencyUSD);
        //<Cube currency="USD" rate="1.1286"/>

        String currencyJPY=sc.nextLine().replaceAll("^.*'.*'.*'(.*)'.*$","$1");
        tfJPY.setText(currencyJPY);
        //<Cube currency="JPY" rate="121.24"/>

        for(int i=0;i<12;i++)
            sc.nextLine(); //go to RUB

        String currencyRUB=sc.nextLine().replaceAll("^.*'.*'.*'(.*)'.*$","$1");
        tfRUB.setText(currencyRUB);
        //<Cube currency="RUB" rate="79.4434"/>

        sc.close();
    }

}
