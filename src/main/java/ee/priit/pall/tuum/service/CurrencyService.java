package ee.priit.pall.tuum.service;

import ee.priit.pall.tuum.entity.Currency;
import ee.priit.pall.tuum.repository.CurrencyRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

    private final CurrencyRepository mapper;

    public CurrencyService(CurrencyRepository mapper) {
        this.mapper = mapper;
    }

    public List<Currency> getCurrencies() {
        return mapper.findAll();
    }

    boolean isCurrencySupported(Long id) {
        return mapper.findById(id) != null;
    }

    public List<String> getCurrencyCodes() {
        return mapper.findAllCurrencyCodes();
    }

    public Currency getCurrency(String isoCode) {
        return mapper.findByIsoCode(isoCode);
    }
}
