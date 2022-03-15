package ee.priit.pall.tuum.service;

import ee.priit.pall.tuum.entity.Currency;
import ee.priit.pall.tuum.repository.BalanceRepository;
import ee.priit.pall.tuum.entity.Balance;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {
    private final BalanceRepository mapper;
    private final CurrencyService currencyService;

    public BalanceService(BalanceRepository mapper,
      CurrencyService currencyService) {
        this.mapper = mapper;
        this.currencyService = currencyService;
    }

    public void createBalance(Long accountId, String currencyCode) {
        Currency currency = currencyService.getCurrency(currencyCode);
        if (currency == null) {
            throw new RuntimeException("Currency not found with iso_code: " + currencyCode);
        }
        mapper.save(accountId, currency.getId());
    }

    public List<Balance> getBalances(Long accountId) {
        return mapper.findByAccountId(accountId);
    }

    public Balance debit(Long accountId, Long currencyId, Long amount) {
        Balance balance = mapper.findByAccountIdAndCurrencyId(accountId, currencyId);
        if (balance == null) {
            throw new RuntimeException("BALANCE_NOT_FOUND");
        }
        if (balance.getAmount() < amount) {
            throw new RuntimeException("NOT_ENOUGH_FUNDS");
        }
        mapper.update(balance.getId(), balance.getAmount() - amount);

        return balance;
    }

    public Balance credit(Long accountId, Long currencyId, Long amount) {
        Balance balance = mapper.findByAccountIdAndCurrencyId(accountId, currencyId);
        if (balance == null) {
            throw new RuntimeException("BALANCE_NOT_FOUND");
        }
        mapper.update(balance.getId(), balance.getAmount() + amount);

        return balance;
    }
}
