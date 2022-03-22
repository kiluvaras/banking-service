package ee.priit.pall.tuum.service;

import ee.priit.pall.tuum.entity.Currency;
import ee.priit.pall.tuum.repository.CurrencyRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository repository;

    public CurrencyServiceImpl(CurrencyRepository repository) {
        this.repository = repository;
    }

    public boolean isCurrencySupported(String isoCode) {
        return repository.findByIsoCode(isoCode) != null;
    }

    public List<String> getCurrencyCodes() {
        return repository.findAllCurrencyCodes();
    }

    public Currency getCurrency(String isoCode) {
        return repository.findByIsoCode(isoCode);
    }
}
