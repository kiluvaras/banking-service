package ee.priit.pall.tuum.service;

import ee.priit.pall.tuum.dto.CurrencyResponse;
import ee.priit.pall.tuum.dto.mapper.CurrencyMapper;
import ee.priit.pall.tuum.entity.Currency;
import ee.priit.pall.tuum.repository.CurrencyRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository repository;
    private final CurrencyMapper mapper;

    public CurrencyServiceImpl(CurrencyRepository repository, CurrencyMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public boolean isCurrencySupported(String isoCode) {
        return repository.findByIsoCode(isoCode) != null;
    }

    public List<String> getCurrencyCodes() {
        return repository.findAllCurrencyCodes();
    }

    public CurrencyResponse getCurrency(String isoCode) {
        Currency currency = repository.findByIsoCode(isoCode);
        return mapper.toResponse(currency);
    }
}
