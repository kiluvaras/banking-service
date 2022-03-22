package ee.priit.pall.tuum.service;

import ee.priit.pall.tuum.dto.CurrencyResponse;
import ee.priit.pall.tuum.entity.Currency;
import java.util.List;

public interface CurrencyService {
    boolean isCurrencySupported(String isoCode);
    List<String> getCurrencyCodes();
    CurrencyResponse getCurrency(String isoCode);
}
