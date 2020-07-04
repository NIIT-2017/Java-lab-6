package task4.server;

import task4.CurrencyExchange;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class CurrencyRates {
    private static Map<CurrencyExchange, Double> currencyRates = new HashMap<>();

    static {
        currencyRates.put(new CurrencyExchange(Currency.getInstance("RUB"), Currency.getInstance("RUB")), 1.0);
        currencyRates.put(new CurrencyExchange(Currency.getInstance("USD"), Currency.getInstance("USD")), 1.0);
        currencyRates.put(new CurrencyExchange(Currency.getInstance("EUR"), Currency.getInstance("EUR")), 1.0);

        currencyRates.put(new CurrencyExchange(Currency.getInstance("RUB"), Currency.getInstance("USD")), 0.014428);
        currencyRates.put(new CurrencyExchange(Currency.getInstance("USD"), Currency.getInstance("RUB")), 69.290252);

        currencyRates.put(new CurrencyExchange(Currency.getInstance("RUB"), Currency.getInstance("EUR")), 0.012810);
        currencyRates.put(new CurrencyExchange(Currency.getInstance("EUR"), Currency.getInstance("RUB")), 78.061927);

        currencyRates.put(new CurrencyExchange(Currency.getInstance("USD"), Currency.getInstance("EUR")), 0.887865);
        currencyRates.put(new CurrencyExchange(Currency.getInstance("EUR"), Currency.getInstance("USD")), 1.126526);
    }

    public static double getRate(CurrencyExchange currencyExchange) {
        return currencyRates.getOrDefault(currencyExchange, 0.0);
    }
}
