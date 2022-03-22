package ee.priit.pall.tuum.service;

import ee.priit.pall.tuum.entity.Currency;
import java.util.List;

public interface CurrencyService {
    boolean isCurrencySupported(String isoCode);
    List<String> getCurrencyCodes();
    Currency getCurrency(String isoCode);
}
