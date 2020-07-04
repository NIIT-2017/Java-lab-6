package task4;

import java.io.Serializable;
import java.util.Currency;
import java.util.Objects;

public class CurrencyExchange implements Serializable {
    private final Currency from;
    private final Currency to;

    public CurrencyExchange(Currency from, Currency to) {
        this.from = from;
        this.to = to;
    }

    public Currency getFrom() {
        return from;
    }

    public Currency getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyExchange exchange = (CurrencyExchange) o;
        return from.equals(exchange.from) &&
                to.equals(exchange.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
