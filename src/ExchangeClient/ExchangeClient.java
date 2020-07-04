package ExchangeClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ExchangeClient {
    private static String CURRENCY_INFO_URL = "https://cbr.ru/currency_base/daily/";
    private ArrayList<String> currency = new ArrayList<>(
            Arrays.asList(new String[]{"EUR", "CZK", "BYN", "USD", "GBP", "CHF", "TRY", "PLN"}));
    private URL url;
    private ArrayList<Element> exchangeRates = new ArrayList<>();

    public void contactToServer () throws IOException {
            try {
                System.out.println("The client is started!");
                url = new java.net.URL(CURRENCY_INFO_URL);
                Document html = Jsoup.parse(url, 5000);
                Elements rows = html.body().getElementsByTag("tr");
                for (Element el : rows) {
                    if (currency.contains(el.child(1).text())) {
                        exchangeRates.add(el);
                    }
                }
            } catch (UnknownHostException e) {
                System.out.println("The host isn`t found!");
            } catch (NoRouteToHostException e) {
                System.out.println("The server isn`t available!");
            } catch (IOException e) {
                System.out.println("The connection request is rejected!");
            }
            System.out.println("The client is disconnected!");
    }

    public ArrayList<String> getExchangeRates() {
        ArrayList<String> exchRates = new ArrayList<>();
        for (int i = 0; i<exchangeRates.size(); i++) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
            String actualTime = dateFormat.format(new Date());
            String information = "For today " + actualTime +  " 1 " + exchangeRates.get(i).child(1).text()
                    + " (" + exchangeRates.get(i).child(3).text()+ ")" + " costs "
                    + exchangeRates.get(i).child(4).text() + " russian rubles";
            System.out.println(information);
            exchRates.add(information);
        }
        return exchRates;
    }

    public static void main(String[] args) throws IOException {
        ExchangeClient justClient = new ExchangeClient();
        justClient.contactToServer();
        justClient.getExchangeRates();
    }
}
