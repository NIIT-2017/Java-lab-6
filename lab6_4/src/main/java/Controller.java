import com.tunyk.currencyconverter.BankUaCom;
import com.tunyk.currencyconverter.api.Currency;
import com.tunyk.currencyconverter.api.CurrencyConverter;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.InstrumentApi;
import io.swagger.client.model.Instrument;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.tunyk.currencyconverter.api.Currency.EUR;
import static com.tunyk.currencyconverter.api.Currency.RUB;
import static com.tunyk.currencyconverter.api.Currency.USD;

public class Controller implements Initializable {

    @FXML
    private Button btn_RUB_bas;

    @FXML
    private Button btn_USD_bas;

    @FXML
    private Button btn_EUR_bas;

    @FXML
    private Button btn_LTC_bas;

    @FXML
    private Button btn_ETH_bas;

    @FXML
    private Button btn_XBT_bas;

    @FXML
    private Button btn_RUB_conv;

    @FXML
    private Button btn_USD_conv;

    @FXML
    private Button btn_EUR_conv;

    @FXML
    private Button btn_LTC_conv;

    @FXML
    private Button btn_ETH_conv;

    @FXML
    private Button btn_XBT_conv;

    @FXML
    private Label lbl_Cash;

    @FXML
    private Label lbl_UP;

    @FXML
    private Label lbl_DOWN;

    protected static CurrencyConverter finalCurrencyConverter;
    List<Instrument> result;
    protected int flagCripto=0;
    double LastpriceXBT=0;
    double res=0;
    public void initialize(URL location, ResourceBundle resources) {

        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://www.bitmex.com/api/v1/");

        InstrumentApi apiInstance = new InstrumentApi();

        try

        {
            result= apiInstance.instrumentGetActive();
            Instrument i = result.stream().filter(result -> result.getSymbol().equals("XBTUSD")).findFirst().orElse(null);
            LastpriceXBT = i.getLastPrice();

        } catch (ApiException e) {
            System.err.println("Exception when calling InstrumentApi#instrumentGetActive");
            e.printStackTrace();
        }



        CurrencyConverter currencyConverter = null;
        try {
            currencyConverter = new BankUaCom(RUB, USD);
            lbl_UP.setText("Базовая валюта: Доллар");
            lbl_DOWN.setText("Котируемая валюта: Рубль");
            lbl_Cash.setText(String.valueOf(currencyConverter.convertCurrency(1f, USD, RUB)));
        } catch (CurrencyConverterException e) {
            e.printStackTrace();
        }


        finalCurrencyConverter= currencyConverter;

        btn_RUB_bas.setOnAction(e -> {
            flagCripto =0;
            finalCurrencyConverter.setToCurrency(RUB);
            lbl_UP.setText("Базовая валюта: Рубль");

       });
        btn_USD_bas.setOnAction(e -> {
            flagCripto =0;
            finalCurrencyConverter.setToCurrency(USD);
            lbl_UP.setText("Базовая валюта: Доллар");
        });
        btn_EUR_bas.setOnAction(e -> {
            flagCripto =0;
            finalCurrencyConverter.setToCurrency(EUR);
            lbl_UP.setText("Базовая валюта: Евро");
        });
        btn_XBT_bas.setOnAction(e -> {
            flagCripto =1;
            res=currency(LastpriceXBT,1.0);
            lbl_UP.setText("Базовая валюта: Биткойн");
        });
        btn_LTC_bas.setOnAction(e -> {
            flagCripto =1;
            Instrument i = result.stream().filter(result -> result.getSymbol().equals("LTCZ18")).findFirst().orElse(null);
            double Curlastprice = i.getLastPrice();
            res=currency(LastpriceXBT,Curlastprice);
            lbl_UP.setText("Базовая валюта: Лайткоин");

        });
        btn_ETH_bas.setOnAction(e -> {
            flagCripto =1;
            Instrument i = result.stream().filter(result -> result.getSymbol().equals("ETHUSD")).findFirst().orElse(null);
            double LastpriceETH = i.getLastPrice();
            res=currency(LastpriceETH,1.0);
            lbl_UP.setText("Базовая валюта: Этер");
        });

        btn_RUB_conv.setOnAction(e -> {
            lbl_DOWN.setText("Котируемая валюта: Рубль");
            if (flagCripto==0) {
                try {
                    Currency cur = finalCurrencyConverter.getToCurrency();
                    lbl_Cash.setText(String.valueOf(finalCurrencyConverter.convertCurrency(1f, cur, RUB)));
                } catch (CurrencyConverterException e1) {
                    e1.printStackTrace();
                }
            }
         else{
            double cur=0;
            try {
                cur = finalCurrencyConverter.convertCurrency(1f, USD,RUB );
            } catch (CurrencyConverterException e1) {
                e1.printStackTrace();
            }
            lbl_Cash.setText(String.valueOf(String.format("%.5f",res*cur)));


        }
        });

        btn_USD_conv.setOnAction(e -> {
            lbl_DOWN.setText("Котируемая валюта: Доллар");
            if (flagCripto==0) {
                try {
                    Currency cur = finalCurrencyConverter.getToCurrency();
                    lbl_Cash.setText(String.valueOf(finalCurrencyConverter.convertCurrency(1f, cur, USD)));
                } catch (CurrencyConverterException e1) {
                    e1.printStackTrace();
                }
            }
            else {
                lbl_Cash.setText(String.valueOf(String.format("%.5f",res)));

            }
        });

        btn_EUR_conv.setOnAction(e -> {
            lbl_DOWN.setText("Котируемая валюта: Евро");
            if (flagCripto==0) {
                try {
                    Currency cur = finalCurrencyConverter.getToCurrency();
                    lbl_Cash.setText(String.valueOf(finalCurrencyConverter.convertCurrency(1f, cur, EUR)));
                } catch (CurrencyConverterException e1) {
                    e1.printStackTrace();
                }
            }
            else{
                double cur=0;
                try {
                    cur = finalCurrencyConverter.convertCurrency(1f, USD,EUR );
                } catch (CurrencyConverterException e1) {
                    e1.printStackTrace();
                }
                lbl_Cash.setText(String.valueOf(String.format("%.5f",res*cur)));


            }
        });

        btn_XBT_conv.setOnAction(e -> {
            lbl_DOWN.setText("Котируемая валюта: Биткойн");
          if (flagCripto==0){
             double Calkres = currency1(finalCurrencyConverter.getToCurrency());
              lbl_Cash.setText(String.valueOf(String.format("%.7f",Calkres/LastpriceXBT)));
          }
          else{
              lbl_Cash.setText(String.valueOf(String.format("%.5f",res/LastpriceXBT)));

          }
        });

        btn_LTC_conv.setOnAction(e -> {
            lbl_DOWN.setText("Котируемая валюта: Лайткоин");
            Instrument i = result.stream().filter(result -> result.getSymbol().equals("LTCZ18")).findFirst().orElse(null);
            double Curlastprice = i.getLastPrice();
            if (flagCripto==0){

                double Calkres = currency1(finalCurrencyConverter.getToCurrency());
                lbl_Cash.setText(String.valueOf(String.format("%.5f",Calkres/(Curlastprice*LastpriceXBT))));

            }
            else{
               double LTC =currency(LastpriceXBT,Curlastprice);
                lbl_Cash.setText(String.valueOf(String.format("%.5f",res/LTC)));
            }
        });

        btn_ETH_conv.setOnAction(e -> {
            lbl_DOWN.setText("Котируемая валюта: Этер");
            Instrument i = result.stream().filter(result -> result.getSymbol().equals("ETHUSD")).findFirst().orElse(null);
            double Curlastprice = i.getLastPrice();
            if (flagCripto==0){

                double Calkres = currency1(finalCurrencyConverter.getToCurrency());
                lbl_Cash.setText(String.valueOf(String.format("%.5f",Calkres/Curlastprice)));

            }
            else{
                lbl_Cash.setText(String.valueOf(String.format("%.5f",res/Curlastprice)));
            }
        });

    }

    public static double currency(Double LastpriceXBT, Double Curlastprice){

        double Calckres=LastpriceXBT*Curlastprice;
        return Calckres;
    }

    public static double currency1(Currency value){
        double Calckres=0;
        switch(value){

            case RUB:
                try {
                    Calckres= Controller.finalCurrencyConverter.convertCurrency(1f, value,USD);
                } catch (CurrencyConverterException e) {
                    e.printStackTrace();
                }
             break;
            case USD:
                try {
                    Calckres= Controller.finalCurrencyConverter.convertCurrency(1f, value,USD);
                } catch (CurrencyConverterException e) {
                    e.printStackTrace();
                }
                break;
            case EUR:
                try {
                    Calckres= Controller.finalCurrencyConverter.convertCurrency(1f, value,USD);
                } catch (CurrencyConverterException e) {
                    e.printStackTrace();
                }
                break;
        }

        return Calckres;
    }
}